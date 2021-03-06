package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameActivity extends Activity {

    private final static String TAG = "GameActivity";
    Bundle bundle;
    Game myGame;
    private TextView gameTitleTextView, gamePublisherTextView, gameStudioTextView,
            gameReleaseYearTextView, gameReleaseDateTextView, gameRatingTextView, gameValueTextView;
    private ImageView gameCoverBackgroundImageView, gameCoverImageView;
    private ProgressBar ratingProgressBar;
    private RatingBar gameUserRatingBar;
    private Button merchantsButton, gamefaqsButton; /*ratingButton*/;
    private String title, callingActivity;
    private float gameUserRating;
    private final String dataErrorString = "";
    private final String gameValueErrorString = "N/A";
    private int ratingInteger;

    SqlWishListHelper sqlWishListHelper = new SqlWishListHelper(this);
    SqlGameHelper sqlGameHelper = new SqlGameHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SqlWishListHelper sqlWishListHelper = new SqlWishListHelper(this);

        setContentView(R.layout.activity_game);

        gameTitleTextView = (TextView) findViewById(R.id.gameTitleTextView);
        gamePublisherTextView = (TextView) findViewById(R.id.gamePublisherTextView);
        gameStudioTextView = (TextView) findViewById(R.id.gameStudioTextView);
        gameReleaseYearTextView = (TextView) findViewById(R.id.gameReleaseYearTextView);
        gameReleaseDateTextView = (TextView) findViewById(R.id.gameReleaseDateTextView);
        gameRatingTextView = (TextView) findViewById(R.id.gameRatingTextView);
        gameValueTextView = (TextView) findViewById(R.id.gameValueTextView);
        ratingProgressBar = (ProgressBar) findViewById(R.id.gameRatingProgressBar);
        gameUserRatingBar = (RatingBar) findViewById(R.id.gameUserRatingBar);

        merchantsButton = (Button) findViewById(R.id.merchantsButton);
        merchantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, LocalMerchantActivity.class);
                startActivity(intent);
            }
        });
        gamefaqsButton = (Button) findViewById(R.id.gamefaqsButton);
       // ratingButton = (Button) findViewById(R.id.ratingSaveButton);

        //gameCoverImageView = (ImageView) findViewById(R.id.gameCoverImageView);
        gameCoverBackgroundImageView = (ImageView) findViewById(R.id.gameCoverBackgroundImageView);

        bundle = getIntent().getExtras();

        callingActivity = bundle.getString("callingActivity");

        if (callingActivity == null){
            callingActivity = "N/A";
        }
        if (bundle != null) {

            title = bundle.getString("gameTitle");
           // myGame = bundle.getParcelable("temporaryGame");
            if(callingActivity.equals("WishList")) {
                myGame = sqlWishListHelper.getGame(title);
            }
            else if(callingActivity.equals("Collection")){
                myGame = sqlGameHelper.getGame(title);
            }
            else{
                myGame = bundle.getParcelable("temporaryGame");
            }

            if (myGame != null){
                gameUserRating = myGame.getGameUserRating();
                Log.d("gameUserRating", Float.toString(gameUserRating));
                gameUserRatingBar.setRating(gameUserRating);
                gameUserRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                       // myGame.setGameUserRating(rating);
                       // sqlWishListHelper.updateGame(myGame);
                       // Toast.makeText(getApplicationContext(), "HEY", Toast.LENGTH_SHORT).show();
                       // Log.d("intoWishDB", Float.toString(rating));

                        if(myGame !=  null){
                            myGame.setGameUserRating(rating);

                            if(callingActivity.equals("WishList")) {
                                sqlWishListHelper.updateGame(myGame);
                            }
                            else if(callingActivity.equals("Collection")){
                                sqlGameHelper.updateGame(myGame);
                                Log.d("intoC", "LORD!!! IF YOURE LISTENING! HELP!!!!!!");
                            }

                            //sqlWishListHelper.updateGame(myGame);
                            Log.d("intoWi", "did we make it this far?");
                            //sqlWishListHelper.addGame(myGame);

                        }


                    }
                });


            }

            if (title != null) {
                gameTitleTextView.setText(title);
                gamefaqsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://www.gamefaqs.com/search?game="+ title;
                        Uri url2 = Uri.parse(url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, url2);
                        startActivity(browserIntent);
                    }
                });
            } else {
                gameTitleTextView.setText(dataErrorString);
            }
            String value =  bundle.getString("gameValue");
            if (value != null) {
                gameValueTextView.setText("$"+ value);
            } else {
                gameValueTextView.setText("$" + gameValueErrorString);
            }
            String publisher = bundle.getString("gamePublisher");
            if (publisher != null) {
                // gamePublisherTextView.setText(publisher);
                new IgdbApiTask().execute(publisher);
            } else {
                gamePublisherTextView.setText(dataErrorString);
            }
            String studio = bundle.getString("gameStudio");
            if (studio != null) {
                //gameStudioTextView.setText(studio);
                new IgdbApiTaskStudio().execute(studio);
            } else {
                gameStudioTextView.setText(dataErrorString);
            }

//            String year = Double.toString(bundle.getDouble("gameReleaseYear"));
//            if (year != null) {
//                gameReleaseYearTextView.setText(year);
//            } else {
//                gameReleaseYearTextView.setText(dataErrorString);
//            }
            String date = bundle.getString("gameReleaseDate");
            if (date != null) {
                gameReleaseDateTextView.setText(date);
                gameReleaseYearTextView.setText(date.substring(0,4));
            } else {
                gameReleaseDateTextView.setText(dataErrorString);
                gameReleaseYearTextView.setText(dataErrorString);
            }

            String rating = bundle.getString("gameRating");
            if (rating != null) {
                rating = rating.substring(0,4);

                gameRatingTextView.setText(rating + "/100");

                //The string is a decimal so turn it into double
                Double ratingDouble = Double.parseDouble(rating);

                //double has a intValue for conversion to int
                ratingInteger = ratingDouble.intValue();

                if (ratingInteger<=100) {
                    new LoadGameRatingValueTask().execute(ratingInteger);
                }

            } else {
                gameRatingTextView.setText(dataErrorString);
            }
            //Added by Nicholas Clemmons
            //Get coverHash to identify image.
            String cover = bundle.getString("coverHash");
            if (cover != null) {
                // Picasso used for image downloading
                //See for details: "http://square.github.io/picasso/"

                // "t_cover_big" to get 227 x 320 cover image.
                // "_2x" to get retina (DPR 2.0) sizes.
                // See for details: "https://market.mashape.com/igdbcom/internet-game-database/
                // overview#wiki-images"

                /*Picasso.with(getApplicationContext()).load("https://res.cloudinary.com/igdb/imag" +
                        "e/upload/t_cover_big_2x/"
                        + cover + ".jpg").into(gameCoverImageView);*/

                Picasso.with(getApplicationContext()).load("https://images.igdb.com/igdb/image/" +
                        "upload/t_cover_big_2x/"
                        + cover + ".jpg").into(gameCoverBackgroundImageView);

                //Log.d(TAG, cover);

            }

        }

        //put button here
        /*
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating1 = gameUserRatingBar.getRating();
                /*
                String id = " ";
                if(bundle!=null){
                    id = bundle.getString("gameId")+ " ";
                }

                String title;
                title =  bundle.getString("gameTitle");
                //Log.d("intoWish", id);

                

                //String stringRat = Float.toString(myGame.getGameUserRating()) + " ";
                //Log.d("intoWis", stringRat);

                if(myGame !=  null){
                    myGame.setGameUserRating(rating1);

                    if(callingActivity.equals("WishList")) {
                        sqlWishListHelper.updateGame(myGame);
                    }
                    else if(callingActivity.equals("Collection")){
                        sqlGameHelper.updateGame(myGame);
                        Log.d("intoC", "LORD!!! IF YOURE LISTENING! HELP!!!!!!");
                    }

                    //sqlWishListHelper.updateGame(myGame);
                    Log.d("intoWi", "did we make it this far?");
                    //sqlWishListHelper.addGame(myGame);

                }

                Log.d("intoWishDB", Float.toString(rating1));
            }
        });
        */
    }
    class LoadGameRatingValueTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            ratingProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        // parameters are from AsyncTask.execute( ) method call
        protected Void doInBackground(Integer... resId) {
            int tmp = resId[0];
            // simulating long-running operation
            for (int i = 1; i < tmp; i++) {
                sleep(); // 0.5 sec
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ratingProgressBar.setProgress(values[0]);
        }


        private void sleep() {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private class IgdbApiTask extends AsyncTask <Object, Void, HttpResponse<JsonNode>> {

        //Network Activities must be done in  doInBackground
        @Override
        protected HttpResponse<JsonNode> doInBackground(Object[] objects) {
            HttpResponse<JsonNode> response = null;
            try {
                //Get my string from the objects
                String game_name = (String) objects[0];
                Log.d("nametime3", "sup");

                //API Request
                String searchString = JsonCompanyParser.parseSearchString(game_name);
                //Changed order to relevance rather than date released -HASEEB

                response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/" +
                        "companies/"+ searchString + "?fields=name&limit=10&offset=0")
                        .header("X-Mashape-Key", "4KjzzTanigmshoC1cuOPyXU16sUvp1xp5m7jsnV3lAlo5HH0wK")
                        .header("Accept", "application/json")
                        .asJson();

                // These code snippets use an open-source library. http://unirest.io/java



                return response;
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (response == null ) {
                Toast.makeText(GameActivity.this,
                        "Invalid data. Possibly a wrong query",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {

                ArrayList<String> gameArrayList = JsonCompanyParser.getGameList(response);

                /*
                String hash;
                if(gameArrayList.size() > 0)
                {
                    hash = gameArrayList.get(0).getCoverHash() + " ";
                }
                else
                {
                    hash = "";
                }
                String[] rl = new String[gameArrayList.size()];
                String[] h1 = new String[gameArrayList.size()];
                */
                String pubName = " ";


                for(int i = 0; i < gameArrayList.size(); i++) {
                    if(i>0){
                        pubName = pubName +", \n"+ gameArrayList.get(i);
                    }
                    else{
                        pubName = "\n"+ gameArrayList.get(i);
                    }
                }

                gamePublisherTextView.setText(pubName);

               // gameStudioTextView.setText(pubName);

                if(pubName.equals(" ")){
                    Log.d("Nametime2", "heyo");
                }



            }


        }



    }

    private class IgdbApiTaskStudio extends AsyncTask <Object, Void, HttpResponse<JsonNode>> {

        //Network Activities must be done in  doInBackground
        @Override
        protected HttpResponse<JsonNode> doInBackground(Object[] objects) {
            HttpResponse<JsonNode> response = null;
            try {
                //Get my string from the objects
                String game_name = (String) objects[0];
                Log.d("nametime3", "sup");

                //API Request
                String searchString = JsonCompanyParser.parseSearchString(game_name);
                //Changed order to relevance rather than date released -HASEEB

                response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/" +
                        "companies/"+ searchString + "?fields=name&limit=10&offset=0")
                        .header("X-Mashape-Key", "4KjzzTanigmshoC1cuOPyXU16sUvp1xp5m7jsnV3lAlo5HH0wK")
                        .header("Accept", "application/json")
                        .asJson();

                // These code snippets use an open-source library. http://unirest.io/java



                return response;
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (response == null ) {
                Toast.makeText(GameActivity.this,
                        "Invalid data. Possibly a wrong query",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {

                ArrayList<String> gameArrayList = JsonCompanyParser.getGameList(response);

                /*
                String hash;
                if(gameArrayList.size() > 0)
                {
                    hash = gameArrayList.get(0).getCoverHash() + " ";
                }
                else
                {
                    hash = "";
                }
                String[] rl = new String[gameArrayList.size()];
                String[] h1 = new String[gameArrayList.size()];
                */
                String pubName = " ";


                for(int i = 0; i < gameArrayList.size(); i++) {
                    if(i>0){
                        pubName = pubName + ", \n" + gameArrayList.get(i);
                    }
                    else{
                        pubName = "\n"+gameArrayList.get(i);
                    }
                }

               // gamePublisherTextView.setText(pubName);

                gameStudioTextView.setText(pubName);

                if(pubName.equals(" ")){
                    Log.d("Nametime2", "heyo");
                }



            }


        }



    }
}
