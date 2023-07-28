package com.example.registroincidentes;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetLocationAPI{


    public interface OnLocationInfoListener {
        void onLocationInfoReceived(JSONObject jsonObject);

        void onError(Exception e);
    }

    public static void getLocationInfo(Double lat, Double lng, OnLocationInfoListener listener) {
        String apiKey = "AIzaSyBUWRuZtSD3ANqYcmjXiTaEtB0dTX_S3mQ"; // Reemplaza esto con tu clave de acceso a la API
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng + "&sensor=true&key=" + apiKey;

        LocationInfoTask task = new LocationInfoTask(listener);
        task.execute(apiUrl);
    }

    private static class LocationInfoTask extends AsyncTask<String, Void, JSONObject> {
        private OnLocationInfoListener listener;

        LocationInfoTask(OnLocationInfoListener listener) {
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();

            try {
                String apiUrl = urls[0];
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Leer la respuesta
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                // Crear el JSONObject a partir de la respuesta
                jsonObject = new JSONObject(responseBuilder.toString());
                Log.i("API", jsonObject.toString());

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                listener.onError(e);
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            listener.onLocationInfoReceived(jsonObject);
        }
    }
}

