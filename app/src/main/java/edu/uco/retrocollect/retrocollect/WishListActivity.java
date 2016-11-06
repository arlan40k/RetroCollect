package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListActivity extends Activity {

    private ListView gamesList;
    private String gameTitle, gamePublisher, gameStudio, gameReleaseYear, gameReleaseDate,
            gameRating;
    private Game temporaryGame;

    //Adam Bilby
//Static games will now be dynamic with database games
    Game[] gameArray = {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        //Adam Bilby
        //Sql Datbase initialize
        SqlWishListHelper sqlWishListHelper = new SqlWishListHelper(this);
        //Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate,
        // String gamePublisher, String gameStudio, double gameRating) {

        ArrayList<Game> gameArrayList =  sqlWishListHelper.getAllGames();

        //Transform ArrayList into Array
        gameArray = new Game[gameArrayList.size()];
        gameArray = gameArrayList.toArray(gameArray);
        //End of Adam Bilby Block
        gamesList = (ListView) findViewById(R.id.gamesList);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < gameArray.length; ++i) {
            list.add(gameArray[i].getTitle() + "     " + gameArray[i].getReleaseYear());
        }

        final WishListActivity.MyArrayAdapter adapter = new WishListActivity.MyArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        //ArrayAdapter adapter = new ArrayAdapter<String>
        //      (this, R.layout.activity_collection, gameSampleArray);


        gamesList.setAdapter(adapter);


        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(WishListActivity.this, GameActivity.class);
                temporaryGame = gameArray[position];

                gameTitle = temporaryGame.getTitle();
                intent.putExtra("gameTitle", gameTitle);

                gamePublisher = temporaryGame.getPublisher();
                intent.putExtra("gamePublisher", gamePublisher);

                gameStudio = temporaryGame.getStudio();
                intent.putExtra("gameStudio", gameStudio);

                gameReleaseYear = String.valueOf(temporaryGame.getReleaseYear());
                intent.putExtra("gameReleaseYear", gameReleaseYear);

                gameReleaseDate = temporaryGame.getReleaseDate();
                intent.putExtra("gameReleaseDate", gameReleaseDate);

                gameRating = String.valueOf(temporaryGame.getRating());
                intent.putExtra("gameRating", gameRating);

                startActivity(intent);
            }
        });

        gamesList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {

                Game game = gameArray[position];
                Bundle bundle = new Bundle();
                bundle.putString("gameId", game.getGameId());
                bundle.putString("gameTitle", game.getTitle());
                bundle.putDouble("gameReleaseYear", game.getReleaseYear());
                bundle.putString("gameReleaseDate", game.getReleaseDate());
                bundle.putString("gamePublisher", game.getPublisher());
                bundle.putString("gameStudio", game.getStudio());
                bundle.putDouble("gameRating", game.getRating());

                String gameTitleForSearch = gameArray[position].getTitle();


                gameTitleForSearch = gameTitleForSearch.trim();
                gameTitleForSearch =  gameTitleForSearch.replaceAll("[^a-zA-Z0-9[\\s]]", "");
                gameTitleForSearch = gameTitleForSearch.replace(" ", "+");

                Log.d("search", gameTitleForSearch);

                bundle.putString("gameNameForDialog", gameTitleForSearch);
                WishListLongClickFragment dialogFragment = new WishListLongClickFragment();
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getFragmentManager(), "test");


                return true;


            }

        });


    }

    private class MyArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public MyArrayAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
    }
}