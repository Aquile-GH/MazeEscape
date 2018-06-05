package com.example.aquile.mazeescape;

import com.example.aquile.mazeescape.playground.model.GameBoard;

public class MazeBoard extends GameBoard {
        private static final int MAZE_WIDTH = 12;
        private static final int MAZE_HEIGHT = 14;

        public MazeBoard() {
            super(MAZE_WIDTH, MAZE_HEIGHT);
        }

        @Override
        public void onEmptyTileClicked(int x, int y) {
            //Nothing
        }

        @Override
        public String getBackgroundImg(int x, int y) {
            return "empty";
        }
}
