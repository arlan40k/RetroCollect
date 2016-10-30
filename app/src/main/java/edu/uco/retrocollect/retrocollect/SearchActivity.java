package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class SearchActivity extends Activity {
    ListView lstView;
    Bundle bundle;
    ArrayList<Game> loadedGames;
    EditText txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lstView = (ListView) findViewById(R.id.lstView);

        //Bundle from Main Activity
        bundle = getIntent().getExtras();
        if(bundle != null)
        {
            //Get string from bundle
            String game_name = bundle.getString("game");
            //Api async call
            new IgdbApiTask().execute(game_name);
        }

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gameActivity = new Intent(SearchActivity.this, GameActivity.class);
                //Changed "game_name" to "gameTitle" so that GameActivity can access it
                gameActivity.putExtra("gameTitle", loadedGames.get(i).getTitle());
                startActivity(gameActivity);
            }
        });

   }
    //How to search on enter key...
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            new IgdbApiTask().execute(txtSearch.getText().toString());
            return true;
        }
        return super.dispatchKeyEvent(e);
    };
    private class IgdbApiTask extends AsyncTask <Object, Void, HttpResponse<JsonNode>> {

        //Network Activities must be done in  doInBackground
        @Override
        protected HttpResponse<JsonNode> doInBackground(Object[] objects) {
            HttpResponse<JsonNode> response = null;
            try {
                //Get my string from the objects
                String game_name = (String) objects[0];

                //API Request
                String searchString = JsonGameParser.parseSearchString(game_name);
                 response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/games/" +
                         "?fields=name&limit=30&offset=0&order=release_dates.date%3Adesc&search=" + searchString)
                        .header("X-Mashape-Key", "4KjzzTanigmshoC1cuOPyXU16sUvp1xp5m7jsnV3lAlo5HH0wK")
                        .header("Accept", "application/json")
                        .asJson();

               return response;
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (response == null ) {
                Toast.makeText(SearchActivity.this,
                        "Invalid data. Possibly a wrong query",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {

                ArrayList<Game> gameArrayList = JsonGameParser.getGameList(response);
                String[] rl = new String[gameArrayList.size()];
                for(int i = 0; i < gameArrayList.size(); i++)
                {
                    rl[i] = gameArrayList.get(i).getTitle();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        SearchActivity.this, android.R.layout.simple_list_item_1, rl);
                lstView.setAdapter(arrayAdapter);
                loadedGames = gameArrayList;
            }


        }



    }


}


