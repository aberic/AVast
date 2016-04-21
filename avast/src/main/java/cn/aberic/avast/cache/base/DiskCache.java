package cn.aberic.avast.cache.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

import cn.aberic.avast.cache.disklrucache.DiskLruCache;
import cn.aberic.avast.core.AVast;

/**
 * 作者：Aberic on 16/2/17 19:15
 * 邮箱：abericyang@gmail.com
 */
public class DiskCache {

    // private static final String TAG = "DiskCache";
    /** 磁盘缓存是否创建 **/
    private boolean mIsDiskLruCacheCreated;
    /** SD/本地 缓存总大小设定为100MB */
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 200;
    public DiskLruCache mDiskLruCache;

    private static DiskCache diskCache;

    public static DiskCache obtain() {
        if (null == diskCache) {
            synchronized (DiskCache.class) {
                if (null == diskCache) {
                    diskCache = new DiskCache();
                }
            }
        }
        return diskCache;
    }

    public boolean isDiskLruCacheCreated() {
        return mIsDiskLruCacheCreated;
    }

    /**
     * 磁盘缓存在文件系统中的存储路径.
     * 缓存路径可选择SD卡上的缓存目录,具体是指 /sdcard/Android/data/package_name/cache 目录.
     * 当应用被卸载时,此目录也会一并删除.
     *
     * @param context
     *         上下文
     * @param uniqueName
     *         目录唯一名
     *
     * @return File（目录路径）
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        /* 外部存储是否可用 */
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;// 外部缓存路径
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 根据传入路径查询可用字节数
     * <p/>
     * 获取手机内部可用空间大小
     *
     * @param path
     *         查询路径
     *
     * @return 可用字节数
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            /* java.io.File.getUsableSpace() 方法返回可用字节数，
             * 这个虚拟机上的分区是此抽象名字。
             * 这种方法通常会提供多少新的数据实际上可以写成这种方法检查写权限和其他操作系统的限制，以更准确的估计。*/
            return path.getUsableSpace();
        }
        /* 在存储文件时，为了保证有充足的存储空间大小，通常需要知道系统内部或者sdcard的剩余存储空间大小，这里就需要用到StatFs类.
         * StatFs 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况 */
        final StatFs statFs = new StatFs(path.getPath());
        /* 获取内容
         * 内容1: 获取该区域可用的文件系统数
         * 内容2: 获取当前可用的存储空间 */
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    public void init(Context context) {
        long diskSize = DISK_CACHE_SIZE;
        /* 本地存储缓存路径 */
        File diskCacheDir = getDiskCacheDir(context, "AVast");
        if (!diskCacheDir.exists()) {// 判断本地缓存文件夹是否存在,否则创建
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) < diskSize) {
            diskSize = getUsableSpace(diskCacheDir) / 2 > 1024 * 1024 * 10 ? diskSize : 0;
        }
        if (diskSize == 0) {
            mIsDiskLruCacheCreated = false;
            return;
        }
        try {
            /* 参数1:directory 磁盘缓存在文件系统中的存储路径;
             * 参数2:appVersion 应用版本号,一般设为1即可.当版本号发生变化时,DiskLruCache 会清空之前的缓存文件;
             * 参数3:valueCount 单个节点所对应的数据的个数,一般设为1即可;
             * 参数4:maxSize 缓存的总大小.当缓存大小超出设定值后,DiskLruCache 会清除一些缓存以保证总大小不大于这个设定值;
             */
            mDiskLruCache = DiskLruCache.open(diskCacheDir, AVast.obtain().util.method.getVersionCode(), 1, diskSize);
            mIsDiskLruCacheCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        if (mDiskLruCache.size() > 0) {
            try {
                // 这个方法用于将所有的缓存数据全部删除,会删除包括日志文件在内的所有文件
                mDiskLruCache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
