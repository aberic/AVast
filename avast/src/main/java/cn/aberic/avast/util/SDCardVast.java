package cn.aberic.avast.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * SD卡工具箱
 */
public class SDCardVast {

    /**
     * 获取SD卡的状态
     */
    public String getState() {
        return Environment.getExternalStorageState();
    }


    /**
     * SD卡是否可用
     *
     * @return 只有当SD卡已经安装并且准备好了才返回true
     */
    public boolean isAvailable() {
        return getState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡的根目录
     *
     * @return null：不存在SD卡
     */
    public File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }


    /**
     * 获取SD卡的根路径
     *
     * @return null：不存在SD卡
     */
    public String getRootPath() {
        File rootDirectory = getRootDirectory();
        return rootDirectory != null ? rootDirectory.getPath() : null;
    }

    /**
     * 获取sd卡路径
     *
     * @return Stringpath
     */
    public String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            sdDir = context.getFilesDir();
        }
        return sdDir.getAbsolutePath();
    }

    /**
     * 获取文件本地缓存路径
     *
     * @param context
     *         上下文
     *
     * @return 路径(Path)
     */
    public String getSDCachePath(Context context) {
        if (isAvailable()) {// 外部存储是否可用
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }
}
