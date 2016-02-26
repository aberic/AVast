package cn.aberic.avast.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.aberic.avast.core.AVast;

/**
 * 一般通用公共方法类
 *
 * @author Aberic
 */
public class MethodVast {

    private Context mContext;

    public MethodVast(Context context) {
        this.mContext = context;
    }

    /**
     * 返回当前程序版本名
     */
    public String VersionName() {
        String ReturnValue = "";
        try {
            ReturnValue = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (Exception e) {
            ReturnValue = "";
        }
        return ReturnValue;
    }

    /**
     * 返回当前程序版本号
     */
    public int VersionCode() {
        int ReturnValue = 0;
        try {
            ReturnValue = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            ReturnValue = 0;
        }
        return ReturnValue;
    }

    /**
     * 安装apk
     * <p/>
     * Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法。如果使用
     * Activity的startActivity方法
     * ，不会有任何限制，而如果使用Context的startActivity方法的话，就需要开启一个新的task ，遇到（Calling
     * startActivity() from outside of an Activity context requires the
     * FLAG_ACTIVITY_NEW_TASK flag. Is this really what you
     * want?）异常的，都是因为使用了Context的startActivity方法。解决办法是，加一个flag
     *
     * @param file
     *         apk文件
     */
    public void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    /**
     * 得到当前时区+08 -02之类
     */
    public String[] getTimeZoon() throws Exception {
        String TimeZ[] = new String[2];
        long ltime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String localTime, rawTime;
        // 获得系统时间
        ltime = System.currentTimeMillis();
        // 直接取得的是当地时区时间，如果是中国的就是GMT+8
        localTime = sdf.format(ltime);
        String format = "yyyy-MM-dd HH:mm:ss";
        // 要设置为标准时间就要对时区做设置
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+00:00"));
        // 再取出就是标准时间了
        rawTime = sdf.format(ltime);
        TimeZ[0] = String
                .valueOf(AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                        - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format)) > 12 ? String.valueOf(
                        AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                                - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format)) - 24)
                        .length() < 3 ? String.valueOf(AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                        - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format))
                        - 24).replace("-", "-0") : AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                        - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format)) - 24 : String.valueOf(
                        AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                                - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format))).length() < 2 ? "+0"
                        + (AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                        - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format))) : "+"
                        + (AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                        - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format))));
        TimeZ[1] = String.valueOf(AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format)) > 12
                ? AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format)) - 24
                : (AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(localTime, format))
                - AVast.obtain().util.date.getHour(AVast.obtain().util.date.parseStringToDate(rawTime, format))));
//        Log.i("PublicCls", "localTime=" + AVast.obtain().date.getHour(AVast.obtain().date.parseStringToDate(localTime, format)));
//        Log.i("PublicCls", "rawTime=" + AVast.obtain().date.getHour(AVast.obtain().date.parseStringToDate(rawTime, format)));
//        Log.i("PublicCls", TimeZ[0]);
//        Log.i("PublicCls", TimeZ[1]);
        return TimeZ;
    }

    /**
     * 是否为双卡手机
     *
     * @return boolean
     */
    public boolean isDoublePhone() {
        boolean isDouble = true;
        Method method = null;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 只要在反射getSimStateGemini 这个函数时报了错就是单卡手机
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]{int.class});
            method.invoke(tm, new Object[]{new Integer(0)});
            method.invoke(tm, new Object[]{new Integer(1)});
            isDouble = true;
        } catch (Exception e) {
            isDouble = false;
            e.printStackTrace();
        }
        return isDouble;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     *         上下文
     * @param className
     *         判断的服务名字
     *
     * @return true 在运行 false 不在运行
     */
    public boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取屏幕分辨率_宽（像素）
     */
    public int GetScreenWidthPixels() {
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕分辨率_高（像素）
     */
    public int GetScreenHeightPixels() {
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.heightPixels;
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mContext.getResources().getDisplayMetrics());
    }

    /**
     * 获取手机imei码
     *
     * @return 手机imei码
     */
    public String GetIMEI() {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
            imei = "";
        }
        return imei;
    }

    /**
     * 获取手机号码
     *
     * @return 手机号码
     */
    public String GetTelNumber() {
        String ReturnValue;
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ReturnValue = tm.getLine1Number();
        } catch (Exception e) {
            ReturnValue = "";
        }
        return ReturnValue;
    }

    /**
     * 获取手机simcode
     *
     * @return 手机simcode
     */
    public String GetSimSerialNumber() {
        String ReturnValue;
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ReturnValue = tm.getSimSerialNumber();
        } catch (Exception e) {
            ReturnValue = "";
        }
        return ReturnValue;
    }

    /**
     * 获取手机IMSI
     *
     * @return 手机IMSI
     */
    public String GetIMSI() {
        String ReturnValue;
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ReturnValue = tm.getSubscriberId();
        } catch (Exception e) {
            ReturnValue = "";
        }
        return ReturnValue;
    }

    /**
     * 发送短信
     *
     * @param _Tel
     *         电话号码
     * @param _Content
     *         内容
     */
    public void SendSMS(String _Tel, String _Content) {
        try {
            PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(), 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(_Tel, null, _Content, sentIntent, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 关闭 Closeable 对象
     *
     * @param closeable
     *         Closeable对象
     */
    public void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
