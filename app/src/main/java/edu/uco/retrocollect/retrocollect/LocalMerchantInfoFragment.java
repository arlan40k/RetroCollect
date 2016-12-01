package edu.uco.retrocollect.retrocollect;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class LocalMerchantInfoFragment extends DialogFragment  {
    private ListView listview;
    ArrayAdapter<String> adapter;
    private ArrayList<String> data;
    private String localInfo[];
    private ArrayList<String> test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this frag;
        super.onCreateView(inflater, container, savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);

        data = getArguments().getStringArrayList("data");
        localInfo = new String[data.size()];
        for(int i = 0; i < data.size(); i++) {
            switch (i)
            {
                case 2: localInfo[i] = "Phone: " + data.get(i);
                    break;
                case 3: localInfo[i] = "Website";
                    break;
                default:
                    localInfo[i] = data.get(i);
            }
        }

        View v = inflater.inflate(R.layout.fragment_local_merchant_info, null);
        listview = (ListView) v.findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, localInfo);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               if(((TextView) view).getText().toString().equals("Website"))
               {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(i)));
                    startActivity(browserIntent);
               }else if(((TextView) view).getText().toString().contains("Phone:")){
                   Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get(i)));
                   startActivity(dialIntent);
               }
            }
        });

        return v;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Setup the layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View root = inflater.inflate(R.layout.fragment_local_merchant_info, null);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Customizing the dialog features

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
