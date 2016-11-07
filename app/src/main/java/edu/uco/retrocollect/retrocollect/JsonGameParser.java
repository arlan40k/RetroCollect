package edu.uco.retrocollect.retrocollect;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abilb on 10/28/2016.
 */

public class JsonGameParser {

    public static String parseSearchString(String search)
    {

        String myString = search.trim();
        //Chnaged the regex to not remove numbers and spaces
        String another =  myString.replaceAll("[^a-zA-Z0-9[\\s]]", "");
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

               //Deal with title
               String name = jsonArray.getJSONObject(i).getString("name");
               //Id must be brought in as int then converted -HASEEB
               int id  = jsonArray.getJSONObject(i).getInt("id");
               String idString = Integer.toString(id);
               //Deal with Date
             Double release_year = 2005.0;
             String release_date = "7/24/2005";


             long unixSeconds = jsonArray.getJSONObject(i).getLong("first_release_date");
               if(unixSeconds > 0)
               {
                   Date date = new Date( ((int) unixSeconds)); // *1000 is to convert seconds to milliseconds
                   SimpleDateFormat sdf = null; // the format of your date
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                       sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   }
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                       sdf.setTimeZone(TimeZone.getTimeZone("GMT-6")); // give a timezone reference for formating (see comment at the bottom
                   }
                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                       String formattedDate = sdf.format(date);
                       release_year = Double.parseDouble(formattedDate.substring(0, 4));
                       release_date = formattedDate;

                   }
               }

                String publisher = jsonArray.getJSONObject(i).getString("developers");
                String develeoper = jsonArray.getJSONObject(i).getString("publishers");
                Double rating = jsonArray.getJSONObject(i).getDouble("rating");
                release_year = Double.parseDouble(release_date.substring(0, 4));
                if(develeoper == null)
                {
                    develeoper = "Nintendo";
                }
                if(publisher == null)
                {
                    publisher = "Nintendo";
                }
               //put idString in the constructor instead of id -HASEEB
               gameArrayList.add(new Game(name, idString, release_year, release_date, publisher, develeoper, rating));
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }

       return gameArrayList;
    }
}
