package com.example.aquile.mazeescape;

import com.example.aquile.mazeescape.model.GameBoard;

public class MazeStructure extends GameBoard {
    private static final int MAZE_X = 14;
    private static final int MAZE_Y = 12;

    public MazeStructure() {
        super(MAZE_X, MAZE_Y);
    }

    @Override
    public void onEmptyTileClicked(int x, int y) {
    }
}
