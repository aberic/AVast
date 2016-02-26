package cn.aberic.avast.pool;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Aberic on 16/1/25 10:08
 * 邮箱：abericyang@gmail.com
 */
class AThreadFactory implements ThreadFactory {

    private final String mName;
    private final int mPriority;
    private final AtomicInteger mNumber = new AtomicInteger();

    public AThreadFactory(String name, int priority) {
        mName = name;// 线程池的名称
        mPriority = priority;//线程池的优先级
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, mName + "-" + mNumber.getAndIncrement()) {
            @Override
            public void run() {
                /// 设置线程的优先级
                Process.setThreadPriority(mPriority);
                super.run();
            }
        };
    }
}
