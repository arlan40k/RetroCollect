package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CollectionActivity extends Activity {

    private GridView gamesList;
    //private ImageView testImage;
    private String gameTitle, gamePublisher, gameStudio, gameReleaseYear, gameReleaseDate,
            gameRating, gameCoverHash;
    private Game temporaryGame;

    ArrayList<Game> gameArrayList;

    private boolean gridBool = true;

    Bundle bundle;




    //Adam Bilby
//Static games will now be dynamic with database games
    Game[] gameArray = {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        //Adam Bilby
        //Ticker Solution
        final TickerView tickerView = (TickerView) findViewById(R.id.tickerView);
        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());

        //Adam Bilby
        //Sql Datbase initialize
        SqlGameHelper sqlGameHelper = new SqlGameHelper(this);
        gameArrayList =  sqlGameHelper.getAllGames();

        Collections.sort(gameArrayList, new Comparator<Game>() {

            public int compare(Game o1, Game o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }

        });

        //Transform ArrayList into Array
        gameArray = new Game[gameArrayList.size()];
        gameArray = gameArrayList.toArray(gameArray);
        //End of Adam Bilby Block

        //Get Ticker Price
        String totalPrice;
        Double actualPrice  = 0.00;
        for(int i = 0; i < gameArrayList.size(); i++)
        {
            if(!gameArrayList.get(i).getGameValue().equals("N/A")) {
                actualPrice += Double.parseDouble(gameArrayList.get(i).getGameValue());
            }
        }


        //totalPrice = Double.toString(actualPrice);
        if(!actualPrice.equals("N/A")) {
            totalPrice = String.format("%.2f", actualPrice);
            tickerView.setText("Collection Value: $" + totalPrice);
        }else{
            tickerView.setText("Collection Value: $" + actualPrice);
        }
        gamesList = (GridView) findViewById(R.id.gamesList);



        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < gameArray.length; ++i) {
            list.add(gameArray[i].getTitle());
        }

        //final CollectionAdapter adapter = new CollectionAdapter(this,
          //      android.R.layout.simple_list_item_1, list);


        bundle = getIntent().getExtras();
        if(bundle!=null) {
            gridBool = bundle.getBoolean("gridBool");
        }

        if (!gridBool){
            gamesList.setNumColumns(1);
            final SecondCollectionAdapter adapter2 = new SecondCollectionAdapter(
                    getApplicationContext(), gameArrayList);
            gamesList.setAdapter(adapter2);
        }
        else{
            gamesList.setNumColumns(3);
            final CollectionAdapter adapter = new CollectionAdapter(getApplicationContext(), gameArrayList);
            gamesList.setAdapter(adapter);
        }


        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(CollectionActivity.this, GameActivity.class);

                temporaryGame = gameArray[position];
                intent.putExtra("temporaryGame", temporaryGame);

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

                gameCoverHash = temporaryGame.getCoverHash();
                intent.putExtra("coverHash", gameCoverHash);

                String sup = gameCoverHash +" ";
                Log.d("Cover:", sup);

                //Adam Bilby
                String gameValue = String.valueOf(temporaryGame.getGameValue());

                intent.putExtra("gameValue", gameValue);

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
                bundle.putString("coverHash", game.getCoverHash());
                bundle.putString("gameValue", game.getGameValue());
                bundle.putBoolean("gridBool", gridBool);


                String gameTitleForSearch = gameArray[position].getTitle();


                gameTitleForSearch = gameTitleForSearch.trim();
                gameTitleForSearch =  gameTitleForSearch.replaceAll("[^a-zA-Z0-9[\\s]]", "");
                gameTitleForSearch = gameTitleForSearch.replace(" ", "+");

                Log.d("search", gameTitleForSearch);

                bundle.putString("gameNameForDialog", gameTitleForSearch);
                CollectionLongClickFragment dialogFragment = new CollectionLongClickFragment();
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
            Log.d("hello","sup");
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

            //final CollectionActivity.MyArrayAdapter adapter = new CollectionActivity.MyArrayAdapter(this,
              //      android.R.layout.simple_list_item_1, list);

            if (!gridBool){
                gamesList.setNumColumns(1);
                final SecondCollectionAdapter adapter2 = new SecondCollectionAdapter(
                        getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter2);
            }
            else{
                gamesList.setNumColumns(3);
                final CollectionAdapter adapter = new CollectionAdapter(getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter);
            }

        }
        else if(item.getTitle().equals("by rating")){
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

          //  final CollectionActivity.MyArrayAdapter adapter = new CollectionActivity.MyArrayAdapter(this,
            //        android.R.layout.simple_list_item_1, list);


            if (!gridBool){
                gamesList.setNumColumns(1);
                final SecondCollectionAdapter adapter2 = new SecondCollectionAdapter(
                        getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter2);
            }
            else{
                gamesList.setNumColumns(3);
                final CollectionAdapter adapter = new CollectionAdapter(getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter);
            }


        }
        else if(item.getTitle().equals("by Value"))
        {
            Collections.sort(gameArrayList, new Comparator<Game>() {

                public int compare(Game o1, Game o2) {
                    Double o1Rate = Double.parseDouble(o1.getGameValue());
                    Double o2Rate = Double.parseDouble(o2.getGameValue());
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

            //  final CollectionActivity.MyArrayAdapter adapter = new CollectionActivity.MyArrayAdapter(this,
            //        android.R.layout.simple_list_item_1, list);


            if (!gridBool){
                gamesList.setNumColumns(1);
                final SecondCollectionAdapter adapter2 = new SecondCollectionAdapter(
                        getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter2);
            }
            else{
                gamesList.setNumColumns(3);
                final CollectionAdapter adapter = new CollectionAdapter(getApplicationContext(), gameArrayList);
                gamesList.setAdapter(adapter);
            }
        }
        else if(item.getTitle().equals("List View")){
            gamesList.setNumColumns(1);
            final SecondCollectionAdapter adapter2 = new SecondCollectionAdapter(
                    getApplicationContext(), gameArrayList);
            gamesList.setAdapter(adapter2);

            gridBool = false;

        }
        else if(item.getTitle().equals("Grid View")){
            gamesList.setNumColumns(3);
            final CollectionAdapter adapter = new CollectionAdapter(getApplicationContext(), gameArrayList);
            gamesList.setAdapter(adapter);

            gridBool = true;

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


    private class CollectionAdapter extends BaseAdapter {

        private Context mContext;
        //private Game[] gamesArray;
        private List<Game> gamesArray;

        public CollectionAdapter(Context context, List<Game> gamesArray){
            this.mContext = context;
            this.gamesArray = gamesArray;
        }

        @Override
        public int getCount(){
            return gamesArray.size();
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // 1
            final Game game = gameArray[position];

            // 2
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.linearlayout_game, null);
            }

            // 3
            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_game_name);
            //Adam Bilby
            final TextView priceTextView = (TextView)convertView.findViewById(R.id.textview_game_value);
            // 4
            Picasso.with(getApplicationContext()).load("https://res.cloudinary.com/igdb/image/upload/t_cover_small_2x/"
                    +  game.getCoverHash() +  ".jpg").into(imageView);

            nameTextView.setText(game.getTitle());

            if(game.getGameValue() != null)
            {
                priceTextView.setText(game.getGameValue());
            }
            else
            {
                priceTextView.setText("N/A");
            }

            return convertView;
        }

    }

    private class SecondCollectionAdapter extends BaseAdapter {

        private Context mContext;
        //private Game[] gamesArray;
        private List<Game> gamesArray;

        public SecondCollectionAdapter(Context context, List<Game> gamesArray){
            this.mContext = context;
            this.gamesArray = gamesArray;
        }

        @Override
        public int getCount(){
            return gamesArray.size();
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // 1
            final Game game = gameArray[position];

            // 2
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.linearlayout_game2, null);
            }

            // 3
            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_game_name);
            //Adam Bilby
            final TextView priceTextView = (TextView)convertView.findViewById(R.id.textview_game_value);
            // 4
            Picasso.with(getApplicationContext()).load("https://res.cloudinary.com/igdb/image/upload/t_cover_small_2x/"
                    +  game.getCoverHash() +  ".jpg").into(imageView);

            nameTextView.setText(game.getTitle());

            if(game.getGameValue() != null)
            {
                priceTextView.setText("$" + game.getGameValue());
            }
            else
            {
                priceTextView.setText("$N/A");
            }

            return convertView;
        }

    }
}
