package com.codingblocks.hangmangame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private String category="capitals";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] categories=getResources().getStringArray(R.array.categories);

        Spinner spinner=(Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);             // Apply the adapter to the spinner
        spinner.setPrompt("capitals");
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categories[position];
                Log.i("category", category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button playBtn=(Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("category", category);
                startActivity(i);

            }
        });

    }
}
