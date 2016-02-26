package cn.aberic.avast.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类，内置两个方法分别用于短时间和长时间来显示Toast
 *
 * @author Aberic
 */
public class ToastVast {

    private Context mContext;
    private Toast toast = null; //Toast的对象！

    public ToastVast(Context context) {
        this.mContext = context;
    }

    public void showToast(Context mContext, String str, int duration) {
        if (toast == null) {
            toast = Toast.makeText(mContext, str, duration);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param str
     *         要显示的内容
     */
    public void showShort(String str) {
        showToast(mContext, str, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param str
     *         要显示的内容
     */
    public void showLong(String str) {
        showToast(mContext, str, Toast.LENGTH_LONG);
    }
}
