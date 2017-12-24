package other;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import loader.Loader;
import loader.LocalLoader;
import loader.NullLoader;
import loader.UrlLoader;

/**
 * Created by luozhenlong on 2017/12/14.
 */

public class LoaderManager {

    private static final String TAG = "LoaderManager";

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";

    private NullLoader mNullLoader = new NullLoader();
    //单例的实例
    private static LoaderManager mInstance;
    //存loader的容器，可以根据
    private Map<String, Loader> mLoaderMap = new HashMap<String, Loader>();

    /**
     * 初始化剂加载好Loader，后面可以直接获取使用
     */
    private LoaderManager() {
        register(HTTPS, new UrlLoader());
        register(HTTP, new UrlLoader());
        register(FILE, new LocalLoader());
    }

    /**
     * 单例，双重校验锁方式
     *
     * @return
     */
    public static LoaderManager getInstance() {
        if (mInstance == null) {
            synchronized (LoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new LoaderManager();
                }
            }
        }
        return mInstance;
    }

    public final synchronized void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            Log.i(TAG,"schema :"+schema);
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }


}
