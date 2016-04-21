package cn.aberic.avast.vast;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 图片压缩功能类
 * <p/>
 * 作者：Aberic on 16/2/4 10:47
 * 邮箱：abericyang@gmail.com
 */
public class ImageCompressSize {

    /**
     * 解析/解码采样位图
     * <p/>
     * 从资源中获取图片并压缩至指定大小
     *
     * @param res
     *         资源（一般情况为getResources()）
     * @param resId
     *         资源id
     * @param reqWidth
     *         需求宽
     * @param reqHeight
     *         需求高
     *
     * @return 指定 Bitmap 图
     */
    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /* BitmapFactory只会解析/解码图片的原始宽高信息,而不会真正加载进内存 */
        options.inJustDecodeBounds = true;
        /* ALPHA_8 代表8位Alpha位图.
         * ARGB_4444 代表16位ARGB位图.
         * ARGB_8888 代表32位ARGB位图.
         * RGB_565 代表8位RGB位图.
         * 位图位数越高代表其可以存储的颜色信息越多，当然图像也就越逼真。*/
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeResource(res, resId, options);
        /* 得到图片压缩所需采样率值 */
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        /* 取消纯解析/解码机制 */
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 解析/解码采样位图
     * <p/>
     * 从文件中获取图片并压缩至指定大小
     *
     * @param fd
     *         文件描述内容
     * @param reqWidth
     *         需求宽
     * @param reqHeight
     *         需求高
     *
     * @return 指定 Bitmap 图
     */
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /* BitmapFactory只会解析/解码图片的原始宽高信息,而不会真正加载进内存 */
        options.inJustDecodeBounds = true;
        /* ALPHA_8 代表8位Alpha位图.
         * ARGB_4444 代表16位ARGB位图.
         * ARGB_8888 代表32位ARGB位图.
         * RGB_565 代表8位RGB位图.
         * 位图位数越高代表其可以存储的颜色信息越多，当然图像也就越逼真。*/
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        /* 得到图片压缩所需采样率值 */
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        /* 取消纯解析/解码机制 */
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 解析/解码采样位图
     * <p/>
     * 从文件中获取图片并压缩至指定大小
     *
     * @param filePath
     *         文件路径
     * @param reqWidth
     *         需求宽
     * @param reqHeight
     *         需求高
     *
     * @return 指定 Bitmap 图
     */
    public Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /* BitmapFactory只会解析/解码图片的原始宽高信息,而不会真正加载进内存 */
        options.inJustDecodeBounds = true;
        /* ALPHA_8 代表8位Alpha位图.
         * ARGB_4444 代表16位ARGB位图.
         * ARGB_8888 代表32位ARGB位图.
         * RGB_565 代表8位RGB位图.
         * 位图位数越高代表其可以存储的颜色信息越多，当然图像也就越逼真。*/
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);
        /* 得到图片压缩所需采样率值 */
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        /* 取消纯解析/解码机制 */
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算生成需求大小图片所得采样率的值
     *
     * @param options
     *         原始图片数据源
     * @param reqWidth
     *         需求宽
     * @param reqHeight
     *         需求高
     *
     * @return 采样率值
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        /* 如果需求宽高为0,即不要求压缩 */
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }
        /* 从BitmapFactory.Options取出原始图片的宽和高 */
        final int height = options.outHeight;
        final int width = options.outWidth;
        // Log.d(TAG, "height=" + height + "|" + reqHeight + "||width=" + width + "|" + reqWidth);
        int inSampleSize = 1;// 图片压缩采样率,默认为1不压缩,当为2时,像素为原图的1/4
        if (height > reqHeight || width > reqWidth) {// 真实高宽大于需求高宽
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            /* 计算所支持的最大采样率值,如果依旧大于需求宽高,则扩大采样率,且采样率为2的倍数 */
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
