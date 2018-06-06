package com.example.aquile.mazeescape;

import com.example.aquile.mazeescape.objects.Avatar;
import com.example.aquile.mazeescape.objects.Exit;
import com.example.aquile.mazeescape.objects.Wall;

import com.example.aquile.mazeescape.playground.model.Game;
import com.example.aquile.mazeescape.playground.model.GameBoard;

public class MazeEscape extends Game {
    public static final String TAG = "MazeEscape";

    public MazeEscape() {
        super(new MazeBoard());

        initNewGame();
    }

    public void initNewGame() {

        GameBoard board = getGameBoard();
        board.removeAllObjects();

        //Start
        board.addGameObject(new Avatar(), 5, 7);

        //Walls around the borders
        for (int i = 0; i < 14; i++) {
            board.addGameObject(new Wall(),0,i);
            board.addGameObject(new Wall(),11,i);

            if(i < 11 && i != 0){
                board.addGameObject(new Wall(), i, 0);

                if(i != 1){
                    board.addGameObject(new Wall(), i,13);
                }
            }

        }

        //Walls inside the maze
        WallsIn(board);

        //Exit
        board.addGameObject(new Exit(),1,13);

        board.updateView();
    }

    private void WallsIn(GameBoard board){
        board.addGameObject(new Wall(),1,6);
        board.addGameObject(new Wall(),1,7);
        board.addGameObject(new Wall(),1,8);
        board.addGameObject(new Wall(),2,2);
        board.addGameObject(new Wall(),2,3);
        board.addGameObject(new Wall(),2,4);
        board.addGameObject(new Wall(),2,8);
        board.addGameObject(new Wall(),2,10);
        board.addGameObject(new Wall(),2,11);
        board.addGameObject(new Wall(),3,3);
        board.addGameObject(new Wall(),3,6);
        board.addGameObject(new Wall(),3,10);
        board.addGameObject(new Wall(),4,1);
        board.addGameObject(new Wall(),4,5);
        board.addGameObject(new Wall(),4,6);
        board.addGameObject(new Wall(),4,7);
        board.addGameObject(new Wall(),4,8);
        board.addGameObject(new Wall(),4,10);
        board.addGameObject(new Wall(),4,12);
        board.addGameObject(new Wall(),5,1);
        board.addGameObject(new Wall(),5,3);
        board.addGameObject(new Wall(),5,5);
        board.addGameObject(new Wall(),5,8);
        board.addGameObject(new Wall(),6,3);
        board.addGameObject(new Wall(),6,5);
        board.addGameObject(new Wall(),6,8);
        board.addGameObject(new Wall(),6,9);
        board.addGameObject(new Wall(),6,11);
        board.addGameObject(new Wall(),7,2);
        board.addGameObject(new Wall(),7,3);
        board.addGameObject(new Wall(),7,5);
        board.addGameObject(new Wall(),7,6);
        board.addGameObject(new Wall(),7,8);
        board.addGameObject(new Wall(),7,9);
        board.addGameObject(new Wall(),8,2);
        board.addGameObject(new Wall(),8,8);
        board.addGameObject(new Wall(),8,11);
        board.addGameObject(new Wall(),9,2);
        board.addGameObject(new Wall(),9,4);
        board.addGameObject(new Wall(),9,5);
        board.addGameObject(new Wall(),9,6);
        board.addGameObject(new Wall(),9,10);
        board.addGameObject(new Wall(),9,11);
        board.addGameObject(new Wall(),10,8);
    }
}
