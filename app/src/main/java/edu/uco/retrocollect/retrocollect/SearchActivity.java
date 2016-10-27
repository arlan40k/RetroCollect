package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchActivity extends Activity {
    ListView lstView;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lstView = (ListView) findViewById(R.id.lstView);
        bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String game_name = bundle.getString("game");
            new IgdbApiTask().execute(game_name);
        }


   }

    private class IgdbApiTask extends AsyncTask <Object, Void, HttpResponse<String>> {
        @Override
        protected HttpResponse<String> doInBackground(Object[] objects) {
            HttpResponse<String> response = null;
            try {
                String game_name = (String) objects[0];
                String game = game_name.replace(" ", "+");
                 response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/games/" +
                         "?fields=name&limit=10&offset=0&order=release_dates.date%3Adesc&search=zelda")
                        .header("X-Mashape-Key", "4KjzzTanigmshoC1cuOPyXU16sUvp1xp5m7jsnV3lAlo5HH0wK")
                        .header("Accept", "application/json")
                        .asString();

               return response;
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(HttpResponse<String> response) {
            if (response == null | response.getBody().length() <= 0) {
                Toast.makeText(SearchActivity.this,
                        "Invalid data. Possibly a wrong query",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {

                String modresponse = response.getBody().replace("\"name\":", "");
                ArrayList<String> responseList = new ArrayList<>();

                StringTokenizer stringTokenizer = new StringTokenizer(modresponse, "[{}],");
                while(stringTokenizer.hasMoreTokens())
                {
                    stringTokenizer.nextToken();
                    responseList.add(stringTokenizer.nextToken());
                }
                String[] rl = new String[responseList.size()];
                        rl = responseList.toArray(rl);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        SearchActivity.this, android.R.layout.simple_list_item_1, rl);
                lstView.setAdapter(arrayAdapter);
            }


        }



    }
}


