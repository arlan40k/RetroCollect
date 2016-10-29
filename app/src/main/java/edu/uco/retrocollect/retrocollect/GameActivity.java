package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends Activity {
    Bundle bundle;
    private TextView gameTitleTextView, gamePublisherTextView, gameStudioTextView,
            gameReleaseYearTextView, gameReleaseDateTextView, gameRatingTextView;
    private final String dataErrorString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameTitleTextView = (TextView) findViewById(R.id.gameTitleTextView);
        gamePublisherTextView = (TextView) findViewById(R.id.gamePublisherTextView);
        gameStudioTextView = (TextView) findViewById(R.id.gameStudioTextView);
        gameReleaseYearTextView = (TextView) findViewById(R.id.gameReleaseYearTextView);
        gameReleaseDateTextView = (TextView) findViewById(R.id.gameReleaseDateTextView);
        gameRatingTextView = (TextView) findViewById(R.id.gameRatingTextView);

        bundle = getIntent().getExtras();
        if (bundle != null) {

            String title = bundle.getString("gameTitle");
            if (title != null) {
                gameTitleTextView.setText(title);
            } else {
                gameTitleTextView.setText(dataErrorString);
            }
            String publisher = bundle.getString("gamePublisher");
            if (title != null) {
                gamePublisherTextView.setText(publisher);
            } else {
                gamePublisherTextView.setText(dataErrorString);
            }
            String studio = bundle.getString("gameStudio");
            if (title != null) {
                gameStudioTextView.setText(studio);
            } else {
                gameStudioTextView.setText(dataErrorString);
            }
            String year = bundle.getString("gameReleaseYear");
            if (year != null) {
                gameReleaseYearTextView.setText(year);
            } else {
                gameReleaseYearTextView.setText(dataErrorString);
            }
            String date = bundle.getString("gameReleaseDate");
            if (date != null) {
                gameReleaseDateTextView.setText(date);
            } else {
                gameReleaseDateTextView.setText(dataErrorString);
            }
            String rating = bundle.getString("gameRating");
            if (title != null) {
                gameRatingTextView.setText(rating);
            } else {
                gameRatingTextView.setText(dataErrorString);
            }

        }

    }
/*    private class IgdbApiGameInfoTask extends AsyncTask<Object, Void, HttpResponse<JsonNode>> {

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
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                        SearchActivity.this, android.R.layout.simple_list_item_1, rl);
//                lstView.setAdapter(arrayAdapter);
//                loadedGames = gameArrayList;
            }


        }



    }*/
}
