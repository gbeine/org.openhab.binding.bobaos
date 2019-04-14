/**
 * Copyright (c) 2014,2019 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.bobaos.internal;

import java.math.BigDecimal;

import javax.json.JsonValue;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.bobaos.internal.jobaos.BobaosResponseHandler;
import org.openhab.binding.bobaos.internal.jobaos.DatapointValueListener;
import org.openhab.binding.bobaos.internal.jobaos.JobaosSub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BobaosHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Vladimir Shabunin - Initial contribution
 */
@NonNullByDefault
public class BobaosHandler extends BaseThingHandler {

    @Nullable
    private JobaosSub bobaos;

    private final Logger logger = LoggerFactory.getLogger(BobaosHandler.class);

    @Nullable
    private BobaosConfiguration config;

    public BobaosHandler(Thing thing) {
        super(thing);
        logger.debug("hello");
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handle comand: ");
        logger.debug(channelUID.toString() + ": " + command.toString());

        String channelId = channelUID.getId();
        Channel channel = getThing().getChannel(channelId);

        Configuration channelConfig = channel.getConfiguration();

        // [{key=control; type=BigDecimal; value=2}, {key=status; type=BigDecimal; value=6}]
        System.out.println(channelConfig.toString());

        BigDecimal control = (BigDecimal) channelConfig.get("control");

        int id = control.intValue();

        if (command instanceof OnOffType) {
            if ((OnOffType) command == OnOffType.ON) {
                bobaos.setValue(id, true, new BobaosResponseHandler() {
                    @Override
                    public void onSuccess(@Nullable JsonValue payload) {
                        logger.debug("Set true value success");
                    }
                });
            } else {
                bobaos.setValue(id, false, new BobaosResponseHandler() {
                    @Override
                    public void onSuccess(@Nullable JsonValue payload) {
                        logger.debug("Set false value success");
                    }
                });
            }

        }
    }

    @Override
    public void initialize() {
        // logger.debug("Start initializing!");
        config = getConfigAs(BobaosConfiguration.class);

        String redisURI = config.redisURI;
        String requestChannel = config.requestChannel;
        String serviceChannel = config.serviceChannel;
        String broadcastChannel = config.broadcastChannel;
        String resPrefix = config.resPrefix;

        bobaos = new JobaosSub(config.redisURI, requestChannel, serviceChannel, resPrefix, broadcastChannel);

        // TODO: update channels if present
        bobaos.registerValueListener(new DatapointValueListener() {
            @Override
            public void onValue(int id, @Nullable Boolean value) {
                logger.debug("bool value: " + id + " = " + value);
                // try to find switch channels with `id` datapoint as a status
                for (Channel channel : getThing().getChannels()) {
                    logger.debug(channel.toString());
                    logger.debug(channel.getChannelTypeUID().getAsString());
                    logger.debug(BobaosBindingConstants.CHANNEL_TYPE_SWITCH.getAsString());
                    if (channel.getChannelTypeUID().equals(BobaosBindingConstants.CHANNEL_TYPE_SWITCH)) {
                        logger.debug("iiiis switch");
                        Configuration channelConfig = channel.getConfiguration();

                        // [{key=control; type=BigDecimal; value=2}, {key=status; type=BigDecimal; value=6}]
                        System.out.println(channelConfig.toString());

                        BigDecimal statusDecimal = (BigDecimal) channelConfig.get("status");
                        int status = statusDecimal.intValue();

                        if (id == status) {
                            if (value) {
                                updateState(channel.getUID(), OnOffType.ON);
                            } else {
                                updateState(channel.getUID(), OnOffType.OFF);
                            }
                        }
                    }
                }
            }

            @Override
            public void onValue(int id, int value) {
                logger.debug("int value: " + id + " = " + value);
            }

            @Override
            public void onValue(int id, double value) {
                logger.debug("double value: " + id + " = " + value);
            }

            @Override
            public void onValue(int id, @Nullable String value) {
                logger.debug("string value: " + id + " = " + value);
            }
        });
        bobaos.subscribe();

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly. Also, before leaving this method a thing
        // status from one of ONLINE, OFFLINE or UNKNOWN must be set. This might already be the real thing status in
        // case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        // Example for background initialization:
        scheduler.execute(() -> {
            bobaos.ping(new BobaosResponseHandler() {
                @Override
                public void onSuccess(@Nullable JsonValue payload) {
                    System.out.println("here comes success: " + payload.toString());
                    updateStatus(ThingStatus.ONLINE);
                }
            });
        });
    }

    @Override
    public void dispose() {
        logger.debug("Disposing of server");
        bobaos.dispose();
    }
}
