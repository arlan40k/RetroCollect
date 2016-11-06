package edu.uco.retrocollect.retrocollect;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Hasee on 11/5/2016.
 */

public class JsonGameImageParser {
    public static String parseSearchString(String search)
    {


        String newString = search;

        return newString;
    }

    public static ArrayList<String> getGameList(HttpResponse<JsonNode> jsonNodeHttpResponse)
    {
        //The Purpose of this block is to convert the Json object into a usable array of objects.
        JsonNode jsonNodeBody = jsonNodeHttpResponse.getBody();
        JSONArray jsonArray =  jsonNodeBody.getArray();
        ArrayList<String> gameCoverUrl = null;
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {

                //Deal with title
                String coverUrl = jsonArray.getJSONObject(i).getString("cover");
                

                gameCoverUrl.add(coverUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return gameCoverUrl;
    }
}

