package cn.aberic.avast.cache.impl;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.base.DiskCache;
import cn.aberic.avast.cache.disklrucache.DiskLruCache;
import cn.aberic.avast.cache.request.BitmapCacheRequest;
import cn.aberic.avast.imageLoader.utils.ImageViewHelper;

/**
 * 作者：Aberic on 16/2/29 13:27
 * 邮箱：abericyang@gmail.com
 */
public class BitmapDiskCache implements BaseCache<Bitmap> {

    private static final int DISK_CACHE_INDEX = 0;
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    @Override
    public Bitmap get(CacheRequest key) {
        if (null == DiskCache.obtain().mDiskLruCache) {
            return null;
        }
        Bitmap bitmap = null;
        /* 获取 DiskLruCache Snapshot 对象,即可得到磁盘缓存的输入流,并通过输入流获取 Bitmap 对象 */
        try {
            DiskLruCache.Snapshot snapshot = DiskCache.obtain().mDiskLruCache.get(((BitmapCacheRequest) key).imageUriMD5);
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
                        , ImageViewHelper.getImageViewWidth(((BitmapCacheRequest) key).getImageView())
                        , ImageViewHelper.getImageViewHeight(((BitmapCacheRequest) key).getImageView()));// 压缩文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void put(CacheRequest key, Bitmap bitmap) {
        /* 获取 DiskLruCache Editor 对象,即可编辑 DiskLruCache.
         * 如果当前不存在 Editor 对象,那么 edit() 就会返回一个新的 Editor 对象,通过它可以得到一个文件输出流.
         * 如果当前缓存正在被编辑,那么 edit() 将会返回 null ,即 DiskLruCache 不允许同时编辑一个缓存对象 */
        DiskLruCache.Editor editor;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = DiskCache.obtain().mDiskLruCache.edit(((BitmapCacheRequest) key).imageUriMD5);
            if (editor != null) {
            /* 由于 DiskLruCache 的 open 方法中设置了一个节点只能有一个数据,因此下面的 DISK_CACHE_INDEX 常量直接设为0 */
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (writeBitmapToDisk(((BitmapCacheRequest) key), bitmap, outputStream)) {
                    Log.d("BitmapDiskCache", "获取到网络数据|" + key);
                    editor.commit();// 提交写入disk缓存
                } else {
                    editor.abort();// 如果下载中出现异常,则撤销写入,回退整个操作
                }
                AVast.obtain().util.method.closeQuietly(outputStream);
                DiskCache.obtain().mDiskLruCache.flush();// 刷新
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToDisk(BitmapCacheRequest request, Bitmap bitmap, OutputStream outputStream) {
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
