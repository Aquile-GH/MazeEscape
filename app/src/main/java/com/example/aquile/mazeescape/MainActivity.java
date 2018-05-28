package com.example.aquile.mazeescape;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
