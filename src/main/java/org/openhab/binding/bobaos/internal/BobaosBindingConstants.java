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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;

/**
 * The {@link BobaosBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Vladimir - Initial contribution
 */
@NonNullByDefault
public class BobaosBindingConstants {

    public static final String BINDING_ID = "bobaos";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "boba");

    // channel uids?
    public static final ChannelTypeUID CHANNEL_TYPE_SWITCH = new ChannelTypeUID(BINDING_ID, "datapoint-switch-type");
}
