package cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import request.BitmapRequest;

/**
 * 缓存，同时具有内存缓存和硬盘缓存的缓存类
 * Created by luozhenlong on 2017/12/24.
 */

public class DiskAndMemoryCache implements BitmapCache {
    private static final String TAG = "DiskAndMemoryCache";
    private Context mContext;
    //硬盘缓存实例
    private final DiskCache mDiskCache;
    //内存缓存实例,-1表示默认
    private MemoryCache mMemoryCache = new MemoryCache(-1);


    public DiskAndMemoryCache(Context context){
        mContext =context;
        mDiskCache = DiskCache.getDiskCache(context);
    }
    @Override
    public Bitmap get(BitmapRequest key) {
        //先从内存取
        Bitmap bitmap = mMemoryCache.get(key);
        if(null == bitmap){
            //没有取到从硬盘取
            bitmap = mDiskCache.get(key);
            if(null != bitmap)
            {
                Log.i(TAG,"cache data from disk to memory :"+key.imageUri);
                //取到后缓存到内存
                mMemoryCache.put(key, bitmap);
            }
        }

        return bitmap;
    }

    @Override
    public void put(BitmapRequest key, Bitmap value) {
        Log.i(TAG,"put into memory and disk"+key.imageUri);
        mMemoryCache.put(key,value);
        mDiskCache.put(key,value);

    }

    @Override
    public void remove(BitmapRequest key) {
        mMemoryCache.remove(key);
        mDiskCache.remove(key);

    }
}
