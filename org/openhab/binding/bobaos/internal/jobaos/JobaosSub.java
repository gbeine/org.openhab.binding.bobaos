package org.openhab.binding.bobaos.internal.jobaos;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.JsonWriter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JobaosSub {
    private Random randomGenerator;
    private String redisUri, bobaosReqChannel, bobaosServiceChannel, resPrefix, bobaosBroadcastChannel;

    private Jedis pub, sub, responseSub;

    ArrayList<DatapointValueListener> listeners;

    // for all reqs
    public void commonRequest(JsonObject request, String reqChannel, String responseChannel,
            BobaosResponseHandler handler) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = Json.createWriter(stringWriter);
        writer.writeObject(request);
        writer.close();

        // JSON.stringify
        String message = stringWriter.getBuffer().toString();

        System.out.println(message);

        responseSub.subscribe(new JedisPubSub() {
            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                // TODO Auto-generated method stub
                super.onSubscribe(channel, subscribedChannels);
                pub.publish(reqChannel, message);
            }

            @Override
            public void onMessage(String channel, String message) {
                JsonReader jsonReader = Json.createReader(new StringReader(message));
                JsonObject object = jsonReader.readObject();
                jsonReader.close();

                String method = object.getJsonString("method").getString();

                JsonValue payload = object.get("payload");

                // TODO: callback?
                if (method.contains("success")) {
                    handler.onSuccess(payload);
                } else if (method.contains("error")) {
                    handler.onError(payload.toString());
                }

                unsubscribe();
            }
        }, responseChannel);
    }

    // service reqs
    public void ping(BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "ping").add("payload", JsonValue.NULL)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosServiceChannel, responseChannel, handler);
    }

    public void reset(BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "reset").add("payload", JsonValue.NULL)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosServiceChannel, responseChannel, handler);
    }

    public void state(BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "get sdk state").add("payload", JsonValue.NULL)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosServiceChannel, responseChannel, handler);
    }

    // datapoint reqs
    public void getValue(int id, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "get value").add("payload", id)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void getStoredValue(int id, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "get stored value").add("payload", id)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void readValue(int id, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "read value").add("payload", id)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void pollValues(BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "poll values").add("payload", JsonValue.NULL)
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void setValue(int id, Boolean value, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "set value")
                .add("payload", Json.createObjectBuilder().add("id", id).add("value", value))
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void setValue(int id, int value, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "set value")
                .add("payload", Json.createObjectBuilder().add("id", id).add("value", value))
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void setValue(int id, double value, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "set value")
                .add("payload", Json.createObjectBuilder().add("id", id).add("value", value))
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    public void setValue(int id, String value, BobaosResponseHandler handler) {
        // generate random [1-255] number
        int randomNum = randomGenerator.nextInt((255 - 1) + 1) + 1;
        // response channel: prefix_1, prefix_2, ... prefix_255
        String responseChannel = resPrefix + "_" + randomNum;

        // compose request
        JsonObject request = Json.createObjectBuilder().add("method", "set value")
                .add("payload", Json.createObjectBuilder().add("id", id).add("value", value))
                .add("response_channel", responseChannel).build();

        commonRequest(request, bobaosReqChannel, responseChannel, handler);
    }

    private void processDatapointValueJson(JsonValue payload) {
        // depends on type of payload
        ValueType mt = payload.getValueType();

        // process single object
        if (mt.compareTo(ValueType.OBJECT) == 0) {
            JsonObject pl = payload.asJsonObject();
            int id = pl.getInt("id");
            JsonValue jvalue = pl.get("value");

            ValueType vt = jvalue.getValueType();

            // depends on value type
            if (vt.compareTo(ValueType.NUMBER) == 0) {
                JsonNumber jnumber = (JsonNumber) jvalue;
                if (jnumber.isIntegral()) {
                    int value = jnumber.intValue();
                    for (DatapointValueListener listener : listeners) {
                        listener.onValue(id, value);
                    }

                } else {
                    double value = jnumber.doubleValue();
                    for (DatapointValueListener listener : listeners) {
                        listener.onValue(id, value);
                    }
                }
            }
            if (vt.compareTo(ValueType.TRUE) == 0) {
                for (DatapointValueListener listener : listeners) {
                    listener.onValue(id, true);
                }
            }
            if (vt.compareTo(ValueType.FALSE) == 0) {
                for (DatapointValueListener listener : listeners) {
                    listener.onValue(id, false);
                }
            }
            if (vt.compareTo(ValueType.STRING) == 0) {
                JsonString jstring = (JsonString) jvalue;
                String value = jstring.getString();
                for (DatapointValueListener listener : listeners) {
                    listener.onValue(id, value);
                }
            }
            if (vt.compareTo(ValueType.OBJECT) == 0) {
                // do nothing yet
            }
        }

        // process array
        if (mt.compareTo(ValueType.ARRAY) == 0) {
            JsonArray pa = payload.asJsonArray();
            // recursion
            for (JsonValue t : pa) {
                processDatapointValueJson(t);
            }
        }
    }

    public void registerValueListener(DatapointValueListener listener) {
        listeners.add(listener);
        System.out.println("Listener registered");
    }

    public void subscribe() {
        // https://groups.google.com/forum/#!topic/redis-db/B7Oc3cMZk10
        // run in separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sub.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            // System.out.println("incoming message: " + message);

                            JsonReader jsonReader = Json.createReader(new StringReader(message));
                            JsonObject object = jsonReader.readObject();
                            jsonReader.close();

                            String method = object.getJsonString("method").getString();

                            JsonValue payload = object.get("payload");
                            if (method.contains("datapoint value")) {
                                processDatapointValueJson(payload);
                                return;
                            }
                        }
                    }, bobaosBroadcastChannel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void dispose() {
        pub.disconnect();
        sub.disconnect();
        responseSub.disconnect();
    }

    public JobaosSub(String redisUri, String requestChannel, String serviceChannel, String resPrefix,
            String broadcastChannel) {
        this.redisUri = redisUri;
        this.bobaosReqChannel = requestChannel;
        this.bobaosServiceChannel = serviceChannel;
        this.resPrefix = resPrefix;
        this.bobaosBroadcastChannel = broadcastChannel;

        this.listeners = new ArrayList<DatapointValueListener>();

        try {
            // init pub/sub/redis client
            pub = new Jedis(new URI(this.redisUri));
            sub = new Jedis(new URI(this.redisUri));
            responseSub = new Jedis(new URI(this.redisUri));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        randomGenerator = new Random();
    }
}