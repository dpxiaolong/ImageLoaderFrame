package loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import cache.BitmapCache;

import other.SimpleImageLoader;
import request.BitmapRequest;

/**
 * Created by luozhenlong on 2017/12/14.
 */

public abstract class AbsLoader implements Loader {

    private static final String TAG = "AbsLoader";
    /**
     * 图片缓存
     */
    private static BitmapCache mCache = SimpleImageLoader.getInstance().getmConfig().mBitmapCache;


    @Override
    public void loadImage(BitmapRequest request) {

        // 1、从缓存中获取
        Bitmap resultBitmap = mCache.get(request);
//        Log.i(TAG, "缓存 : " + resultBitmap + ", uri = " + request.imageUri);
        if (resultBitmap == null) {
            showLoading(request);
            // 2、没有缓存，调用onLoaderImage加载图片
            //线程睡，测试是否可以显示加载中图片
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultBitmap = onLoadImage(request);
            // 3、缓存图片
            cacheBitmap(request, resultBitmap);
        } else {
            request.justCacheInMem = true;
        }
        // 4、将结果投递到UI线程
        deliveryToUIThread(request, resultBitmap);

    }

    /**
     * 先显示加载中的默认图片
     * @param request
     */
    public void showLoading(final BitmapRequest request) {
        final ImageView imageView = request.getmImageView();
        if (request.isImageViewTagValid()) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
//                    Log.i(TAG,"showLoading imageUri : "+request.imageUri);
                    imageView.setImageResource(SimpleImageLoader.getInstance().getmConfig().mDisplayConfig.loadingResId);
                }
            });
        }
    }

    /**
     * 加载图片
     *
     * @param result
     * @return
     */
    public abstract Bitmap onLoadImage(BitmapRequest result);

    /**
     * 缓存图片
     *
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        // 缓存新的图片
        if (bitmap != null && mCache != null) {
            synchronized (mCache) {
                mCache.put(request, bitmap);
            }
        }
    }

    /**
     * 投递结果到UI线程
     *
     * @param request
     * @param bitmap
     */
    protected void deliveryToUIThread(final BitmapRequest request,
                                      final Bitmap bitmap) {
        Log.i(TAG,"deliveryToUIThread Thread Name"+Thread.currentThread().getName()+"Id:"+Thread.currentThread().getId());
        final ImageView imageView = request.getmImageView();
        if (imageView == null) {
            return;
        }
        //这里用了View的post方式将结果更新到了主线程。
        imageView.post(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG,"run Thread Name"+Thread.currentThread().getName()+"Id:"+Thread.currentThread().getId());

                updateImageView(request, bitmap);
            }
        });
    }

    /**
     * 更新控件
     *
     * @param request
     * @param result
     */
    private void updateImageView(BitmapRequest request, Bitmap result) {

        Log.i(TAG,"updateImageView Thread Name"+Thread.currentThread().getName()+"Id:"+Thread.currentThread().getId());

        final ImageView imageView = request.getmImageView();
        final String uri = request.imageUri;
        if (result != null && imageView.getTag().equals(uri)) {
            imageView.setImageBitmap(result);
        }

        // 加载失败,return是为了不让界面刷新，把失败的图片刷成空白
        if (result == null) {
            Log.i(TAG,"updateImageView fail ,resID:"+SimpleImageLoader.getInstance().getmConfig().mDisplayConfig.failedResId);
            imageView.setImageResource(SimpleImageLoader.getInstance().getmConfig().mDisplayConfig.failedResId);
            return;
        }

        // 回调接口,request != null则不进入的原因是会把本来失败设置的图片更新掉
        if (request.imageListener != null) {
            Log.i(TAG,"result:"+result);
            request.imageListener.onComplete(imageView, result, uri);
        }
    }


}
