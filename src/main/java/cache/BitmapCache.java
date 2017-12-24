package cache;

import android.graphics.Bitmap;

import request.BitmapRequest;

/**
 * 缓存的接口,定义图片缓存的基本的三个操作
 * Created by luozhenlong on 2017/12/14.
 */

public interface BitmapCache {
    //获取缓存
    public Bitmap get(BitmapRequest key);

    //存入缓存
    public void put(BitmapRequest key, Bitmap value);

    //移除缓存
    public void remove(BitmapRequest key);

}
