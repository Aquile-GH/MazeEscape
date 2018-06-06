package com.example.aquile.mazeescape;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.example.aquile.mazeescape.objects.Avatar;
import com.example.aquile.mazeescape.objects.Exit;
import com.example.aquile.mazeescape.objects.Wall;
import com.example.aquile.mazeescape.playground.view.GameBoardView;

public class MazeBoardView extends GameBoardView {
    private static final String TAG = "GameView";

    public MazeBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGameView();
    }

    public MazeBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    private void initGameView() {
        Log.d(TAG, "Loading all images");

        spriteCache.setContext(this.getContext());

        // Load the 'empty' cell bitmap
        spriteCache.loadTile("empty", R.drawable.cell);

        // Load the images for the GameObjects
        spriteCache.loadTile(Wall.Wall_IMAGE, R.drawable.rock);
        spriteCache.loadTile(Exit.EXIT_IMAGE, R.drawable.leaf);
        spriteCache.loadTile(Avatar.AVATAR_IMAGE, R.drawable.wombat);
    }
}