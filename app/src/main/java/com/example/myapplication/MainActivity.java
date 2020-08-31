package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View V) {
                Intent categoryIntents = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(categoryIntents);

                                        }
                                    }
        );
        exitBtn=findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });


    }
}