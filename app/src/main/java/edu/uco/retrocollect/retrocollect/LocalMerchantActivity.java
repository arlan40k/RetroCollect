package edu.uco.retrocollect.retrocollect;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static edu.uco.retrocollect.retrocollect.R.id.map;

public class LocalMerchantActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static ArrayList<Marker> markers = new ArrayList<>();
    private HashMap<String, String> placeIDs = new HashMap<>();
    private HashMap<Marker, ArrayList<String>> storeInfo = new HashMap<>();

    public static boolean isInit = true;
    private LatLng loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lat = getIntent().getStringExtra("LAT");
        String lng = getIntent().getStringExtra("LNG");

        markers.clear();
        placeIDs.clear();
        storeInfo.clear();
        isInit = true;
        if(!lat.equals("") && !lng.equals(""))
            loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        else
            loc = new LatLng(35.638033, -97.485540);

        setContentView(R.layout.activity_local_merchant);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        // mMap.addMarker(new MarkerOptions().position(loc).title("Marker in Edmond"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        new HttpGet().execute();
        Thread t1 = new Waiter();
        t1.start();
        //comgooglemaps://?q=Pizza&center=37.759748,-122.427135

        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=YOUR_API_KEY

        CameraPosition camera = new CameraPosition.Builder()
                .target(loc).zoom(12).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                return null;
            }
        });
/*        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                for(Marker marker : markers) {
                    if(Math.abs(marker.getPosition().latitude - latLng.latitude) < 0.005 && Math.abs(marker.getPosition().longitude - latLng.longitude) < 0.005) {

                        Bundle bundle = new Bundle();
                        ArrayList<String> tmp = storeInfo.get(marker);
                        bundle.putStringArrayList("data", storeInfo.get(marker));
                        LocalMerchantInfoFragment frag = new LocalMerchantInfoFragment();
                        frag.setArguments(bundle);
                        frag.show(getFragmentManager(), "test");

                        break;
                    }
                }
            }
        });*/

    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = new Bundle();
        ArrayList<String> tmp = storeInfo.get(marker);
        bundle.putStringArrayList("data", storeInfo.get(marker));
        LocalMerchantInfoFragment frag = new LocalMerchantInfoFragment();
        frag.setArguments(bundle);
        frag.show(getFragmentManager(), "test");
        return false;
    }



    private class HttpGet extends AsyncTask<String, Void, ArrayList<String>> {

        //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY
        final String LocalPlace_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        final String SpecificPlace_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String hash = "";
            String tmpId = "";
            if(strings.length > 0){
                hash = strings[0];
                for(String s : placeIDs.keySet())
                    if(s.equals(hash))
                        tmpId = placeIDs.get(s);
            }


            InputStream input = null;
            HttpURLConnection httpUrlConnection = null;
            ArrayList<String> results = null;

            try {
                //If marker is selected
                if (!isInit) {

                    //ChIJk3qurLwfsocRDjVezvHTnbo
                    Uri builtUri = Uri.parse(SpecificPlace_URL).buildUpon()
                            .appendQueryParameter("placeid", tmpId)
                            .appendQueryParameter("key", "AIzaSyDl1oGJ5P5LR6nEnhLkYHOhEIqUaa8J1fU")
                            .build();
                    URL url = new URL(builtUri.toString());
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    input = new BufferedInputStream(httpUrlConnection.getInputStream());
                    String data = readStream(input);
                    results = JsonMerchantDataParser.getData(data);
                } else {
                    Uri builtUri = Uri.parse(LocalPlace_URL).buildUpon()
                            //.appendQueryParameter("location", Double.toString(loc.latitude) + "," + Double.toString(loc.longitude))
                            .appendQueryParameter("location", Double.toString(loc.latitude) + "," + Double.toString(loc.longitude))
                            .appendQueryParameter("radius", "8046.72")
                            .appendQueryParameter("type", "store")
                            .appendQueryParameter("keyword", "GameStop")
                            .appendQueryParameter("key", "AIzaSyDl1oGJ5P5LR6nEnhLkYHOhEIqUaa8J1fU")
                            .build();
                    Log.d("URL: ", builtUri.toString());
                    URL url = new URL(builtUri.toString());
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    input = new BufferedInputStream(httpUrlConnection.getInputStream());
                    String data = readStream(input);
                    results = JsonMerchantDataParser.getData(data);
                }
            } catch (Exception ex) {

            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
                if (input != null) {
                    try {
                        input.close();
                    } catch (final IOException ex) {
                    }
                }
            }

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<String> results) {
            if (results == null || results.size() == 0) {
                Toast.makeText(LocalMerchantActivity.this, "Search Failed", Toast.LENGTH_SHORT).show();
                return;
            }

             if (!isInit) {
                 Marker tmpMarker;
                 String name = results.remove(0);
                 String phone = results.remove(0);
                 String web = results.remove(0);
                 String status = results.remove(0);
                 String placeId = results.remove(0);
                 String weekdays = results.remove(0);

                 if (status.equals("true"))
                     status = "Status: open";
                 else
                     status = "Status: closed";


                 ArrayList<String> tmp = new ArrayList<>();
                 tmp.add(name);
                 tmp.add(status);
                 tmp.add(phone);
                 tmp.add(web);

                 tmp.add(weekdays);

                 for(String s : placeIDs.keySet()){
                     if(placeId.equals(placeIDs.get(s))){
                         for (Marker m : markers)
                             if(String.valueOf(m.hashCode()).equals(s)){
                                 storeInfo.put(m, tmp);
                             }

                     }
                 }

            } else {
                while (results.size() > 0) {
                    String name = results.remove(0);
                    double lat = Double.valueOf(results.remove(0));
                    double lon = Double.valueOf(results.remove(0));
                    String id = results.remove(0);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name));
                    String i = String.valueOf(marker.hashCode());
                    placeIDs.put(i, id);
                    markers.add(marker);
                }
                isInit = false;
/*                for (Marker m : markers)
                    Log.d("ID", placeIDs.get(m));*/
            }

        }


        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

    }
        private class Waiter extends Thread {
            public void run(){
                while(isInit){}
                for(Marker m : markers){
                    new HttpGet().execute(String.valueOf(m.hashCode()));
                }
            }
        }

}
