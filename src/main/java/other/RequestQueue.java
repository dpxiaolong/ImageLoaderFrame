package other;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import request.BitmapRequest;

/**
 * 图片的请求队列
 * Created by luozhenlong on 2017/12/14.
 */

public class RequestQueue {

    private static final String TAG = "other.RequestQueue";
    /**
     * 线程安全的请求队列
     */
    private static BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<BitmapRequest>();
    /**
     * 序列化生成器
     * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。适合高并发使用
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    /**
     * 线程个数,由配置类决定
     */
    private int mDispatcherNums = SimpleImageLoader.getInstance().getmConfig().mThreadCount;
    /**
     * 网络请求线程组，用于请求网络图片
     */
    private RequestDispatcher[] mDispatchers = null;

    /**
     * 构造传入
     *
     * @param threadCount 并行线程数
     */
    public RequestQueue(int threadCount) {
        mDispatcherNums = threadCount;
    }

    /**
     * 启动请求线程
     */
    private final void startDispatchers() {
        mDispatchers = new RequestDispatcher[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            mDispatchers[i] = new RequestDispatcher(mRequestQueue);
            mDispatchers[i].start();
        }
    }

    /**
     * 开始执行
     */
    public void start() {
        stop();
        startDispatchers();
    }

    /**
     * 停止执行
     */
    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].interrupt();
            }
        }
    }

    /**
     * 添加请求
     *
     * @param request
     */
    public void addRequest(BitmapRequest request) {
        if (!mRequestQueue.contains(request)) {
            //为请求添加序列号
            request.serialNum = this.generateSerialNumber();
            Log.i(TAG,"addRequest request serialNum"+request.serialNum);
            mRequestQueue.add(request);
        } else {
            Log.e(TAG, "a same requeast is added");
        }
    }

    /**
     * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。适合高并发使用
     *
     * @return 这里相当于int a； a++；
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }

    /**
     * 清楚请求队列的所有i请求
     */
    public void clear() {
        mRequestQueue.clear();
    }


}
