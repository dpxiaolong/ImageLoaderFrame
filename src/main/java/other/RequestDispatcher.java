package other;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

import loader.Loader;
import request.BitmapRequest;

/**
 * Created by luozhenlong on 2017/12/14.
 */

public class RequestDispatcher extends Thread {

    private static final String TAG = "RequestDispatcher";
    /**
     * 请求的队列，构造传入
     */
    private BlockingQueue<BitmapRequest> mRequestQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> queue) {
        mRequestQueue = queue;
    }

    @Override
    public void run() {
        try {
            //线程没有停止则一直运行
            while (!this.isInterrupted()) {

                final BitmapRequest request = mRequestQueue.take();
                Log.i(TAG, "take request serialNum:" + request.serialNum);
                if (request.isCancel) {
                    continue;
                }
                // 解析图片schema
                final String schema = parseSchema(request.imageUri);
                // 根据schema获取对应的Loader
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                // 加载图片
                imageLoader.loadImage(request);

            }
        } catch (InterruptedException e) {
            Log.i(TAG, "error，isInterrupted");
        }

    }

    /**
     * 解析uri的格式
     *
     * @param uri
     * @return
     */
    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            String schema = uri.split("://")[0];
            Log.i(TAG, "schema" + schema);
            return schema;
        } else {
            Log.e(TAG, "wrong scheme, image uri is : " + uri);
        }
        return "";
    }

}
