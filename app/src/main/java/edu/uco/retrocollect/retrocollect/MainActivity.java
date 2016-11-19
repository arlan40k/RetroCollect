package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends Activity {

    private Button searchButton;
    private Button collectionButton;
    private Button wishListButton;
    private EditText searchEditText;
    private String lat = "";
    private String lng = "";
    private int RETURN = 1;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    //Used for bubble functionality
    public static final int OVERLAY_PERMISSION_REQ_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.searchBar);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        MagicButton barcodeButton = (MagicButton) findViewById(R.id.barcodeButton);
        MagicButton collectionButton = (MagicButton) findViewById(R.id.collectionButton);
        MagicButton wishListButton = (MagicButton) findViewById(R.id.wishListButton);
        MagicButton localMerchantButton = (MagicButton) findViewById(R.id.localMerchantsButton);
        MagicButton launchWidget = (MagicButton) findViewById(R.id.launchWidget);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                searchActivity.putExtra("game", searchEditText.getText().toString());
                startActivity(searchActivity);
            }
        });
/*
        collectionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent collectionActivity = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(collectionActivity);
            }
        });
*/
        collectionButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent collectionActivity = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(collectionActivity);
            }
        });
/*
        wishListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent wishListActivity = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(wishListActivity);
            }
        });
*/
        wishListButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wishListActivity = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(wishListActivity);
            }
        });
/*
        localMerchantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localMerchantIntent = new Intent(MainActivity.this, LocalMerchantActivity.class);
                localMerchantIntent.putExtra("LAT", lat);
                localMerchantIntent.putExtra("LNG", lng);
                startActivity(localMerchantIntent);
            }
        });
*/
        localMerchantButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localMerchantIntent = new Intent(MainActivity.this, LocalMerchantActivity.class);
                localMerchantIntent.putExtra("LAT", lat);
                localMerchantIntent.putExtra("LNG", lng);
                startActivity(localMerchantIntent);
            }
        });

        barcodeButton.setMagicButtonClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // Intent wishListActivity = new Intent(MainActivity.this, BarcodeActivity.class);
               // startActivity(wishListActivity);

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {


                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            android.Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{android.Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                Intent barcodeActivity = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(barcodeActivity);

            }
        });

        /*

        launchWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOverlays();
                //Checks to see if we can use a bubble.
                if(checkOverlaysPermission())
                {
                    Intent intent = new Intent(MainActivity.this, BubbleWidgetService.class);
                    startService(intent);
                }
            }
        });

        */

        launchWidget.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOverlays();
                //Checks to see if we can use a bubble.
                if(checkOverlaysPermission())
                {
                    Intent intent = new Intent(MainActivity.this, BubbleWidgetService.class);
                    startService(intent);
                }
            }
        });


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.CAMERA )
                        != PackageManager.PERMISSION_GRANTED ){

            Toast.makeText(MainActivity.this, "Permission was denied, go to settings " +
                    "to enable camera", Toast.LENGTH_SHORT).show();
            return  ;
        }

    }

    //Note the following three methods will only be applicable to build versions >= 23
    //Check permissions
    public boolean checkOverlaysPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this))
                return true;
            else
                return  false;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Intent wishListActivity = new Intent(MainActivity.this, BarcodeActivity.class);
                    startActivity(wishListActivity);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

        }
    }
    //Request permissions required for bubble functionality.
    public void requestOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                }
            }
        }
    }


}

