package com.example.aquile.mazeescape.objects;

import com.example.aquile.mazeescape.playground.model.GameBoard;
import com.example.aquile.mazeescape.playground.model.GameObject;

public class Wall extends GameObject {
    public static final String Wall_IMAGE = "Wall";

    @Override
    public String getImageId() {
        return Wall_IMAGE;
    }

    @Override
    public void onTouched(GameBoard gameBoard) {
        //nothing
    }
}
