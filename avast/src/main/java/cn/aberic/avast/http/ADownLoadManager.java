package cn.aberic.avast.http;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import cn.aberic.avast.R;
import cn.aberic.avast.core.AVast;
import cn.aberic.avast.util.NotificationVast;

public class ADownLoadManager {

    /**
     * 从服务器下载apk
     *
     * @param context
     *         上下文
     * @param path
     *         连接地址
     * @param dirPath
     *         输出目录
     * @param name
     *         输出文件名
     *
     * @return File
     *
     * @throws Exception
     */
    public static File getFileFromServer(Context context, String path, String dirPath, String name) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (AVast.obtain().util.sdCard.isAvailable()) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            // 获取通知服务
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("未来开始下载").setContentTitle("未来正在下载").setContentText("当前下载进度……").setOngoing(true);
            // 获取到文件的大小
            builder.setProgress(conn.getContentLength(), 0, false);
            manager.notify(NotificationVast.NOTIFICATION_DOWNLOAD, builder.build());

            InputStream is = conn.getInputStream();
            File file = new File(dirPath, name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            long first = new Date().getTime();
            long second = 0l;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                second = new Date().getTime();
                if (second - first > 500) {
                    // 获取当前下载量
                    builder.setContentText("当前下载进度" + total / 1024 + "K/" + conn.getContentLength() / 1024 + "K").setProgress(
                            conn.getContentLength(), total, false);
                    manager.notify(NotificationVast.NOTIFICATION_DOWNLOAD, builder.build());
                    first = second;
                }
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 下载apk
     *
     * @param urlPath
     *         下载地址
     * @param notificationInfo
     *         通知
     * @param dirPath
     *         输出目录
     * @param name
     *         输出文件名
     *
     * @return
     */
    public File downloadApk(Context context, String urlPath, NotificationVast notificationInfo, String dirPath, String name) {
        try {
            File file = ADownLoadManager.getFileFromServer(context, urlPath, dirPath, name);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
