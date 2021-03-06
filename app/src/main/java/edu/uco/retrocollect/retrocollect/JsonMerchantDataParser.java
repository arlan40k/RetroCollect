package edu.uco.retrocollect.retrocollect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Garrett A. Clement on 10/27/2016.
 */

public class JsonMerchantDataParser {
    public static ArrayList<String> getData(String forecast) throws JSONException {
        ArrayList<String> results = new ArrayList<>();
        JSONObject localMerchantJASON = new JSONObject((forecast));

        try{
            if(!LocalMerchantActivity.isInit) {
                JSONObject data = localMerchantJASON.getJSONObject("result");

                results.add(data.getString("name"));
                results.add(data.getString("formatted_phone_number"));
                results.add(data.getString("website"));
                results.add(data.getJSONObject("opening_hours").getString("open_now"));
                results.add(data.getString("place_id"));

                JSONArray tmp = data.getJSONObject("opening_hours").getJSONArray("weekday_text");
                String weekdays = "";

                for(int i = 0; i < tmp.length(); i++)
                        weekdays += tmp.getString(i) + "\n";

                results.add(weekdays);


                System.out.print("b");
            }else{

                JSONArray stores = localMerchantJASON.getJSONArray("results");
                JSONObject currentStore;
                JSONObject location;
                for(int i = 0; i <  stores.length(); i++) {
                    currentStore = stores.getJSONObject(i);
                    if (currentStore.getString("name").equals("GameStop") || currentStore.getString("name").equals("Vintage Stock")) {

                        results.add(currentStore.getString("name"));
                        location = currentStore.getJSONObject("geometry").getJSONObject("location");
                        results.add(Double.toString(location.getDouble("lat")));
                        results.add(Double.toString(location.getDouble("lng")));
                        results.add(currentStore.getString("place_id"));

                    }
                }

            }

            return results;
        } catch(JSONException ex){
            System.out.println("CRAP");
        }
        return results;


    }
}
