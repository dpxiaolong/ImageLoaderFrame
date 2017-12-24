package loadPolicy;

import android.util.Log;
import android.util.TimeFormatException;

import request.BitmapRequest;

/**
 * 顺序加载策略，会根据添加的顺序进行加载
 * Created by luozhenlong on 2017/12/14.
 */

public class SerialPolicy implements LoadPolicy {
    private static final String TAG = "SerialPolicy";
    @Override
    public int compare(BitmapRequest request1, BitmapRequest request2) {
        int result = request1.serialNum - request2.serialNum;
        Log.i(TAG,"result"+result);
        if(result > 0){
            return 1;
        }else {
            return -1;
        }
    }

}
