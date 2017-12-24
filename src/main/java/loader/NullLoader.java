package loader;

import android.util.Log;

import request.BitmapRequest;

/**
 * Created by luozhenlong on 2017/12/14.
 */

public class NullLoader implements Loader {
    private static final String TAG = "NullLoader";

    @Override
    public void loadImage(BitmapRequest result) {
        Log.i(TAG, "no schma error imageUri : " + result.imageUri);
    }
}
