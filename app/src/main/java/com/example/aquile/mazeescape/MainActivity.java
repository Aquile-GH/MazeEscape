package com.example.aquile.mazeescape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        }

    public void startGame(View view) {
        Intent newIntent = new Intent(MainActivity.this, MainGame.class);
        startActivity(newIntent);
    }

    public void openHighScores(View view) {
        Intent newIntent = new Intent(MainActivity.this, HighScoreScreen.class);
        startActivity(newIntent);
    }
}
