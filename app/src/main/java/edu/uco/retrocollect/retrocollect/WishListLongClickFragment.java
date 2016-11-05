package edu.uco.retrocollect.retrocollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class WishListLongClickFragment extends DialogFragment {


    private String [] str = {"Local","Online"};
    private boolean local;
    private boolean online;

    private String lat = "35.638033";
    private String lng  = "-97.485540";

    private String search;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        search = getArguments().getString("gameNameForDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose");
        builder.setSingleChoiceItems(str, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {

                    case 0:
                        local = true;
                        online = false;
                        //Log.d("", "hello");


                        break;

                    case 1:
                        online = true;
                        local = false;

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
