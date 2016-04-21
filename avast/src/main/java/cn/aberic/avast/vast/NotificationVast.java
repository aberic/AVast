package cn.aberic.avast.vast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;

import java.io.File;

import cn.aberic.avast.R;
import cn.aberic.avast.core.AVast;

/**
 * 通知栏工具类
 */
public class NotificationVast {

    /** 下拉菜单栏普通显示id **/
    public static final int NOTIFICATION_NORMAL = 0;
    /** 下拉菜单栏大布局Text类型显示id **/
    public static final int NOTIFICATION_BIG_TEXT = 1;
    /** 下拉菜单栏大布局Picture显示id **/
    public static final int NOTIFICATION_BIG_PICTURE = 2;
    /** 常驻下拉菜单栏下载用id **/
    public static final int NOTIFICATION_DOWNLOAD = 9;
    /** 常驻下拉菜单栏应用id **/
    public static final int NOTIFICATION_APP = 10;

    private NotificationManager manager;
    private Bitmap icon;
    private Context mContext;

    public NotificationVast(Context context) {
        mContext = context;
        // 获取通知服务
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
    }

    /**
     * 普通notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param messageNum
     *         设置信息条数
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     * @param when
     *         设置消息时间
     * @param action
     *         单击后跳转的类class--Activity
     * @param id
     *         下拉菜单栏普通显示id
     */
    public void showNormal(String ticker, String contentTitle, String contentText, String contentInfo, int messageNum, boolean autoCancel, long when, String action, int id) {
        // 实例化Intent
        Intent intent = new Intent(action);
        // 获得PendingIntent
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                .setNumber(messageNum).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).setContentIntent(pi)
                .build();
        manager.notify(id, notification);
    }

    /**
     * 普通notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param messageNum
     *         设置信息条数
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     * @param when
     *         设置消息时间
     * @param action
     *         单击后跳转的类class--Activity
     */
    public void showNormal(String ticker, String contentTitle, String contentText, String contentInfo, int messageNum, boolean autoCancel, long when, String action) {
        // 实例化Intent
        Intent intent = new Intent(action);
        // 获得PendingIntent
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                .setNumber(messageNum).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).setContentIntent(pi)
                .build();
        manager.notify(NOTIFICATION_NORMAL, notification);
    }

    /**
     * 普通notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param messageNum
     *         设置信息条数
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     * @param when
     *         设置消息时间
     * @param apk
     *         将要安装的 apk 文件
     */
    public void showNormal(String ticker, String contentTitle, String contentText, String contentInfo, int messageNum, boolean autoCancel, long when, File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(null!=apk){
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
            // 获得PendingIntent
            PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                    .setNumber(messageNum).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).setContentIntent(pi)
                    .build();
            manager.notify(NOTIFICATION_NORMAL, notification);
        } else {
            manager.cancel(NOTIFICATION_NORMAL);
            AVast.obtain().util.toast.showShort("文件下载失败");
        }
    }

    /**
     * 普通notification，含bundle
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param messageNum
     *         设置信息条数
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     * @param when
     *         设置消息时间
     * @param action
     *         单击后跳转的类class--Activity
     * @param bundle
     *         传递给对应界面内容，可以为空
     */
    public void showNormal(String ticker, String contentTitle, String contentText, String contentInfo, int messageNum, boolean autoCancel,
                           long when, String action, Bundle bundle) {
        // 实例化Intent
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        // 获得PendingIntent
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                .setNumber(messageNum).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).setContentIntent(pi)
                .build();
        manager.notify(NOTIFICATION_NORMAL, notification);
    }

    /**
     * 大布局Text类型notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param bigContentTitle
     *         设置大标题?
     * @param bigText
     *         设置标题下详细内容
     * @param summaryText
     *         设置在细节区域底端添加一行文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     */
    public void showBigViewText(String ticker, String contentTitle, String contentText, String bigContentTitle, String bigText,
                                String summaryText, String contentInfo, boolean autoCancel, long when) {
        BigTextStyle textStyle = new BigTextStyle();
        textStyle.setBigContentTitle(bigContentTitle).setSummaryText(summaryText).bigText(bigText);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                .setStyle(textStyle).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).build();
        manager.notify(NOTIFICATION_BIG_TEXT, notification);
    }

    /**
     * 大布局Picture类型notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param bigContentTitle
     *         设置大标题?
     * @param summaryText
     *         设置在细节区域底端添加一行文本
     * @param contentInfo
     *         设置附加内容（比如显示条数等，一般为空）
     * @param autoCancel
     *         点击之后是否自动消失（一般为true）
     * @param bitmap
     *         Picture
     */
    public void showBigViewPic(String ticker, String contentTitle, String contentText, String bigContentTitle, String summaryText,
                               String contentInfo, boolean autoCancel, Bitmap bitmap, long when) {
        BigPictureStyle pictureStyle = new BigPictureStyle();
        pictureStyle.setBigContentTitle(bigContentTitle).setSummaryText(summaryText).bigPicture(bitmap);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker).setContentInfo(contentInfo).setContentTitle(contentTitle).setContentText(contentText)
                .setStyle(pictureStyle).setAutoCancel(autoCancel).setDefaults(Notification.DEFAULT_ALL).setWhen(when).build();
        manager.notify(NOTIFICATION_BIG_PICTURE, notification);
    }

    /**
     * 下载（进度条）类型notification
     *
     * @param ticker
     *         第一次提示消息的时候显示在通知栏上
     * @param contentTitle
     *         设置内容标题
     * @param contentText
     *         设置内容文本
     * @param max
     *         进度最大值（一般为100）
     * @param progress
     *         当前进度值
     */
    public void showProgress(String ticker, String contentTitle, String contentText, int max, int progress) {
        Notification notification = new NotificationCompat.Builder(mContext).setSmallIcon(R.mipmap.ic_launcher).setTicker(ticker)
                .setContentTitle(contentTitle).setContentText(contentText).setOngoing(true).setProgress(max, progress, false).build();
        manager.notify(NOTIFICATION_DOWNLOAD, notification);
    }

    /**
     * 添加常驻notification
     *
     * @param path
     *         本地头像路径
     * @param action
     *         单击后跳转的类class--Activity
     * @param ico
     *         小图标资源id
     */
    public void addOnGoing(String path, String action, String title, String content, int ico) {
        if (new File(path).exists()) {// 如果本地头像存在，则通过网络重新获取
            icon = AVast.obtain().util.bitmap.convertToBitmap(path, 100, 100);
        }
        // 实例化Intent
        Intent intent = new Intent(action);
        // 获得PendingIntent
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext).setLargeIcon(icon).setSmallIcon(ico)
                .setContentTitle(title).setContentText(content)
                .setOngoing(true).setContentIntent(pi).build();
        manager.notify(NOTIFICATION_APP, notification);
    }

    /**
     * 移除常驻notification
     *
     * @param id
     *         notification自维护id
     */
    public void removeOnGoing(int id) {
        manager.cancel(id);
    }

}
