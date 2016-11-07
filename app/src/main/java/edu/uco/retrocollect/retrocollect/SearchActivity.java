package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    SqlGameHelper sqlGameHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lstView = (ListView) findViewById(R.id.lstView);

        //Initialize DB
        sqlGameHelper = new SqlGameHelper(this);

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
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                // sqlGameHelper.addGame(loadedGames.get(i));
                Game game = loadedGames.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("gameId", game.getGameId());
                bundle.putString("gameTitle", game.getTitle());
                bundle.putDouble("gameReleaseYear", game.getReleaseYear());
                bundle.putString("gameReleaseDate", game.getReleaseDate());
                bundle.putString("gamePublisher", game.getPublisher());
                bundle.putString("gameStudio", game.getStudio());
                bundle.putDouble("gameRating", game.getRating());
                bundle.putString("coverHash", game.getCoverHash()); // - HASEEB
                Log.d("sendHash", game.getCoverHash() + " "); // -haseeb debugging
                SearchLongClickFragment searchLongClickFragment = new SearchLongClickFragment();
                searchLongClickFragment.setArguments(bundle);
                searchLongClickFragment.show(getFragmentManager(), "test");
                return true;
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gameActivity = new Intent(SearchActivity.this, GameActivity.class);
                //Changed "game_name" to "gameTitle" so that GameActivity can access it
                gameActivity.putExtra("gameTitle", loadedGames.get(i).getTitle());
                gameActivity.putExtra("gameID", loadedGames.get(i).getGameId());

                gameActivity.putExtra("gameReleaseYear", Double.toString(loadedGames.get(i).getReleaseYear()).substring(0,4));
                gameActivity.putExtra("gameReleaseDate", loadedGames.get(i).getReleaseDate());
                gameActivity.putExtra("gameStudio", loadedGames.get(i).getStudio());
                gameActivity.putExtra("gamePublisher", loadedGames.get(i).getPublisher());
                gameActivity.putExtra("gameRating", Double.toString(loadedGames.get(i).getRating()).substring(0,5));
                gameActivity.putExtra("coverHash", loadedGames.get(i).getCoverHash()); // -HASEEB
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
                //Changed order to relevance rather than date released -HASEEB
                 response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/games/" +
                         "?fields=*&limit=30&offset=0&search=" + searchString)
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
                String hash = gameArrayList.get(0).getCoverHash() + " ";
                String[] rl = new String[gameArrayList.size()];
                for(int i = 0; i < gameArrayList.size(); i++)
                {
                    rl[i] = gameArrayList.get(i).getTitle();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        SearchActivity.this, android.R.layout.simple_list_item_1, rl);
                lstView.setAdapter(arrayAdapter);
               // String hash = gameArrayList.get(0).getCoverHash() + " ";
                Log.d("arrayListHash", hash);
                loadedGames = gameArrayList;
            }


        }



    }


}


