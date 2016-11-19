package edu.uco.retrocollect.retrocollect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ActionMode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class LocalMerchantActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static ActionMode actionMode;
    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private LocationRequest locationRequest;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static final String TAG = LocalMerchantActivity.class.getSimpleName();
    private static ArrayList<Marker> markers = new ArrayList<>();
    private HashMap<String, String> placeIDs = new HashMap<>();
    private HashMap<Marker, ArrayList<String>> storeInfo = new HashMap<>();
    private LatLng cLoc = null;
    public static boolean isInit = true;
    public boolean initMove = true;
    public boolean mReady = false;
    public static boolean wait = false;
    private LatLng loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check get status of gps location
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean gpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(gpsStatus == false) {
            wait = true;
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent gpsOptionsIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                            wait = false;
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            wait = false;
                            onBackPressed();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("To use this feature you must enable your GPS").setPositiveButton("Enable", dialogClickListener)
                    .setNegativeButton("Return", dialogClickListener).show();
        }




        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(25 * 1000);        // 10 seconds, in milliseconds

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //Used for non-gps locationing
/*      String lat = getIntent().getStringExtra("LAT");
        String lng = getIntent().getStringExtra("LNG");*/

/*        if (!lat.equals("") && !lng.equals(""))
            loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        else
            loc = new LatLng(35.638033, -97.485540);*/
        markers.clear();
        placeIDs.clear();
        storeInfo.clear();
        isInit = true;


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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        new HttpGet().execute();
        Thread t1 = new Waiter();
        Thread t2 = new Updater();
        t1.start();
        t2.start();
        //comgooglemaps://?q=Pizza&center=37.759748,-122.427135

        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=YOUR_API_KEY3
        mReady = true;
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

    @Override
    protected void onResume() {
        super.onResume();
        initMove = true;
        apiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (apiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
            apiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleNewLocation(Location location) {

        cLoc= new LatLng(location.getLatitude(), location.getLongitude());

        if(initMove == true && cLoc != null)
        {
            CameraUpdate center=  CameraUpdateFactory.newLatLng(cLoc);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            initMove = false;
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
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
                    while (cLoc == null && wait == true){}
                    Uri builtUri = Uri.parse(LocalPlace_URL).buildUpon()
                            .appendQueryParameter("location", Double.toString(cLoc.latitude) + "," + Double.toString(cLoc.longitude))
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
                //Toast.makeText(LocalMerchantActivity.this, "Search Failed", Toast.LENGTH_SHORT).show();
                isInit = true;
                new HttpGet().execute();
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

    private class Updater extends Thread {
        public void run(){

        }



    }
}
