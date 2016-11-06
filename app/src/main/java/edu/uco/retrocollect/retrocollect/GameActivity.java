package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends Activity {
    private final static String TAG = "GameActivity";
    Bundle bundle;
    private TextView gameTitleTextView, gamePublisherTextView, gameStudioTextView,
            gameReleaseYearTextView, gameReleaseDateTextView, gameRatingTextView;
    private ProgressBar ratingProgressBar;
    private final String dataErrorString = "";
    private int ratingInteger;
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
                //The string is a decimal so i trun it into double
                Double ratingDouble = Double.parseDouble(rating);
                //double has a intValue for conversion to int
                ratingInteger = ratingDouble.intValue();
            //    if (ratingInteger<=100) {
            //        new LoadGameRatingValueTask().execute(ratingInteger);
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
