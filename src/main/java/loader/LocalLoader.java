package loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import request.BitmapRequest;

/**
 * 本地图片加载器，当解析到文件是以file开头的时候调用这个加载器
 * Created by luozhenlong on 2017/12/16.
 */

public class LocalLoader extends AbsLoader {
    private static final String TAG = "LocalLoader";

    @Override
    public Bitmap onLoadImage(BitmapRequest request) {

        final String imagePath = Uri.parse(request.imageUri).getPath();
        Log.i(TAG, "imagePath :" + imagePath);
        final File imgFile = new File(imagePath);

        if (!imgFile.exists()) {
            Log.i(TAG, "imgFile. not exists");
            return null;
        }
        // 加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return bitmap;
    }
}
