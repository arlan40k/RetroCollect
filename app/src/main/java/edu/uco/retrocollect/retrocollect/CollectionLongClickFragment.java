package edu.uco.retrocollect.retrocollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;


public class CollectionLongClickFragment extends DialogFragment {


    private String [] str = {"Remove from Collection","Walkthroughs", "share"};
    private boolean local;
    private boolean online;
    private boolean share;

    private String lat = "35.638033";
    private String lng  = "-97.485540";

    private String search;
  //  private String gameIdToRemove;
    SqlGameHelper sqlGameHelper;

    private boolean gridBool = true;

    private String gameId;

    private String  gameTitle,  gameReleaseDate, gamePublisher,
            gameStudio = "", gameCoverHash, gameValue;
    private Double gameReleaseYear, gameRating = 0.0;

    private Game selectedGame;

    /*
                bundle.putString("gameId", game.getGameId());
                bundle.putString("gameTitle", game.getTitle());
                bundle.putDouble("gameReleaseYear", game.getReleaseYear());
                bundle.putString("gameReleaseDate", game.getReleaseDate());
                bundle.putString("gamePublisher", game.getPublisher());
                bundle.putString("gameStudio", game.getStudio());
                bundle.putDouble("gameRating", game.getRating());
*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sqlGameHelper = new SqlGameHelper(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();

        search = getArguments().getString("gameNameForDialog");

        gameId = getArguments().getString("gameId");
        gameTitle = getArguments().getString("gameTitle");
        gameReleaseYear = getArguments().getDouble("gameReleaseYear");
        gameReleaseDate = getArguments().getString("gameReleaseDate");
        gamePublisher = getArguments().getString("gamePublisher");
        gameStudio = getArguments().getString("gameStudio");
        gameRating = getArguments().getDouble("gameRating");
        gameCoverHash = getArguments().getString("coverHash");
        gameValue = getArguments().getString("gameValue");
        gridBool = getArguments().getBoolean("gridBool");


        selectedGame = new Game(gameTitle,gameId,gameReleaseYear,
                gameReleaseDate,gamePublisher,gameStudio,gameRating, gameCoverHash);



        //gameIdToRemove = getArguments().getString("gameIdToRemove");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Option");
        //builder.setView(inflater.inflate(R.layout.fragment_collection_long_click, null));
        builder.setSingleChoiceItems(str, 3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {

                    case 0:
                        local = true;
                        online = false;
                        share = false;
                        //Log.d("", "hello");


                        break;

                    case 1:
                        online = true;
                        local = false;
                        share = false;

                        break;

                    case 2:
                        online = false;
                        local = false;
                        share = true;

                        break;

                    default:

                        break;

                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                if(local){

                    sqlGameHelper.deleteGame(selectedGame);

                    //sqlGameHelper.deleteGameById(gameId);
                    //String id1 = gameId;
                    //Log.d("ig", "hello");


                    Intent refreshCollectionIntent = new Intent(getActivity(),
                            CollectionActivity.class);
                    getActivity().finish();
                    refreshCollectionIntent.putExtra("gridBool", gridBool);
                    startActivity(refreshCollectionIntent);

                }
                else if(online){
                    String url = "http://www.gamefaqs.com/search?game="+ search;
                    Uri url2 = Uri.parse(url);
                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, url2);
                    startActivity(browserIntent);

                    Log.d("urlIsHere", url);

                }
                else if (share){
                    String msg = "Check out this game: " + selectedGame.getTitle();
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Check it out");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }

            }
        })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();


    }

}
