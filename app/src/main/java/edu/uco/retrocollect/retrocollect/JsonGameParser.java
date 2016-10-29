package edu.uco.retrocollect.retrocollect;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by abilb on 10/28/2016.
 */

public class JsonGameParser {

    public static String parseSearchString(String search)
    {

        String myString = search.trim();
        String another =  myString.replaceAll("[^a-zA-Z ]", "");
        String newString = another.replace(" ", "+");

        return newString;
    }

    public static ArrayList<Game> getGameList(HttpResponse<JsonNode> jsonNodeHttpResponse)
    {
        //The Purpose of this block is to convert the Json object into a usable array of objects.
       JsonNode jsonNodeBody = jsonNodeHttpResponse.getBody();
       JSONArray jsonArray =  jsonNodeBody.getArray();
       ArrayList<Game> gameArrayList = new ArrayList<>();
       for(int i = 0; i < jsonArray.length(); i++)
       {
           try {
               String name = jsonArray.getJSONObject(i).getString("name");
               gameArrayList.add(new Game(name));
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }

       return gameArrayList;
    }
}
