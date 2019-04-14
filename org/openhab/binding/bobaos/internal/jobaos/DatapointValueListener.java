package org.openhab.binding.bobaos.internal.jobaos;

public class DatapointValueListener {

    public void onValue(int id, Boolean value) {
        // System.out.println("default listener bool value: " + id + " = " + value);
    }

    public void onValue(int id, int value) {
        // System.out.println("default listener int value: " + id + " = " + value);
    }

    public void onValue(int id, double value) {
        // System.out.println("default listener double value: " + id + " = " + value);
    }

    public void onValue(int id, String value) {
        // System.out.println("default listener string value: " + id + " = " + value);
    }

    public DatapointValueListener() {
        // TODO Auto-generated constructor stub
        // class to register incoming values
        // OnValue method will be overrided
        /****
         *
         *
         *
         * bobaos.registerValueListener(new DatapointValueListener() {
         *
         * @Override
         *           public void onValue(int id, Boolean value) {
         *           System.out.println("bool value: " + id + " = " + value);
         *           }
         *
         * @Override
         *           public void onValue(int id, int value) {
         *           System.out.println("int value: " + id + " = " + value);
         *           }
         *
         * @Override
         *           public void onValue(int id, double value) {
         *           System.out.println("double value: " + id + " = " + value);
         *           }
         *
         * @Override
         *           public void onValue(int id, String value) {
         *           System.out.println("string value: " + id + " = " + value);
         *           }
         *           });
         *
         */
    }
}
