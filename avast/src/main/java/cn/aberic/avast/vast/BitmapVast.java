package cn.aberic.avast.vast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * 一般 Bitmap（图片）处理通用公共方法类
 *
 * @author Aberic
 */
public class BitmapVast {

    private Context mContext;

    public BitmapVast(Context context) {
        this.mContext = context;
    }

    /**
     * 取得指定区域的图形
     *
     * @param source
     * @param x
     * @param y
     * @param width
     * @param height
     *
     * @return
     */
    public Bitmap getBitmap(Bitmap source, int x, int y, int width, int height) {
        return Bitmap.createBitmap(source, x, y, width, height);
    }

    /**
     * 从大图中截取小图
     *
     * @param source
     * @param row
     * @param col
     * @param rowTotal
     * @param colTotal
     * @param multiple
     * @param isRecycle
     *
     * @return
     */
    public Bitmap getImage(Bitmap source, int row, int col, int rowTotal, int colTotal, float multiple, boolean isRecycle) {
        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth() / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (isRecycle) {
            recycleBitmap(source);
        }
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
        }
        return temp;
    }

    /**
     * 从大图中截取小图
     *
     * @param source
     * @param row
     * @param col
     * @param rowTotal
     * @param colTotal
     * @param multiple
     *
     * @return
     */
    public Drawable getDrawableImage(Bitmap source, int row, int col, int rowTotal, int colTotal, float multiple) {
        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth() / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
        }
        Drawable d = new BitmapDrawable(mContext.getResources(), temp);
        return d;
    }

    public Bitmap decodeResource(int resourseId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        opt.inPurgeable = true;
        opt.inInputShareable = true; // 需把 inPurgeable设置为true，否则被忽略
        // 获取资源图片
        InputStream is = mContext.getResources().openRawResource(resourseId);
        return BitmapFactory.decodeStream(is, null, opt); // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无需再使用java层的createBitmap，从而节省了java层的空间
    }

    /**
     * 从assets文件下解析图片
     *
     * @param resName
     *
     * @return
     */
    public Bitmap decodeBitmapFromAssets(String resName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream in = null;
        try {
            // in = AssetsResourcesUtil.openResource(resName);
            in = mContext.getAssets().open(resName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in, null, options);
    }

    /** 回收不用的bitmap */
    public void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
        }
    }

    /**
     * drawable转换成bitmap
     *
     * @param drawable
     *
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            drawable.draw(canvas);
            return bitmap;
        } else {
            throw new IllegalArgumentException("can not support this drawable to bitmap now!!!");
        }
    }

    /**
     * bitmap转换成drawable
     *
     * @param bmp
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public Drawable bitmapToDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    /**
     * 读取sd卡下图片，由图片路径转换为bitmap，w和h代表需要转换的大小
     *
     * @param path
     * @param w
     * @param h
     *
     * @return
     */
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    /**
     * Bitmap优化显示
     *
     * @param imageFile
     *         图片路径
     *
     * @return bitmap
     */
    public Bitmap getBitmap(String imageFile) {
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile, bitmapFactoryOptions);
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight / 300);
        int xRatio = (int) Math.ceil(bitmapFactoryOptions.outWidth / 300);

        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }
        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imageFile, bitmapFactoryOptions);
        return bitmap;
    }

    /**
     * 加载本地图片
     *
     * @param file
     *
     * @return
     */
    public Bitmap getLoacalBitmap(String file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 bitmap 大小
     *
     * @param bitmap
     *         bitmap
     *
     * @return size
     */
    public long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
