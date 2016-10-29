package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListActivity extends Activity {

    /*    private Button tempGameButton;*/
    private ListView gamesList;
    private String gameTitle, gamePublisher, gameStudio, gameReleaseYear, gameReleaseDate, gameRating;
    private Game temporaryGame;
    String[] gameSampleArray = {"Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",};

    Game[] gameArray = {new Game("Cool game", 1992.0, "April 2"),
            new Game("Neat Game", 1997.0, "July 4", "YouBeSoft", "YourMomsStudio"),
            new Game("bad Game", 2007.0, "June 4"),
            new Game("good Game", 1862.0, "July 8"),
            new Game("ok Game", 9201.0, "August 14"),
            new Game("awesome Game", 2020.0, "July 4"),
            new Game("Neat Game", 1997.0, "July 4"),
            new Game("bad Game", 2007.0, "June 4"),
            new Game("good Game", 1862.0, "July 8"),
            new Game("ok Game", 9201.0, "August 14"),
            new Game("awesome Game", 2020.0, "July 4"),
            new Game("Neat Game", 1997.0, "July 4"),
            new Game("bad Game", 2007.0, "June 4"),
            new Game("good Game", 1862.0, "July 8"),
            new Game("ok Game", 9201.0, "August 14"),
            new Game("awesome Game", 2020.0, "July 4"),};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        gamesList = (ListView) findViewById(R.id.gamesList);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < gameArray.length; ++i) {
            list.add(gameArray[i].getTitle() + "     " + gameArray[i].getReleaseYear());
        }

        final MyArrayAdapter adapter = new MyArrayAdapter(this,
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

/*        Button tempGameButton = (Button) findViewById(R.id.tempGameButton);

        tempGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            *//* nic do whatever you gotta do here to get to game activity while testing
            I'm leaving this here until i can populate the collection properly
            will be here until stable passing to game from clicking titles
            *//*


            }
        });*/



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
