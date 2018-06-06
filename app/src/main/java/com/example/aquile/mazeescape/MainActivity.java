package com.example.aquile.mazeescape;

import com.example.aquile.mazeescape.MazeBoardView;
import com.example.aquile.mazeescape.R;
import com.example.aquile.mazeescape.playground.model.GameBoard;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aquile.mazeescape.MazeEscape;

/**
 * The main activity.
 *
 * @author Paul de Groot
 * @author Jan Stroet
 */
public class MainActivity extends Activity {
    private MazeEscape game;
    private MazeBoardView gameView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Load main.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find some of the user interface elements
        gameView = (MazeBoardView) findViewById(R.id.Maze);

        // Create the game object. This contains all data and functionality
        // belonging to the game
        game = new MazeEscape();

        // Tell the game board view which game board to show
        GameBoard board = game.getGameBoard();
        gameView.setGameBoard(board);
        gameView.setFixedGridSize(board.getWidth(), board.getHeight());

        // Tell user to start the game
        Toast.makeText(getApplicationContext(), "Lets start",
                Toast.LENGTH_SHORT).show();
    }
}
