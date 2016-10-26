package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionActivity extends Activity {

    private Button tempGameButton;
    private ListView gamesList;
    String[] gameSampleArray = {"Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",
            "Game1","Game2","Game3","Game4","Game5",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        gamesList = (ListView) findViewById(R.id.gamesList);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < gameSampleArray.length; ++i) {
            list.add(gameSampleArray[i]);
        }

        final MyArrayAdapter adapter = new MyArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        //ArrayAdapter adapter = new ArrayAdapter<String>
          //      (this, R.layout.activity_collection, gameSampleArray);


        gamesList.setAdapter(adapter);

        Button tempGameButton = (Button) findViewById(R.id.tempGameButton);

        tempGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            /* nic do whatever you gotta do here to get to game activity while testing
            I'm leaving this here until i can populate the collection properly
            will be here until stable passing to game from clicking titles
            */


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
