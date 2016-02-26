package cn.aberic.avast.imageLoader.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.imageLoader.disklrucache.DiskLruCache;
import cn.aberic.avast.imageLoader.request.BitmapRequest;
import cn.aberic.avast.imageLoader.utils.ImageViewHelper;

/**
 * 作者：Aberic on 16/2/17 19:15
 * 邮箱：abericyang@gmail.com
 */
public class DiskCache implements BitmapCache {

    private static final String TAG = "DiskCache";
    /** 磁盘缓存是否创建 **/
    private boolean mIsDiskLruCacheCreated;
    /** SD/本地 缓存总大小设定为100MB */
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 200;
    private static final int DISK_CACHE_INDEX = 0;
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private DiskLruCache mDiskLruCache;

    private static DiskCache instance;

    private DiskCache(Context context) {
        init(context);
    }

    public static DiskCache getInstance(Context context) {
        if (null == instance) {
            synchronized (DiskCache.class) {
                if (null == instance) {
                    instance = new DiskCache(context);
                }
            }
        }
        return instance;
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

    private void init(Context context) {
        long diskSize = DISK_CACHE_SIZE;
        /* 本地存储缓存路径 */
        File diskCacheDir = getDiskCacheDir(context, "AImage");
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
            mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, diskSize);
            mIsDiskLruCacheCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        if (null == mDiskLruCache) {
            return null;
        }
        Bitmap bitmap = null;
        /* 获取 DiskLruCache Snapshot 对象,即可得到磁盘缓存的输入流,并通过输入流获取 Bitmap 对象 */
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key.imageUriMD5);
            if (null != snapshot) {
                // 获取磁盘缓存输入流
                FileInputStream fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                /* 在 ImageCompressSize 中,会对图片进行缩放操作.
                 * 如果使用 FileInputStream 缩放策略,则会执行两次 decodeStream 方法.
                 * 因为 FileInputStream 是一种有序文件流,两次操作会影响文件刘的位置属性,导致第二次 decodeStream 返回 null.
                 * 因此在 ImageCompressSize 里并没有对 FileInputStream 进行缩放操作.
                 * 但可以通过文件流来得到它所对应的文件描述符,随后通过 decodeFileDescriptor 方法来加载一张缩放后的图片 */
                FileDescriptor fd = fis.getFD();// 获取 FileInputStream 文件描述符
                bitmap = AVast.obtain().util.imageCompressSize.decodeSampledBitmapFromFileDescriptor(
                        fd
                        , ImageViewHelper.getImageViewWidth(key.getImageView())
                        , ImageViewHelper.getImageViewHeight(key.getImageView()));// 压缩文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        /* 获取 DiskLruCache Editor 对象,即可编辑 DiskLruCache.
         * 如果当前不存在 Editor 对象,那么 edit() 就会返回一个新的 Editor 对象,通过它可以得到一个文件输出流.
         * 如果当前缓存正在被编辑,那么 edit() 将会返回 null ,即 DiskLruCache 不允许同时编辑一个缓存对象 */
        DiskLruCache.Editor editor;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = mDiskLruCache.edit(key.imageUriMD5);
            if (editor != null) {
            /* 由于 DiskLruCache 的 open 方法中设置了一个节点只能有一个数据,因此下面的 DISK_CACHE_INDEX 常量直接设为0 */
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (writeBitmapToDisk(key, bitmap, outputStream)) {
                    Log.d(TAG, "获取到网络中图片|" + key);
                    editor.commit();// 提交写入disk缓存
                } else {
                    editor.abort();// 如果下载中出现异常,则撤销写入,回退整个操作
                }
                AVast.obtain().util.method.closeQuietly(outputStream);
                mDiskLruCache.flush();// 刷新
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCache() {

    }

    private boolean writeBitmapToDisk(BitmapRequest request, Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
        bitmap.compress(request.format, 100, outputStream);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            AVast.obtain().util.method.closeQuietly(bos);
        }
        return result;
    }
}
