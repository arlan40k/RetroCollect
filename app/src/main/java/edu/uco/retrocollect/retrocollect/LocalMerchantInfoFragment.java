package edu.uco.retrocollect.retrocollect;

import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class LocalMerchantInfoFragment extends DialogFragment  {
    private ListView listview;
    ArrayAdapter<String> adapter;
    private PendingIntent intent;
    private ArrayList<String> data;
    private String localInfo[];
    private ArrayList<String> test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this frag;
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

                // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deptList[i].getUrl()));
                // intent = PendingIntent.getActivity(getActivity(), 0, browserIntent, PendingIntent.FLAG_UPDATE_CURRENT);
               if(((TextView) view).getText().toString().equals("Website"))
               {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(i)));
                    startActivity(browserIntent);
               }else if(((TextView) view).getText().toString().contains("Phone:")){
                   Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get(i)));
                   startActivity(dialIntent);
/*                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deptList[i].getUrl()));
                   Notification.Builder notificaitonBuilder = new Notification.Builder(
                           getActivity().getApplication())
                           .setSmallIcon(android.R.drawable.stat_notify_more)
                           .setAutoCancel(true)
                           .setContentTitle("Notification from: " + localInfo[i])
                           .setContentText("You've been notified ")
                           .setContentIntent(intent);

                   NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                   nm.notify(0, notificaitonBuilder.build());*/
               }
            }
        });

        return v;
    }

}
