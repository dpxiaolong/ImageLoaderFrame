package cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import DiskCacheOfJakewharton.disklrucache.DiskLruCache;

import DiskCacheOfJakewharton.disklrucache.IOUtil;
import other.MD5Util;
import request.BitmapRequest;

/**
 * 硬盘缓存，主要是借用Jakewharton写的lru缓存工具
 * 实现图片缓存到硬盘，读取硬盘的图片，移除某个图片
 * 存放图片的用的是uri的md5值，不能直接用uri会报错，
 * 因为Jakewharton的工具对key做了校验证，存在/等字符会报错
 * Created by luozhenlong on 2017/12/22.
 */

public class DiskCache implements BitmapCache {
    private static final String TAG = "DiskCache";


    /**
     * jakewharton大神写的硬盘缓存
     */
    private DiskLruCache mDiskLruCache;
    /**
     *单例实例
     */
    private static DiskCache mDiskCache;
    /**
     * 缓存文件夹名字
     */
    private static final String DISK_CACHE_FILE_NAME = "DiskCache";
    /**
     * 缓存的空间大小，默认是50M
     */
    private static int mCacheSize = 500*1024*1024;

    /**
     * 私有化构造，单例
     * @param context
     */
    private DiskCache(Context context) {
        initDiskCache(context);
    }

    /**
     * 双重校验锁单利方式
     * @param context
     * @param size，缓存空间大小，以M为单位
     * @return
     */
    public static DiskCache getDiskCache(Context context,int size) {
        mCacheSize = size*1024*1024;
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(context);
                }
            }

        }
        return mDiskCache;
    }

    /**
     * 双重校验锁单利方式
     * @param context
     * @return
     */
    public static DiskCache getDiskCache(Context context) {
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(context);
                }
            }

        }
        return mDiskCache;
    }

    /**
     * 初始化硬盘缓存
     * @param context
     */
    private void initDiskCache(Context context) {
        try {
            //DISK_CACHE_FILE_NAME，可以任意取
            File cacheDir = getDiskCacheDir(context, DISK_CACHE_FILE_NAME);
            if (!cacheDir.exists()) {
                Log.i(TAG,"cacheDir is not exists");
                cacheDir.mkdirs();
            }
            //500M的默认缓存空间，可以自定义
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, getAppVersion(context), 1, mCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得软件的版本号
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 获得硬盘缓存的文件夹
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i(TAG, " dir = " + context.getExternalCacheDir());
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.i(TAG,"cachePath"+cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }


    //获取uri对应的图片
    @Override
    public Bitmap get(BitmapRequest key) {
        Log.i(TAG,"get enter:"+key.imageUri);

        // 图片解析器

        Bitmap bitmap = BitmapFactory.decodeStream(getInputStream(MD5Util.getMd5(key.imageUri)));
        return bitmap;
    }
    //根据图片Uri获得输入流
    private InputStream getInputStream(String imgUri) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(imgUri);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片写入硬盘
     * @param request
     * @param value
     */
    @Override
    public void put(BitmapRequest request, Bitmap value) {
        Log.i(TAG,"put enter:"+request.imageUri);
        DiskLruCache.Editor editor = null;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = mDiskLruCache.edit(MD5Util.getMd5(request.imageUri));
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeBitmapToDisk(value, outputStream)) {
                    // 写入disk缓存
                    editor.commit();
                } else {
                    editor.abort();
                }
                IOUtil.closeQuietly(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写图片到硬盘
     * @param bitmap
     * @param outputStream
     * @return
     */
    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            IOUtil.closeQuietly(bos);
        }
        return result;
    }

    /**
     * 移除图片的方法
     * @param key
     */
    @Override
    public void remove(BitmapRequest key) {
        try {
            mDiskLruCache.remove(MD5Util.getMd5(key.imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
