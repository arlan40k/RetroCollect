package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WishListActivity extends Activity {

    private ListView gamesList;
    private String gameTitle, gamePublisher, gameStudio, gameReleaseYear, gameReleaseDate,
            gameRating, coverHash;
    private Game temporaryGame;



    ArrayList<Game> gameArrayList;

    //Adam Bilby
//Static games will now be dynamic with database games
    Game[] gameArray = {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);


        //Adam Bilby
        //Sql Datbase initialize
        SqlWishListHelper sqlWishListHelper = new SqlWishListHelper(this);
        //Game(String gameTitle, String gameId, double gameReleaseYear, String gameReleaseDate,
        // String gamePublisher, String gameStudio, double gameRating) {

        gameArrayList =  sqlWishListHelper.getAllGames();

        Collections.sort(gameArrayList, new Comparator<Game>() {

            public int compare(Game o1, Game o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }

        });

        //Transform ArrayList into Array
        gameArray = new Game[gameArrayList.size()];
        gameArray = gameArrayList.toArray(gameArray);
        //End of Adam Bilby Block



        gamesList = (ListView) findViewById(R.id.gamesList);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < gameArray.length; ++i) {
            list.add(gameArray[i].getTitle());
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

                coverHash = temporaryGame.getCoverHash();
                intent.putExtra("coverHash", coverHash);

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
               // Log.d("delete2", game.getGameId());
                bundle.putString("gameTitle", game.getTitle());
                bundle.putDouble("gameReleaseYear", game.getReleaseYear());
                bundle.putString("gameReleaseDate", game.getReleaseDate());
                bundle.putString("gamePublisher", game.getPublisher());
                bundle.putString("gameStudio", game.getStudio());
                bundle.putDouble("gameRating", game.getRating());
                bundle.putString("coverHash", game.getCoverHash());

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wish_list_menu, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()



            if(item.getTitle().equals("by A-Z")){
                //Log.d("hello","sup");


                Collections.sort(gameArrayList, new Comparator<Game>() {

                    public int compare(Game o1, Game o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }

                });

                gameArray = new Game[gameArrayList.size()];
                gameArray = gameArrayList.toArray(gameArray);

                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < gameArray.length; ++i) {
                    list.add(gameArray[i].getTitle());
                }

                final WishListActivity.MyArrayAdapter adapter = new WishListActivity.MyArrayAdapter(this,
                        android.R.layout.simple_list_item_1, list);

                gamesList.setAdapter(adapter);

            }
        else {


                Collections.sort(gameArrayList, new Comparator<Game>() {

                    public int compare(Game o1, Game o2) {
                        String o1Rate = Double.toString(o1.getRating());
                        String o2Rate = Double.toString(o2.getRating());
                        return o1Rate.compareTo(o2Rate);
                    }

                });
                Collections.reverse(gameArrayList);

                gameArray = new Game[gameArrayList.size()];
                gameArray = gameArrayList.toArray(gameArray);

                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < gameArray.length; ++i) {
                    list.add(gameArray[i].getTitle());
                }

                final WishListActivity.MyArrayAdapter adapter = new WishListActivity.MyArrayAdapter(this,
                        android.R.layout.simple_list_item_1, list);

                gamesList.setAdapter(adapter);

            }


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