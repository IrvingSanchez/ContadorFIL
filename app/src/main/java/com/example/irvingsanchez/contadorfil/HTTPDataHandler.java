package com.example.irvingsanchez.contadorfil;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by irvingsanchez on 12/09/17.
 */

public class HTTPDataHandler {
    static String stream = null;
    private static String DEBUG_MESSAGE = "Respuesta de Servidor: ";
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int OK = 200;
    private int codigoRespuesta;
    JSONObject jsonObject = new JSONObject();

    public HTTPDataHandler(){}

    public String getHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection
                    = (HttpURLConnection) url.openConnection();
            codigoRespuesta = urlConnection.getResponseCode();
            //codigoRespuesta =500;
            Log.i(DEBUG_MESSAGE, "Codigo " + codigoRespuesta);
            switch (codigoRespuesta)
            {
                case OK:
                    InputStream in =
                            new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = r.readLine()) != null){
                        sb.append(line);
                    }
                    stream = sb.toString();
                    urlConnection.disconnect();
                    break;
                case INTERNAL_SERVER_ERROR:

                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("estado",INTERNAL_SERVER_ERROR);
                        jsonObject.put("mensaje","Error del servidor");
                        stream = jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SERVICE_UNAVAILABLE:
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("estado",SERVICE_UNAVAILABLE);
                        jsonObject.put("mensaje","Servidor congestionado");
                        stream = jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (MalformedURLException e){
            Log.i(DEBUG_MESSAGE, "MalformedURL");
            e.printStackTrace();
        }catch (IOException e){
            Log.i(DEBUG_MESSAGE,"IOException");
            e.printStackTrace();
        }finally {

        }
        return stream;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }
}
