package cn.aberic.avast.pool;

import android.os.Process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * 作者：Aberic on 16/1/25 09:53
 * 邮箱：abericyang@gmail.com
 */
public class AThreadPool {

    /** 总共多少任务（根据CPU个数决定创建活动线程的个数,这样取的好处就是可以让手机承受得住） */
    // private static final int count = Runtime.getRuntime().availableProcessors() * 3 + 2;

    /** 每次只执行一个任务的线程池 */
    // private static ExecutorService singleTaskExecutor = null;

    /** 每次执行限定个数个任务的线程池 */
    // private static ExecutorService limitedTaskExecutor = null;

    /** 所有任务都一次性开始的线程池 */
    // private static ExecutorService allTaskExecutor = null;

    /** 创建一个可在指定时间里执行任务的线程池，亦可重复执行 */
    // private static ExecutorService scheduledTaskExecutor = null;

    /** 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式） */
    // private static ExecutorService scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(3, new ThreadFactoryTest());

    private final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();// 核心线程数为当前设备CPU核心数+1
    private final static int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;// 最大线程数为当前设备CPU核心数的2N+1
    private final static long KEEP_ALIVE_TIME = 60L;// 设置线程的存活时间（闲置超时时长）
    /**
     * 核心线程池.线程数量固定,且只有核心线程.当线程处于闲置状态时,它们不会被回收,除非线程池关闭.
     * 当所有线程都处于活动状态时,新任务就会处于等待状态,直到有线程空闲出来.
     * 该线程池因只有核心线程且核心线程不会被回收,因此可以快速响应外部请求.
     * 另:核心线程没有超时机制,队列也没有大小限制.
     **/
    private final ExecutorService mFixedThreadPool;
    /**
     * 非核心线程池.线程数量不固定,且只有非核心线程,并且最大线程数为 Integer.MAX_VALUE ,即任意大.
     * 当线程池中所有线程都处于活动状态时,会创建新的线程来处理新任务,否则就会利用空闲线程来处理新任务.
     * 线程中的闲置线程都有超时机制,超时时长为60s,超时则回收.
     * 该线程适合执行大量的耗时较少的任务,当整个线程池被闲置下来时,线程池中的线程都会因超时而停止,此时没有任何线程,几乎不占用任何系统资源.
     **/
    private final ExecutorService mCachedThreadPool;

    public AThreadPool() {
        // 创建核心线程池工厂
        ThreadFactory mFixedFactory = new AThreadFactory("fixed_thread_pool", Process.THREAD_PRIORITY_BACKGROUND);
        // 创建非核心线程池工厂
        ThreadFactory mCacheFactory = new AThreadFactory("cache_thread_pool", Process.THREAD_PRIORITY_DISPLAY);
        // mFixedThreadPool = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        mFixedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), mFixedFactory);
        // mCachedThreadPool = Executors.newCachedThreadPool();
        mCachedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), mCacheFactory);
    }

    /**
     * 执行核心线程池
     *
     * @param runnable
     *         runnable
     */
    public void submitFixed(Runnable runnable) {
        mFixedThreadPool.execute(runnable);
    }

    /**
     * 执行非核心线程池
     *
     * @param runnable
     *         runnable
     */
    public void submitCached(Runnable runnable) {
        // Log.d(TAG, "执行新的非核心线程");
        mCachedThreadPool.execute(runnable);
    }
}
