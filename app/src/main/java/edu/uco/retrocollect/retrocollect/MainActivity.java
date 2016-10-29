package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button searchButton;
    private Button collectionButton;
    private Button wishListButton;
    private EditText searchEditText;
    private String lat = "";
    private String lng = "";
    private int RETURN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.searchBar);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button collectionButton = (Button) findViewById(R.id.collectionButton);
        Button wishListButton = (Button) findViewById(R.id.wishListButton);
        Button localMerchantButton = (Button) findViewById(R.id.localMerchantsButton);
        Button tempNewCastleButton = (Button) findViewById(R.id.tmpNewCastle);
        Button tempEdmondButton = (Button) findViewById(R.id.tmpEdmond);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                searchActivity.putExtra("game", searchEditText.getText().toString());
                startActivity(searchActivity);
            }
        });

        collectionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent collectionActivity = new Intent(MainActivity.this, CollectionActivity.class);
                startActivityForResult(collectionActivity, RETURN);
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent wishListActivity = new Intent(MainActivity.this, WishListActivity.class);
                startActivityForResult(wishListActivity, RETURN);
            }
        });

        localMerchantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localMerchantIntent = new Intent(MainActivity.this, LocalMerchantActivity.class);
                localMerchantIntent.putExtra("LAT", lat);
                localMerchantIntent.putExtra("LNG", lng);
                startActivity(localMerchantIntent);
            }
        });

        tempEdmondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = "35.638033";
                lng  = "-97.485540";
                Toast.makeText(MainActivity.this, "LAT:  35.638033 \nLNG: -97.485540", Toast.LENGTH_SHORT).show();
            }
        });
        tempNewCastleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = "35.290691";
                lng  = "-97.606410";
                Toast.makeText(MainActivity.this, "LAT:  35.290691 \nLNG: -97.606410", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
/*Test commit and push - Nicholas*/

