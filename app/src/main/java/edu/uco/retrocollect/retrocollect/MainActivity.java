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

    private int RETURN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.searchBar);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button collectionButton = (Button) findViewById(R.id.collectionButton);
        Button wishListButton = (Button) findViewById(R.id.wishListButton);

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
                Intent contactsActivity = new Intent(MainActivity.this, CollectionActivity.class);
                startActivityForResult(contactsActivity, RETURN);
            }
        });

        wishListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //do stuff for wishList
            }
        });


    }
}


