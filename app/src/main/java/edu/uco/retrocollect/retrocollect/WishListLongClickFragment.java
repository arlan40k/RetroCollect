package edu.uco.retrocollect.retrocollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WishListLongClickFragment extends DialogFragment {


    private String [] str = {"Local Merchant","Online Merchant", "Move to Collection",
            "Remove from Wish List", "Ask friend to buy"};
    private boolean local, collection, remove;
    private boolean online, share;

    private String lat = "35.638033";
    private String lng  = "-97.485540";

    private String search;

    SqlWishListHelper sqlWishListHelper;

    private boolean gridBool = true;


    private String gameId;
    private String  gameTitle,  gameReleaseDate, gamePublisher,
            gameStudio = "", coverHash, gameValue;
    private Double gameReleaseYear, gameRating = 0.0;

    private Game selectedGame;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sqlWishListHelper = new SqlWishListHelper(getActivity());

        search = getArguments().getString("gameNameForDialog");

        gameId = getArguments().getString("gameId");
        gameTitle = getArguments().getString("gameTitle");
        gameReleaseYear = getArguments().getDouble("gameReleaseYear");
        gameReleaseDate = getArguments().getString("gameReleaseDate");
        gamePublisher = getArguments().getString("gamePublisher");
        gameStudio = getArguments().getString("gameStudio");
        gameRating = getArguments().getDouble("gameRating");
        coverHash = getArguments().getString("coverHash");
        gameValue = getArguments().getString("gameValue");
        gridBool = getArguments().getBoolean("gridBool");



        selectedGame = new Game(gameTitle,gameId,gameReleaseYear,
                gameReleaseDate,gamePublisher,gameStudio,gameRating, coverHash);

        selectedGame.setGameValue(gameValue);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose");
        builder.setSingleChoiceItems(str, 5, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {

                    case 0:
                        local = true;
                        online = false;
                        collection = false;
                        remove = false;
                        //Log.d("", "hello");


                        break;

                    case 1:
                        online = true;
                        local = false;
                        collection = false;
                        remove = false;
                        share = false;

                        break;
                    case 2:
                        collection = true;
                        local = false;
                        remove = false;
                        online = false;
                        share = false;

                        break;

                    case 3:
                        remove = true;
                        collection = false;
                        local = false;
                        online = false;
                        share = false;
                        break;

                    case 4:
                        remove = false;
                        collection = false;
                        local = false;
                        online = false;
                        share =true;
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
                    Intent localMerchantIntent = new Intent(getActivity(),
                            LocalMerchantActivity.class);
                    localMerchantIntent.putExtra("LAT", lat);
                    localMerchantIntent.putExtra("LNG", lng);
                    startActivity(localMerchantIntent);
                    Log.d("local", "hello");
                }
                else if(online){
                    String url =
                            "https://www.amazon.com/s/ref=nb_sb_noss?url=search" +
                                    "-alias%3Dvideogames&field-keywords="+ search
                                    +"&rh=n%3A468642%2Ck%3A"+ search;
                    Uri url2 = Uri.parse(url);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, url2);
                    startActivity(browserIntent);

                    Log.d("urlIsHere", url);

                }
                else if(collection){
                    SqlGameHelper sqlGameHelper = new SqlGameHelper(getActivity());
                    sqlGameHelper.addGame(selectedGame);

                    sqlWishListHelper.deleteGame(selectedGame);

                    Toast.makeText(getActivity(), "Game moved to Collection!", Toast.LENGTH_SHORT).show();



                    Intent refreshWishListIntent = new Intent(getActivity(),
                            WishListActivity.class);
                    getActivity().finish();
                    refreshWishListIntent.putExtra("gridBool", gridBool);
                    startActivity(refreshWishListIntent);



                }
                else if(remove){
                    sqlWishListHelper.deleteGame(selectedGame);

                    Intent refreshWishListIntent = new Intent(getActivity(),
                            WishListActivity.class);
                    getActivity().finish();
                    startActivity(refreshWishListIntent);
                }
                else if(share){
                    String msg = "You wanna be a pal and buy me  " + selectedGame.getTitle() + "?";
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
