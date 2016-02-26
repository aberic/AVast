package cn.aberic.avast.imageLoader.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * 作者：Aberic on 16/2/16 21:49
 * 邮箱：abericyang@gmail.com
 */
public class ImageViewHelper {

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;

    /**
     * 获取 ImageView 宽
     * <p/>
     * 配置参数或设备显示尺寸,大小计算算法
     *
     * @param imageView
     *         ImageView
     *
     * @return ImageView 宽
     */
    public static int getImageViewWidth(ImageView imageView) {
        if (null != imageView) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = 0;
            // 获得实际的绘制 getWidth() 的视图。如果视图还没有画则继续下一步判断
            if (null != params && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth();// 得到实际图像宽度
            }
            // 获取布局宽度,如果它没有精确值则继续下一步判断
            if (width <= 0 && null != params) {
                width = params.width;// 得到布局宽度参数
            }
            // 通过反射得到最大宽度
            if (width <= 0) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            }
            return width;
        }
        return DEFAULT_WIDTH;
    }

    /**
     * 获取 ImageView 高
     * <p/>
     * 配置参数或设备显示尺寸,大小计算算法
     *
     * @param imageView
     *         ImageView
     *
     * @return ImageView 高
     */
    public static int getImageViewHeight(ImageView imageView) {
        if (null != imageView) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int height = 0;
            // 获得实际的绘制 getHeight() 的视图。如果视图还没有画则继续下一步判断
            if (null != params && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight();// 得到实际图像高度
            }
            // 获取布局高度,如果它没有精确值则继续下一步判断
            if (height <= 0 && null != params) {
                height = params.height;// 得到布局高度参数
            }
            // 通过反射得到最大高度
            if (height <= 0) {
                height = getImageViewFieldValue(imageView, "mMaxHeight");
            }
            return height;
        }
        return DEFAULT_HEIGHT;
    }

    /**
     * 通过反射获取已声明 ImageView 对象的字段值
     *
     * @param object
     *         对象
     * @param fieldName
     *         字段名
     *
     * @return 字段值
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            // 通过反射获取 fieldName 字段
            Field field = ImageView.class.getDeclaredField(fieldName);
            // 设置该字段允许访问
            field.setAccessible(true);
            // 字段值
            int fieldValue = (int) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
