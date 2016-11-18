package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.ArrayList;


public class BarcodeActivity extends Activity {

    SurfaceView cameraView;
    TextView barcodeView;
    TextView searchField;
    Button findBarcodeButton;
    Button searchButton;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    //private boolean permCheck = false;
    private String barcode = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        searchField = (TextView) findViewById(R.id.searchField);
        findBarcodeButton = (Button) findViewById(R.id.findBarcodeButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setEnabled(false);

        //SurfaceView cameraView = (SurfaceView) findViewById(R.id.camera_view);

        barcodeView = (TextView) findViewById(R.id.code_info);

        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .build();

         final CameraSource cameraSource = new CameraSource
                .Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build();

/*
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
*/



        //final CameraSource cameraSource = cameraSourceBuilder;

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {



                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(),
                                android.Manifest.permission.CAMERA )
                                != PackageManager.PERMISSION_GRANTED ){
                    return  ;
                }


                    try {
                        //cameraSource.start(cameraView.getHolder());
                        //cameraSource.start();

                        cameraSource.start(cameraView.getHolder());


                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }




            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {


                    cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeView.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeView.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                        }
                    });
                }
            }
        });


        findBarcodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                barcode = barcodeView.getText().toString();
               // searchField.setText(barcode);
                if(!barcode.equalsIgnoreCase("Nothing to read.")) {
                    new BarcodeApiTask().execute(barcode);
                }
                else if(barcode.equalsIgnoreCase("Nothing to read.")){
                    Toast.makeText(BarcodeActivity.this, "No barcode scanned! try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String searchString = searchField.getText().toString();

                Intent searchActivity = new Intent(BarcodeActivity.this, SearchActivity.class);
                searchActivity.putExtra("game", searchString);
                startActivity(searchActivity);
            }
        });






    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.CAMERA &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }
*/
    private class BarcodeApiTask extends AsyncTask<Object, Void, HttpResponse<JsonNode>> {

        //Network Activities must be done in  doInBackground
        @Override
        protected HttpResponse<JsonNode> doInBackground(Object[] objects) {
            HttpResponse<JsonNode> response = null;
            try {
                //Get my string from the objects
                String barcode = (String) objects[0];

                //API Request
                //String searchString = JsonGameParser.parseSearchString(barcode);
                //Changed order to relevance rather than date released -HASEEB

                    response = Unirest.get("https://goodfoods-search-grocery-product-" +
                            "reviews-by-barcode-v1.p.mashape.com/search?barcode="+ barcode)
                                    .header("X-Mashape-Key", "4KjzzTanigmshoC1cuOPyXU16sUvp1xp5m7j" +
                                            "snV3lAlo5HH0wK")
                                    .header("Accept", "application/json")
                                    .asJson();





                return response;
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (response == null ) {
                Toast.makeText(BarcodeActivity.this,
                        "Invalid data. Possibly a wrong query",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {

                ArrayList<String> gameArrayList = JsonBarcodeParser.getGameList(response);

                String pubName = " ";


                for(int i = 0; i < gameArrayList.size(); i++) {

                        pubName = gameArrayList.get(i);

                }

                searchField.setText(pubName);

                if(!pubName.equals(" ")){
                    searchButton.setEnabled(true);
                }
            }


        }



    }
}
