package cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import request.BitmapRequest;

/**
 * 内存缓存实现类
 * Created by luozhenlong on 2017/12/14.
 */

public class MemoryCache implements BitmapCache {
    /**
     * 默认值，选-1会最终取1/4可用内存mCacheSize
     */
    public static final int DEFAULT_MEMORY_CACHE_SIZE = -1;
    //持有一个Lru算法的内存缓存容器
    private LruCache<String, Bitmap> mMemeryCache;

    //最大虚拟机可用内存measured in bytes
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    //取四分之一作为缓存
    final int mCacheSize = maxMemory / 4;


    @Override
    public Bitmap get(BitmapRequest key) {
        return mMemeryCache.get(key.imageUri);
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        mMemeryCache.put(key.imageUri, value);
    }

    @Override
    public void remove(BitmapRequest key) {
        mMemeryCache.remove(key.imageUri);
    }

    /**
     * @param CacheSize 指定的内存缓存的大小
     */
    public MemoryCache(int CacheSize) {
        //指定了缓存容量则按照指定，否则取1/4可用内存
        if (CacheSize > 0 && CacheSize < maxMemory) {
            mMemeryCache = new LruCache<String, Bitmap>(CacheSize);
        } else {
            mMemeryCache = new LruCache<String, Bitmap>(mCacheSize);
        }
    }


}
