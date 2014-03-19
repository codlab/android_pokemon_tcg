package fr.codlab.cartes.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by kevin on 18/03/14.
 */
public class Cache {
    private LruCache<Integer, Bitmap> _cache;

    public Cache(){
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);

        _cache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final Integer key, final Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            _cache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(final int key) {
        return (Bitmap) _cache.get(key);
    }
}
