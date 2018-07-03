package com.example.android.krishibox.Utils;

import android.util.Log;

import com.example.android.krishibox.Collections.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class LoginUtil {

    private static final String LOG_TAG = LoginUtil.class.getSimpleName().toString();

    private LoginUtil(){
        //empty constructor
    }

    public static String[] sendRegistrationData (String userName, String userNumber, String JsonString) {
        URL url = createUrl(JsonString);
        Log.v(LOG_TAG,url.toString());
        String[] JsonResponse = new String[2];
        JsonResponse[0] = null;
        JsonResponse[1] = null;
        String toSend = createSendData(userName,userNumber);

        try {
            JsonResponse = makePOSTRequest(url,toSend);
            Log.v(LoginUtil.class.getSimpleName(),JsonResponse[0]);
        } catch (IOException e) {
            Log.e(LoginUtil.class.getSimpleName(),"error making http request ",e);
        }

        return JsonResponse;


    }

    public static void extractFromJson(String jsonResponse) {

        String userName = "";
        String userNumber = "";
        String userID = "";
        Log.v(LoginUtil.class.getSimpleName(),"response "+ jsonResponse );

        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            userName = rootObject.optString("name");
            Long number = rootObject.optLong("number");
            userNumber = String.valueOf(number);
            userID = rootObject.optString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        User user = new User(userName,userNumber,userID);
        Log.v(LOG_TAG,user.getID());


    }

    private static String createSendData(String userName, String userNumber)  {

        JSONObject toSend = new JSONObject();
        try {
            toSend.put("name",userName);
            toSend.put("number",userNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(LOG_TAG, String.valueOf(toSend));
        return toSend.toString();
    }


    private static String[] makePOSTRequest(URL url, String toSend) throws IOException {

        String[] JsonResponse = new String[2];
        JsonResponse[0] = "";
        JsonResponse[1] = "";
        if(url==null || toSend.length()<0)
            return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
           urlConnection.setRequestProperty("Content-Type", "application/json");
            /*urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            //Log.v(LoginUtil.class.getSimpleName(), String.valueOf(urlConnection.getResponseCode()));


                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));
                writer.write(String.valueOf(toSend));

                writer.close();*/
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(toSend);
            writer.flush();
            writer.close();

            int response_code = urlConnection.getResponseCode();
            Log.v(LOG_TAG, String.valueOf(response_code));
            if(response_code == HttpsURLConnection.HTTP_OK)
            {
                JsonResponse[1] = "200";
                inputStream = urlConnection.getInputStream();
                JsonResponse[0] = readFromInputStream(inputStream);
            }
            else
            {
                JsonResponse[1] = "400";
                return JsonResponse;
            }



        } catch (ConnectException e)
        {
            JsonResponse[1] = "no";
            Log.e(LOG_TAG,"timeoutz");
            return JsonResponse;

        }

        catch (SocketTimeoutException e)
        {
            Log.v(LOG_TAG,"timeout");
        } catch (IOException e) {
            Log.e(LoginUtil.class.getSimpleName(),"error in making http connection "+ e);
        } finally {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }

        return JsonResponse;


    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String input = bufferedReader.readLine();
            while(input!=null)
            {
                output.append(input);
                input = bufferedReader.readLine();
            }

        }
        Log.v(LoginUtil.class.getSimpleName(),output.toString());

        return output.toString();
    }

    private static URL createUrl(String jsonString) {
        URL url = null;
        try {
            url = new URL((jsonString));
            Log.v(LoginUtil.class.getSimpleName(), String.valueOf(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
