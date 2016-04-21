package cn.aberic.avast.util;

import android.os.Handler;
import android.os.Message;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时封装器
 * 作者：Aberic on 16/3/1 10:26
 * 邮箱：abericyang@gmail.com
 */
public class TimerUtil {

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    /** 无参构造方法，创建的线程不为主线程，则主线程结束后，timer自动结束，而无需使用cancel来完成对timer的结束 */
    public TimerUtil() {
        timer = new Timer();
        initTimerTask();
    }

    /**
     * 传入了是否为后台线程，后台线程当且仅当进程结束时，自动注销掉
     *
     * @param isDaemon
     *         是否为后台线程
     */
    public TimerUtil(boolean isDaemon) {
        timer = new Timer(isDaemon);
        initTimerTask();
    }

    /**
     * 传入名称和是否为后台线程将timer启动
     *
     * @param name
     *         名称
     * @param isDaemon
     *         是否为后台线程
     */
    public TimerUtil(String name, boolean isDaemon) {
        timer = new Timer(name, isDaemon);
        initTimerTask();
    }

    private void initTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(1));
            }
        };
    }

    public void monitorTimer(final TimerListener listener) {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        listener.onTimerListener();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 启动 Timer
     *
     * @param delay
     *         延迟多久后启动(毫秒)
     * @param period
     *         启动后定时间隔(毫秒)
     */
    public void start(long delay, long period) {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendMessage(handler.obtainMessage(1));
                }
            };
        }
        timer.schedule(timerTask, delay, period);
    }

    /**
     * 启动 Timer
     *
     * @param when
     *         首次启动时间
     * @param period
     *         启动后定时间隔
     */
    public void start(Date when, long period) {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendMessage(handler.obtainMessage(1));
                }
            };
        }
        timer.schedule(timerTask, when, period);
    }

    public void stop() {
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean isStop() {
        if (null == timer) {
            stop();
            return true;
        }
        if (null == timerTask) {
            stop();
            return true;
        }
        return false;
    }

    public interface TimerListener {
        void onTimerListener();
    }


}
