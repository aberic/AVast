package cn.aberic.avast.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;

import cn.aberic.avast.core.AVast;

/**
 * 创建弹窗
 * 作者：Aberic on 15/7/20 16:01
 * 邮箱：abericyang@gmail.com
 */
public class DialogVast {

    private String sure = "确认", cancel = "取消";

    /**
     * 普通双选弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param msg
     *         弹窗内容
     * @param onDialogClickListener
     *         回调函数
     */
    public void show(final Context context, String title, String msg, final OnDialogClickListener onDialogClickListener) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).
                setPositiveButton(sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.resultString("");
                    }
                }).setNegativeButton(cancel, null).show();
    }

    /**
     * 修改文本弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param content
     *         弹窗默认（或原有）内容
     * @param onDialogClickListener
     *         回调函数
     */
    public void showChangeText(final Context context, String title, String content, final OnDialogClickListener onDialogClickListener) {
        final AppCompatEditText editText = new AppCompatEditText(context);
        editText.setText(content);
        new AlertDialog.Builder(context).setTitle(title).setView(editText, 64, 0, 64, 0).
                setPositiveButton(sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(editText.getText().toString())) {
                            AVast.obtain().util.toast.showShort("内容为空，未做任何修改");
                        } else {
                            onDialogClickListener.resultString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton(cancel, null).show();
    }

    /**
     * 单选弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param strs
     *         单选数组
     * @param position
     *         当前选项
     * @param onDialogClickListener
     *         回调函数
     */
    public void showSelect(final Context context, String title, String[] strs, int position, final OnDialogClickListener onDialogClickListener) {
        new AlertDialog.Builder(context).setTitle(title).setSingleChoiceItems(strs, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClickListener.resultString(String.valueOf(which));
                dialog.dismiss();
            }
        }).setNegativeButton(cancel, null).show();
    }

    /**
     * 弹窗传值接口
     *
     * @author aberic
     */
    public interface OnDialogClickListener {

        /**
         * @param str
         *         传值String类型，后续转换
         */
        void resultString(String str);

    }

}
