package com.example.aquile.mazeescape.view;

import java.util.Observable;
import java.util.Observer;

import com.example.aquile.mazeescape.model.GameBoard;
import com.example.aquile.mazeescape.model.GameObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * View that draws a grid of images, representing the game board.
 * This class handles the resizing of the grid.
 * Two possible options exist:
 *  a) a fixed grid, where the number of tiles remains the same, but tiles
 *     may become smaller or larger.
 *  b) a relative grid, where the tile size remains the same, but the number
 *     of tiles drawn may change.
 * 
 * This class also handles the resizing of the bitmaps, so the image quality
 * remains good.
 * 
 * @author Jan Stroet
 * @author Paul de Groot
 */
public abstract class GameBoardView extends View implements Observer {
	private static final String TAG = "Playground";

	/** When true, the number of tiles in the view remains the same. */
	private boolean fixedGrid = true;

	/** Number of tiles in X-direction. */ 
	private int tileCountX = 10;

	/** Number of tiles in Y-direction. */ 
	private int tileCountY = 10;

	/** Size (in pixels) of the tiles. */
	private int mTileSize = 20;

	/** There is a border around the tile grid. This is the border size in pixels in 
	 * the X-direction. */
	protected int borderSizeX;

	/** There is a border around the tile grid. This is the border size in pixels in
	 *  the Y-direction. */
	protected int borderSizeY;

	/** The game board to draw. */
	private GameBoard board;

	/** The number of bitmaps layers. This is 2 by default: one for the background image
	 *  and one for the game objects.
	 */
	private static final int NUM_BITMAP_LAYERS = 2;

	/** Class keeping track of all original bitmaps, so they can be easily recreated when
	 *  the device orientation or resolution changes.
	 */
	protected SpriteCache spriteCache;

	/** A two-dimensional array that stores the bitmaps to draw. */
	private Bitmap[][][] mTileGrid;

	/** Object that can paint this view. */
	private final Paint mPaint = new Paint();

	/** Offset of view into game board. */
	private int modelOffsetX = 0;
	
	/** Offset of view into game board. */
	private int modelOffsetY = 0;

	/**
	 * Constructor.
	 */
	public GameBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusable(true);
		spriteCache = new SpriteCache();
	}

	/**
	 * Constructor.
	 */
	public GameBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		spriteCache = new SpriteCache();
	}

	/**
	 * Sets this view to 'fixed grid mode'. This means that the number of tiles
	 * in the grid will always be the same, but the tile size is changed to match
	 * the size of the view.
	 * 
	 * @param tilesX  The number of tiles in X-direction.
	 * @param tilesY  The number of tiles in Y-direction.
	 */
	public void setFixedGridSize(int tilesX, int tilesY) {
		fixedGrid = true;
		tileCountX = tilesX;
		tileCountY = tilesY;

		recalculateGrid();
	}

	/**
	 * Sets this view to 'variable grid mode'. This means that the grid size will 
	 * always be the same, and the number of tiles in view will be changed according
	 * to the size of the view.
	 * 
	 * @param tileSize  The size in pixels of a tile.
	 */
	public void setVariableGridSize(int tileSize) {
		fixedGrid = false;
		mTileSize = tileSize;

		recalculateGrid();
	}

	/**
	 * Sets a reference to the game board that will be drawn by this view.
	 * Changes to this board will be noticed by calls to notifyObserver.
	 * 
	 * @param board  The game board.
	 */
	public void setGameBoard(GameBoard board) {
		this.board = board;
		board.addObserver(this);

		determineGridBitmaps();
		invalidate();
	}

	/**
	 * Set the offset this view has to the model. This means that if the offset
	 * is (3,3) then the top-left corner of the view will draw the tile (3,3) in
	 * the model.
	 * 
	 * @param offX   X-coordinate of the offset.
	 * @param offY   Y-coordinate of the offset.
	 */
	public void setModelOffset( int offX, int offY ) {
		this.modelOffsetX = offX;
		this.modelOffsetY = offY;
	}
	
	public int getModelOffsetX() {
		return this.modelOffsetX;
	}

	public int getModelOffsetY() {
		return this.modelOffsetY;
	}

	/**
	 * Handles the basic update: the visualization is updated according to
	 * objects on the game board.
	 */
	public void update(Observable o, Object arg) {
		determineGridBitmaps();
		invalidate();
	}

	/**
	 * Called when the user touches the game board.
	 * Determines which tile was clicked and which object was on that place of
	 * the board.
	 * If an object was present, GameObject.onTouched() will be called.
	 * If no object was there, GameBoard.onEmptyTileClicked() will be called.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) ((event.getX() - borderSizeX) / mTileSize);
		int y = (int) ((event.getY() - borderSizeY) / mTileSize);
		if (x < tileCountX && y < tileCountY
				&& (event.getX() - borderSizeX) >= 0
				&& (event.getY() - borderSizeY) >= 0) {

			// Who you gonna call?
			if (board != null) {
				int mx = x + modelOffsetX;
				int my = y + modelOffsetY;			
				Log.d(TAG, "Touched view tile ("+x+", "+y+") = model tile ("+mx+", "+my+")\n");

				if( (mx < board.getWidth()) && (my < board.getHeight())) {
			
					// Determine the object clicked
					GameObject object = board.getObject(mx, my);

					// Call the listener
					if (object != null) {
						object.onTouched(board);
					} else {
						board.onEmptyTileClicked(mx, my);
					}
				}
			}
		}

		return super.onTouchEvent(event);
	}
	
	/** Called when the view size changed. */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Recalculate the grid and tile sizes
		recalculateGrid();
	}

	/**
	 * Recalculates the size of the tiles and the number of tiles in view.
	 */
	private void recalculateGrid() {
		// Get size of view
		// If size is invalid, do nothing
		int w = getWidth();
		int h = getHeight();
		if (w == 0 || h == 0)
			return;

		// Determine tile size and number of tiles
		if (fixedGrid) {
			// Fixed grid: number of tiles stays the same, tile size calculated
			mTileSize = Math.min((int) Math.floor(w / tileCountX),
					(int) Math.floor(h / tileCountY));
			Log.d(TAG, "onSizeChanged: tile size is now " + mTileSize);
		} else {
			// Variable grid: tileSize is static, tile count changes
			tileCountX = (int) Math.floor(w / mTileSize);
			tileCountY = (int) Math.floor(h / mTileSize);
			Log.d(TAG, "onSizeChanged: tile count is now " + tileCountX + ", "
					+ tileCountY);
		}

		// Tiles may not use the whole view, leaving a border. Calculate the size
		// of it. (The game board is centered within the view).
		borderSizeX = ((w - (mTileSize * tileCountX)) / 2);
		borderSizeY = ((h - (mTileSize * tileCountY)) / 2);
		Log.d(TAG, "onSizeChanged: border is now " + borderSizeX + ", " + borderSizeY);

		// If the number of cells in the grid is fixed, cell size may have
		// changed.
		// Reload all the bitmaps
		if (fixedGrid) {
			spriteCache.setTileSize(mTileSize);
			invalidate();
		}
		
		// Update the array of the bitmaps, since that may have now changed
		determineGridBitmaps();
	}

	/**
	 * Update the array that contains which bitmap should be drawn where.
	 * This is done every time the game board changed.
	 */
	private void determineGridBitmaps() {
		mTileGrid = new Bitmap[tileCountX][tileCountY][NUM_BITMAP_LAYERS];
		if( board == null )
			return;

		// For each tile...
		for (int x = 0; x < tileCountX; x++) {
			for (int y = 0; y < tileCountY; y++) {
				
				// Calculate the coordinate of this view-tile in the model
				int mx = x + modelOffsetX;
				int my = y + modelOffsetY;
				
				// Get the game object that is on the board at that position.
				GameObject object = null;
				if( (mx < board.getWidth()) && (my < board.getHeight())) {
					object = board.getObject(mx, my);
				}
				
				// Ask the game object which object should be shown
				String objectImg = null;
				if( object != null ) {
					objectImg = object.getImageId();
				}
				
				// Ask the game board which background image should be shown
				String backgroundImg = null;
				if( (mx < board.getWidth()) && (my < board.getHeight())) {
					backgroundImg = board.getBackgroundImg(mx, my);
				}
				
				// Find the associated bitmaps
				mTileGrid[x][y][0] = spriteCache.get(backgroundImg);
				mTileGrid[x][y][1] = spriteCache.get(objectImg);

				// Emit warnings if images could not be found
				if( (backgroundImg != null) && (mTileGrid[x][y][0] == null) )
					Log.e(TAG, "Could not find bitmap for tile(" + x + ", " + y + "): " + backgroundImg );
				if( (objectImg != null) && (mTileGrid[x][y][0] == null) )
					Log.e(TAG, "Could not find bitmap for tile(" + x + ", " + y + "): " + objectImg );
			}
		}
	}

	/**
	 * Called to draw the view.
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// For each tile on the board ...
		for (int x = 0; x < tileCountX; x++) {
			for (int y = 0; y < tileCountY; y++) {
				// ... and for every layer ...
				for( int layer = 0; layer < NUM_BITMAP_LAYERS; layer++ ) {
					if (mTileGrid[x][y][layer] != null) {
						// ... draw the image
						int drawAtX = borderSizeX + x * mTileSize;
						int drawAtY = borderSizeY + y * mTileSize;
						canvas.drawBitmap(mTileGrid[x][y][layer], drawAtX, drawAtY, mPaint);
					}
				}
			}
		}
	}
}
