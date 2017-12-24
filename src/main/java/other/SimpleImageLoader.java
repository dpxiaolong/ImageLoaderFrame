package other;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import cache.BitmapCache;
import cache.NoCache;
import config.DisplayConfig;
import config.ImageLoaderConfig;
import request.BitmapRequest;
import loadPolicy.SerialPolicy;

/**
 * 加载图片的工具入口类，用于整体控制
 * 通过init传入的配置进行初始化后成为加载器
 * Created by luozhenlong on 2017/12/14.
 */

public class SimpleImageLoader {
    private static final String TAG = "other.SimpleImageLoader";
    /**
     * 实例
     */
    private static SimpleImageLoader mInstance;
    /**
     * 网络请求队列，用于请求网络图片
     */
    private RequestQueue mImageQueue;
    /**
     * 图片加载配置，用于配置
     */
    private ImageLoaderConfig mConfig;
    /**
     * 缓存策略
     */
    public BitmapCache mCache;

    /**
     * 私有化构造函数，单例用
     */
    private SimpleImageLoader() {
    }


    public static void setmInstance(SimpleImageLoader mInstance) {
        SimpleImageLoader.mInstance = mInstance;
    }

    public RequestQueue getmImageQueue() {
        return mImageQueue;
    }

    public void setmImageQueue(RequestQueue mImageQueue) {
        this.mImageQueue = mImageQueue;
    }

    public ImageLoaderConfig getmConfig() {
        return mConfig;
    }

    public void setmConfig(ImageLoaderConfig mConfig) {
        this.mConfig = mConfig;
    }

    public BitmapCache getmCache() {
        return mCache;
    }

    public void setmCache(BitmapCache mCache) {
        this.mCache = mCache;
    }

    /**
     * 单例模式,客户端任意一个即可，这里用懒汉模式
     *
     * @return
     */
    public static SimpleImageLoader getInstance() {

        if (null == mInstance) {
            mInstance = new SimpleImageLoader();
        }
        return mInstance;
    }

    /**
     * 初始化配置，用于
     *
     * @param config
     */
    public void init(ImageLoaderConfig config) {
        mConfig = config;
        mCache = mConfig.mBitmapCache;
        checkConfig();
        mImageQueue = new RequestQueue(mConfig.mThreadCount);
        mImageQueue.start();
    }

    /**
     * 关闭用于请求的线程
     */
    public void release(){
        mImageQueue.stop();
    }

    /**
     * 检查配置，做一些默认配置设置
     */
    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException(
                    "you need init a ImageLoaderConfig");
        }
        /**
         * 没有设置则默认加入队列的顺序加载
         */
        if (mConfig.mLoadPolicy == null) {
            mConfig.mLoadPolicy = new SerialPolicy();
        }
        /**
         * 没有缓存策略则不缓存
         */
        if (mCache == null) {
            mCache = new NoCache();
        }
    }

    /**
     * 显示图片的方法
     *
     * @param imageView 要显示的控件
     * @param uri       对应的Uri 可以有两种格式，后续还可以扩展。
     *  1 网络图片全路径，"https://img13.360buyimg.com/red/jfs/t16486/364/255364080/422739/aa688baf/5a2ba555Ndcf565a0.jpg",
        2 本地图片路径示例  "file:///data/data/com.example.luozhenlong.myapplication/a.JPG",
                     即file:/// + 全路径
     */
    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null, null);
    }

    /**
     * 重载
     *
     * @param imageView
     * @param uri
     * @param config
     */
    public void displayImage(ImageView imageView, String uri, DisplayConfig config) {
        displayImage(imageView, uri, config, null);
    }

    /**
     * 重载
     *
     * @param imageView
     * @param uri
     * @param listener
     */
    public void displayImage(ImageView imageView, String uri, ImageListener listener) {
        displayImage(imageView, uri, null, listener);
    }


    /**
     * 显示图片的方法
     *
     * @param imageView 控件对象
     * @param uri       图片uri
     * @param config    配置加载中显示和加载失败显示什么
     * @param listener  监听加载完毕的回调
     */
    public void displayImage(final ImageView imageView, final String uri,
                             final DisplayConfig config, final ImageListener listener) {
        BitmapRequest request = new BitmapRequest(imageView, uri, listener);
        // 添加对队列中
        mImageQueue.addRequest(request);
    }

    /**
     * 监听加载完毕的回掉
     */
    public static interface ImageListener {
        public void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

    /**
     * 停止加载
     */
    public void stop() {
        mImageQueue.stop();
    }

}
