package com.example.aquile.mazeescape.objects;

import com.example.aquile.mazeescape.playground.model.GameBoard;
import com.example.aquile.mazeescape.playground.model.GameObject;

public class Avatar extends GameObject {
    public static final String AVATAR_IMAGE = "Avatar";

    @Override
    public String getImageId() {
        return AVATAR_IMAGE;
    }

    @Override
    public void onTouched(GameBoard gameBoard) {

    }
}
