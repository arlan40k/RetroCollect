package edu.uco.retrocollect.retrocollect;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CollectionActivity extends Activity {

    private Button tempGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Button tempGameButton = (Button) findViewById(R.id.tempGameButton);

        tempGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            /* nic do whatever you gotta do here to get to game activity while testing
            I'm leaving this here until i can populate the collection properly
            will be here until stable passing to game from clicking titles
             */


            }
        });

    }
}
