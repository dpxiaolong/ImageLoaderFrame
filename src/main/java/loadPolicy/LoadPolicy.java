package loadPolicy;

import request.BitmapRequest;

/**
 * 加载策略
 * Created by luozhenlong on 2017/12/14.
 */

public interface LoadPolicy {
    public int compare(BitmapRequest request1, BitmapRequest request2);
}
