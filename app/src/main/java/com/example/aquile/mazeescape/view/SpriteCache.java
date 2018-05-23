package com.example.aquile.mazeescape.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * A class that stores all the bitmaps that are used in the game.
 * Use this class to load images and associate a name with them. Then, in your
 * game you use that string to refer to these images.
 * 
 * @author Paul de Groot
 */
public class SpriteCache {
	private static final String TAG = "Playground-SC";

	/** A cache of all images that can be used. */
	private HashMap<String, TileImage> images = new HashMap<String, TileImage>();
	
	/** Context to get resources from. */
	private Context context;
	
	/** Size of a bitmap tile */
	private int tileSize = 20;

	/**
	 * Set the context to use for resource loading, e.g. an activity.
	 * @param ctx  The context to set
	 */
	public void setContext( Context ctx ) {
		this.context = ctx;
	}

	/**
	 * Retrieve a sprite based on the image ID.
	 * 
	 * @param imageID  The String-id given when loadTile was called.
	 * @return         The corresponding bitmap, or null.
	 */
	public Bitmap get(String imageID) {
		if( imageID == null )
			return null;
		
		TileImage img = images.get(imageID);
		if( img == null )
			return null;   // Image not found
		return img.bitmap;
	}
	
	/**
	 * Use this method to 'load' the images in the application. Loading means that
	 * a bitmap is created that contains the image scaled to the size of a tile. 
	 * Also, the given 'key' is associated with that image. This means that if a
	 * game object tells this view (by way of getImageId()) to use a certain image,
	 * the correct bitmap can be found.
	 * 
	 * @param key         The string that will be associated with this image 
	 * @param resourceID  The resourceID of the drawable to load, e.g. R.drawable.wombat.
	 * 
	 * @throws Resources.NotFoundException  if the resource could not be found.
	 */
	public void loadTile(String key, int resourceID) {
		// Store this in our hash map
		images.put(key, new TileImage(key, resourceID, tileSize));
	}
	
	/**
	 * Unload a bitmap from memory. Frees up memory.
	 */
	public void unloadTile(String key) {
		images.remove(key);
	}

	/**
	 * Set the size of a tile. Will also reload bitmaps if needed.
	 * 
	 * @param mTileSize
	 */
	void setTileSize(int mTileSize) {
		// Check if tile size actually changed
		if( this.tileSize == mTileSize ) {
			Log.d(TAG, "onSizeChanged: not reloading. Tilesize unchanged");
			return;
		}

		// Set new tile size
		this.tileSize = mTileSize;
		
		// Reload bitmaps
		for (Map.Entry<String, TileImage> i : images.entrySet()) {
			TileImage image = i.getValue();
			image.createBitmap(mTileSize);
		}
	}

	/**
	 * A single image that can be used in the representation of a game
	 * object.
	 * This is not the actual image per se: a bitmap of the exact size of
	 * the tiles is created.
	 * 
	 * @author Paul de Groot
	 */
	class TileImage {
		/** The image name, e.g. 'wombat' */
		private String resourceName;
		/** The resourceID of the source image, e.g. R.drawable.wombat */
		private int resourceID;
		/** The bitmap created from the original image. */
		private Bitmap bitmap;

		/**
		 * Creates a structure that store the resourceID and a bitmap of the size
		 * of a tile in the view.
		 * @param resourceID  The resource ID, e.g. R.drawable.wombat.
		 * @param tileSize    The size of a tile in pixels.
		 *     
		 * @throws Resources.NotFoundException  when the resource was not found.
		 */
		public TileImage(String name, int resourceID, int tileSize) {
			this.resourceName = name;
			this.resourceID = resourceID;
			createBitmap(tileSize);
		}

		/**
		 * Calculate the largest scaling factor that can be used to have
		 * a source image be still larger than the destination size.
		 * 
		 * @param options    Result of a decode with inJustDecodeBounds set to true. 
		 * @param reqWidth   Width of the destination bitmap.
		 * @param reqHeight  Height of the destination bitmap.
		 * @return  Subsample size. One of 1, 2, 4, 8, etc.
		 */
		public int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
		    // Raw height and width of image
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    int inSampleSize = 1;
	
		    if (height > reqHeight || width > reqWidth) {
	
		        final int halfHeight = height / 2;
		        final int halfWidth = width / 2;
	
		        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
		        // height and width larger than the requested height and width.
		        while ((halfHeight / inSampleSize) > reqHeight
		                && (halfWidth / inSampleSize) > reqWidth) {
		            inSampleSize *= 2;
		        }
		    }
	
		    return inSampleSize;
		}
			
		/**
		 * Loads the image identified by the given resourceID and creates a bitmap
		 * the size of a tile from that.
		 * @param tileSize  The tile size.
		 */
		public void createBitmap(int tileSize) {
			Resources resources = SpriteCache.this.context.getResources();
			
			// First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeResource(resources, resourceID, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, tileSize, tileSize);

		    // Log
			Log.d(TAG, "Loading bitmap " + resourceName + ": " +
		             "src ( " + options.outWidth + "x" + options.outHeight +" ) -> [" +
					options.inSampleSize + "] -> dest: (" + 
		            tileSize + "x" + tileSize + ")" );

			// Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    Bitmap sourceImage = BitmapFactory.decodeResource(resources, resourceID, options);
		    
			// Create a bitmap of the exact tile size and draw the drawable on
			// there
			bitmap = Bitmap.createScaledBitmap(sourceImage, tileSize, tileSize, true);
		}
	}
}
