package loadPolicy;

import android.util.Log;

import request.BitmapRequest;

/**
 * 倒顺加载，后添加的先加载，这里存在一个问题，添加的同时在取出
 * 会显得并没有倒序，但是实际是实时的倒序加载
 * 举例，添加1，2，3，4567
 * Created by luozhenlong on 2017/12/16.
 */

public class ReversePolicy implements LoadPolicy {
    @Override
    public int compare(BitmapRequest request1, BitmapRequest request2) {
        int result = request1.serialNum - request2.serialNum;
        if (result > 0) {
            return -1;
        } else {
            return 1;
        }
    }

}

