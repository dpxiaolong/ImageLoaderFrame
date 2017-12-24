package cache;

import android.graphics.Bitmap;

import request.BitmapRequest;

/**
 * Created by luozhenlong on 2017/12/14.
 */

public class NoCache implements BitmapCache {
    @Override
    public Bitmap get(BitmapRequest key) {
        return null;
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {

    }

    @Override
    public void remove(BitmapRequest key) {

    }
}
