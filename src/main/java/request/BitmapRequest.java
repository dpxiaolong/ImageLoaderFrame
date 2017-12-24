package request;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import other.SimpleImageLoader;
import loadPolicy.LoadPolicy;

/**
 * 图片加载的请求
 * Created by luozhenlong on 2017/12/14.
 */

public class BitmapRequest implements Comparable{
    private static final String TAG = "BitmapRequest";

    public SimpleImageLoader.ImageListener imageListener;
    public String imageUri = "";

    private ImageView mImageView = null;
    /**
     * 请求队列号
     */
    public int serialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean isCancel = false;

    /**
     * 是否只在内存缓存
     */
    public boolean justCacheInMem = false;

    /**
     * 加载策略
     */
    LoadPolicy mLoadPolicy = SimpleImageLoader.getInstance().getmConfig().mLoadPolicy;

    public BitmapRequest(ImageView imageView, String uri,
                         SimpleImageLoader.ImageListener listener) {
        imageListener = listener;
        imageUri = uri;
        mImageView = imageView;
        mImageView.setTag(uri);
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        BitmapRequest other = (BitmapRequest) obj;
        if (this.imageUri == other.imageUri) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断ImageView和tag是否相等
     *
     * @return
     */
    public boolean isImageViewTagValid() {
        Log.i(TAG, "isImageViewTagValid :" + mImageView.getTag() + "   imageUri:" + imageUri);
        if (mImageView.getTag() != null) {
            if (mImageView.getTag().equals(imageUri)) {
                Log.i(TAG, "isImageViewTagValid true");
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        BitmapRequest other = (BitmapRequest)o;
        return mLoadPolicy.compare(this,other);
    }
}
