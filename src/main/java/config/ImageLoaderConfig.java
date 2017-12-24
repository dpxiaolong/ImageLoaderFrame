package config;

import android.util.Log;

import cache.BitmapCache;
import cache.MemoryCache;
import loadPolicy.LoadPolicy;
import loadPolicy.SerialPolicy;

/**
 * 用于配置图片加载的一些属性
 * <p>
 * Created by luozhenlong on 2017/12/14.
 */

public class ImageLoaderConfig {
    private static final String TAG = "ImageLoaderConfig";
    /**
     * 缓存策略
     */
    public BitmapCache mBitmapCache = new MemoryCache(MemoryCache.DEFAULT_MEMORY_CACHE_SIZE);
    /**
     * 显示的配置，加载中和加载完毕的图片设置
     */
    public DisplayConfig mDisplayConfig = new DisplayConfig();
    /**
     * 加载的策略，加载顺序的设置
     */
    public LoadPolicy mLoadPolicy = new SerialPolicy();
    /**
     * 多线程运行的线程数，默认四个，因为volley是4个
     */
    public static int mThreadCount = 4;

    /**
     * 设置并行加载图片的线程数
     *
     * @param count
     * @return
     */
    public ImageLoaderConfig setThreadCount(int count) {
        Log.i(TAG, "setThreadCount " + count);
        if (count >= 1) {
            mThreadCount = count;
        }
        return this;
    }

    /**
     * 设置缓存策略
     *
     * @return
     */
    public ImageLoaderConfig setCache(BitmapCache cache) {
        mBitmapCache = cache;
        return this;
    }

    /***
     * 设置加载中应该显示的图片
     * @param resID
     * @return
     */
    public ImageLoaderConfig setLoadingImage(int resID) {
        mDisplayConfig.loadingResId = resID;
        return this;
    }

    /**
     * 设置加载失败应该显示的图片
     *
     * @param resId
     * @return
     */
    public ImageLoaderConfig setFailImage(int resId) {
        mDisplayConfig.failedResId = resId;
        return this;
    }

    /**
     * 设置加载的策略，谁先加载
     *
     * @param policy
     * @return
     */
    public ImageLoaderConfig setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
        return this;
    }


}
