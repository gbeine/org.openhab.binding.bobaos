package org.openhab.binding.bobaos.internal.jobaos;

import javax.json.JsonValue;

public class BobaosResponseHandler {
    public void onSuccess(JsonValue payload) {
    }

    public void onError(String err) {
        System.out.println("Error on request: " + err);
    }
}