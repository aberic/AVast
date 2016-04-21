package cn.aberic.avast.cache.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import cn.aberic.avast.core.AVast;
import cn.aberic.avast.cache.base.BaseCache;
import cn.aberic.avast.cache.request.CacheRequest;
import cn.aberic.avast.cache.request.StringCacheRequest;
import cn.aberic.avast.cache.base.DiskCache;
import cn.aberic.avast.cache.disklrucache.DiskLruCache;

/**
 * 作者：Aberic on 16/2/29 13:27
 * 邮箱：abericyang@gmail.com
 */
public class StringDiskCache implements BaseCache<String> {

    private static final int DISK_CACHE_INDEX = 0;
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    @Override
    public String get(CacheRequest key) {
        if (null == DiskCache.obtain().mDiskLruCache) {
            return null;
        }
        /* 获取 DiskLruCache Snapshot 对象,即可得到磁盘缓存的输入流,并通过输入流获取 String 对象 */
        try {
            DiskLruCache.Snapshot snapshot = DiskCache.obtain().mDiskLruCache.get(((StringCacheRequest) key).uriMd5);
            if (null != snapshot) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader responseReader;

                BufferedInputStream bis = new BufferedInputStream(snapshot.getInputStream(DISK_CACHE_INDEX));
                InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(isr);
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                AVast.obtain().util.method.closeQuietly(isr);
                AVast.obtain().util.method.closeQuietly(bis);
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void put(CacheRequest key, String result) {
        /* 获取 DiskLruCache Editor 对象,即可编辑 DiskLruCache.
         * 如果当前不存在 Editor 对象,那么 edit() 就会返回一个新的 Editor 对象,通过它可以得到一个文件输出流.
         * 如果当前缓存正在被编辑,那么 edit() 将会返回 null ,即 DiskLruCache 不允许同时编辑一个缓存对象 */
        DiskLruCache.Editor editor;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = DiskCache.obtain().mDiskLruCache.edit(((StringCacheRequest) key).uriMd5);
            if (editor != null) {
            /* 由于 DiskLruCache 的 open 方法中设置了一个节点只能有一个数据,因此下面的 DISK_CACHE_INDEX 常量直接设为0 */
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (writeStringToDisk(result, outputStream)) {
                    // Log.d("BitmapDiskCache", "获取到网络数据|" + key);
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

    private boolean writeStringToDisk(String string, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
        boolean result = true;
        try {
            bos.write(string.getBytes());
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
