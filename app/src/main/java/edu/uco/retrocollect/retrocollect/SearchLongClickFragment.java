package edu.uco.retrocollect.retrocollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;


public class SearchLongClickFragment extends DialogFragment {


    private String [] str = {"Add Game to Wishlist","Add Game to Collection", "Find Local Game Stores"};
    private boolean wishlist;
    private boolean collection;
    private boolean map;


    private Game addGame;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();

       String title = getArguments().getString("gameTitle");
       String id = getArguments().getString("gameId");

       double releaseYear = getArguments().getDouble("gameReleaseYear");
        String releaseDate = getArguments().getString("gameReleaseDate");
        String publisher = getArguments().getString("gamePublisher");
        String studio = getArguments().getString("gameStudio");
        Double gameRating = getArguments().getDouble("gameRating");
        String coverHash = getArguments().getString("coverHash");//_HASEEB
        Log.d("searchHash", coverHash+ " ");
        addGame = new Game(title, id, releaseYear, releaseDate, publisher, studio, gameRating, coverHash);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chose Action");

        builder.setSingleChoiceItems(str, 3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {

                    case 0:
                        wishlist = true;
                        collection = false;
                        map = false;
                        //Log.d("", "hello");


                        break;

                    case 1:
                        collection = true;
                        wishlist = false;
                        map = false;
                        break;

                    case 2:
                        collection = false;
                        wishlist = false;
                        map = true;
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
                if(wishlist){
                    SqlWishListHelper sqlWishListHelper = new SqlWishListHelper(getActivity());

                    sqlWishListHelper.addGame(addGame);

                }
                else if(collection){
                    SqlGameHelper sqlGameHelper = new SqlGameHelper(getActivity());

                    sqlGameHelper.addGame(addGame);
                }
                else if(map)
                {
                    Intent i = new Intent(getActivity(), LocalMerchantActivity.class);
                    startActivity(i);

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