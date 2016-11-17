package edu.uco.retrocollect.retrocollect;

import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hasee on 11/17/2016.
 */

public class JsonBarcodeParser {

    public static String parseSearchString(String search)
    {

        String myString = search.trim();
        //Chnaged the regex to not remove numbers and spaces
        String another =  myString.replaceAll("[^0-9,]", "");
        String newString = another.replace(" ", "+");

        return newString;
    }

    public static ArrayList<String> getGameList(HttpResponse<JsonNode> jsonNodeHttpResponse)
    {
        //The Purpose of this block is to convert the Json object into a usable array of objects.
        JsonNode jsonNodeBody = jsonNodeHttpResponse.getBody();
        JSONArray jsonArray =  jsonNodeBody.getArray();
        ArrayList<String> gameArrayList = new ArrayList<>();
        //Log.d("nametime4", "yup");
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {

                //Deal with title


                JSONObject barObj = jsonArray.getJSONObject(i);

                JSONObject prodObj = barObj.getJSONObject("product");
               // JSONObject nameObj = prodObj.getJSONObject("public_name");

                String name = prodObj.getString("public_name");

                Log.d("Nametime", name);


                gameArrayList.add(name);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return gameArrayList;
    }
}
