package com.example.aquile.mazeescape.objects;

import com.example.aquile.mazeescape.playground.model.GameBoard;
import com.example.aquile.mazeescape.playground.model.GameObject;

public class Exit extends GameObject {
    public static final String EXIT_IMAGE = "Exit";


    @Override
    public String getImageId() {
        return EXIT_IMAGE;
    }

    @Override
    public void onTouched(GameBoard gameBoard) {

    }
}

