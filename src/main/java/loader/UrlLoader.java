package loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import request.BitmapRequest;

/**
 * Url的加载器，当schema是Http开头的会掉用这个loader
 * Created by luozhenlong on 2017/12/14.
 */

public class UrlLoader extends AbsLoader {
    FileOutputStream fos = null;
    InputStream is = null;
    private HttpURLConnection conn = null;

    @Override
    public Bitmap onLoadImage(BitmapRequest request) {

        Bitmap bitmap = null;

        final String imageUrl = request.imageUri;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            is = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(is, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != fos) {
                    fos.close();
                }
                if (null != conn) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return bitmap;

    }
}
