package com.example.aquile.mazeescape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aquile.mazeescape.model.GameBoard;
import com.example.aquile.mazeescape.view.MazeView;

public class MainGame extends AppCompatActivity {
    private MazeView mazeView;
    private Maze maze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        mazeView = findViewById(R.id.mazeView);
        maze = new Maze();

        GameBoard gameBoard = maze.getGameBoard();
        mazeView.setGameBoard(gameBoard);
        mazeView.setFixedGridSize(gameBoard.getWidth(), gameBoard.getHeight());
    }
}
