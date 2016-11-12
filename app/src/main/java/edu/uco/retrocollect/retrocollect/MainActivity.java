package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                startActivity(collectionActivity);
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent wishListActivity = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(wishListActivity);
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
    }

}

