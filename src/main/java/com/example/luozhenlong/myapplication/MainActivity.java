package com.example.luozhenlong.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import cache.DiskAndMemoryCache;
import cache.DiskCache;
import cache.MemoryCache;
import config.ImageLoaderConfig;
import loadPolicy.ReversePolicy;
import loadPolicy.SerialPolicy;
import other.SimpleImageLoader;

public class MainActivity extends AppCompatActivity {

    //请求权限返回码code
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 400;

    private static final String TAG = "MainActivity";
    private ImageView mImageview;
    private GridView mGridView;
    /**
     * 用于测试的图片路径，有网络图片和本地图片两种
     */
    public static String[] imagePath = {
            "file:///data/datdsa/com.example.luozhenlong.myapplication/a.JPG",
            "file:///data/data/com.example.luozhenlong.myapplication/3.gif",
            "file:///data/data/com.example.luozhenlong.myapplication/4.gif",
            "https://www.basdfidu.com/img/bd_logo1.png",
            "https://img.alicdn.com/tfs/TB1MaLKRXXXXXaWXFXXXXXXXXXX-480-260.png",
            "https://misc.360buyimg.com/lib/img/e/logo-201305-b.png",
            "https://p4.ssl.qhimg.com/t01a334284ab2c07df4.png",
            "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg",
            "https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=a29308178e94a4c20a23e02d36cf7ce8/5ab5c9ea15ce36d3a1226a2833f33a87e950b180.jpg",
            "file:///data/data/com.example.luozhenlong.myapplication/a.JPG",
            "file:///data/data/com.example.luozhenlong.myapplication/3.gif",
            "file:///data/data/com.example.luozhenlong.myapplication/4.gif",
            "https://www.baidu.com/img/bd_logo1.png",
            "https://misc.360buyimg.com/lib/img/e/logo-201305-b.png",
            "https://img10.360buyimg.com/cms/jfs/t4783/203/1316047608/32612/9cb52708/58ef5e86Nc9196bde.png!q80.webp",
            "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg",
            "https://img13.360buyimg.com/red/jfs/t16486/364/255364080/422739/aa688baf/5a2ba555Ndcf565a0.jpg",
            "https://www.basdfidu.com/img/bd_logo1.png",
            "https://misc.360buyimg.com/lib/img/e/logo-201305-b.png",
            "https://img10.360buyimg.com/cms/jfs/t4783/203/1316047608/32612/9cb52708/58ef5e86Nc9196bde.png!q80.webp",
            "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg",
            "https://img13.360buyimg.com/red/jfs/t16486/364/255364080/422739/aa688baf/5a2ba555Ndcf565a0.jpg",
            "file:///data/data/com.example.luozhenlong.myapplication/a.JPG",
            "file:///data/data/com.example.luozhenlong.myapplication/3.gif",
            "file:///data/data/com.example.luozhenlong.myapplication/4.gif",
            "https://www.baidu.com/img/bd_logo1.png",
            "https://misc.360buyimg.com/lib/img/e/logo-201305-b.png",
            "https://img10.360buyimg.com/cms/jfs/t4783/203/1316047608/32612/9cb52708/58ef5e86Nc9196bde.png!q80.webp",
            "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg",
            "https://img13.360buyimg.com/red/jfs/t16486/364/255364080/422739/aa688baf/5a2ba555Ndcf565a0.jpg",
            "https://www.basdfidu.com/img/bd_logo1.png",
            "https://misc.360buyimg.com/lib/img/e/logo-201305-b.png",
            "https://img10.360buyimg.com/cms/jfs/t4783/203/1316047608/32612/9cb52708/58ef5e86Nc9196bde.png!q80.webp",
            "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg",
            "https://img13.360buyimg.com/red/jfs/t16486/364/255364080/422739/aa688baf/5a2ba555Ndcf565a0.jpg",
            "file:///data/data/com.example.luozhenlong.myapplication/a.JPG",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageview = (ImageView) findViewById(R.id.iv_test);


        //第一步：先检查运行时存储权限，没有就申请。
        checkStoragePermission();
        //第二步：初始化loader的配置
        ImageLoaderConfig imageLoaderConfig = new ImageLoaderConfig()
                //设置加载失败的默认图片
                .setFailImage(R.drawable.fail)
                //设置加载中的默认图片
                .setLoadingImage(R.drawable.loading)
                //设置缓存策略
                .setCache(new DiskAndMemoryCache(this))
                //设置图片加载策略，先加入的请求需要后执行
                .setLoadPolicy(new ReversePolicy())
                //设置执行任务进程数量，建议不要大于处理器核数
                //因为在处理器核数以内效率是增长，大于后由于存在任务调度等消耗，效率反而会下降
                .setThreadCount(4);
        //第三步：初始化加载器
        SimpleImageLoader.getInstance().init(imageLoaderConfig);
        //网络图片路径直接Uri即可
        String pathTest = "https://img12.360buyimg.com/red/jfs/t14662/206/484370755/204243/be01c580/5a2f8866N2a94ffe8.jpg";
        //本地图片示例，需要自己加上schema前缀file://
//        String pathTest2 = "file:///data/data/com.example.luozhenlong.myapplication/a.JPG";
        //第四步：加载图片
        SimpleImageLoader.getInstance().displayImage(mImageview, pathTest);


        //初始化控件
        initGridView();

    }

    @Override
    protected void onDestroy() {
        //关闭线程资源
        SimpleImageLoader.getInstance().release();
        super.onDestroy();
    }

    /**
     * 初始化gridview和数据。
     */
    public void initGridView() {
        Log.i(TAG, "initGridView enter");
        mGridView = (GridView) findViewById(R.id.gv_test);
        mGridView.setAdapter(new MyAdapter());

    }

    /**
     * GridView的adapter
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imagePath.length;
        }

        @Override
        public Object getItem(int position) {
            return getView(position, null, null);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.grid_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Log.i(TAG, "imagePath[position]" + imagePath[position]);
            SimpleImageLoader.getInstance().displayImage(imageView, imagePath[position], new SimpleImageLoader.ImageListener() {
                @Override
                public void onComplete(ImageView imageView, Bitmap bitmap, String uri) {
                    Log.i(TAG, "onComplete enter");
                    imageView.setImageBitmap(bitmap);
                }
            });

            return view;
        }
    }

    /**
     * 检查运行时权限，存储权限
     */
    public void checkStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "checkStoragePermission enter");

                    String file = "data/data/com.example.luozhenlong.myapplication/a.JPG";
                    Bitmap bitmap = BitmapFactory.decodeFile(file);
                    Log.i(TAG, "bitmap " + bitmap);
                    mImageview.setImageBitmap(bitmap);

                } else {
                }
                return;
            }
        }

    }
}
