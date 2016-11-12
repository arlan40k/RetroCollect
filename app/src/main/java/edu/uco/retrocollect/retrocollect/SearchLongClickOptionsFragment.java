package edu.uco.retrocollect.retrocollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;


public class SearchLongClickOptionsFragment extends DialogFragment {


    private String[] str = {"Order by Date", "Order by Relevance"};
    private boolean byDate;
    private boolean byRelevance;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chose Action");

        builder.setSingleChoiceItems(str, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case 0:
                         byDate = true;
                         byRelevance = false;
                        //Log.d("", "hello");


                        break;

                    case 1:
                         byDate = false;
                         byRelevance = true;
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
                if (byDate) {
                    ((SearchActivity) getActivity()).byDate = true;
                    ((SearchActivity) getActivity()).byRelevance = false;

                } else if (byRelevance) {
                    ((SearchActivity) getActivity()).byRelevance = true;
                    ((SearchActivity) getActivity()).byDate = false;
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