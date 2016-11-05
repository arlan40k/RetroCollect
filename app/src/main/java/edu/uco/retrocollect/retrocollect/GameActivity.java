package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class GameActivity extends Activity {
    private final static String TAG = "GameActivity";
    Bundle bundle;
    private TextView gameTitleTextView, gamePublisherTextView, gameStudioTextView,
            gameReleaseYearTextView, gameReleaseDateTextView, gameRatingTextView;
    private ProgressBar ratingProgressBar;
    private final String dataErrorString = "";
    private double ratingInteger;
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
        ratingProgressBar = (ProgressBar) findViewById(R.id.gameRatingProgressBar);

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
            String year = Double.toString(bundle.getDouble("gameReleaseYear"));
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
                ratingInteger = Integer.parseInt(rating);
//                if (ratingInteger<=100) {
//                    new LoadGameRatingValueTask().execute(ratingInteger);
//                }
            } else {
                gameRatingTextView.setText(dataErrorString);
            }

        }

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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
