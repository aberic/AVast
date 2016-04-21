package cn.aberic.avast.vast;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;

import cn.aberic.avast.R;
import cn.aberic.avast.core.AVast;

/**
 * 创建弹窗
 * 作者：Aberic on 15/7/20 16:01
 * 邮箱：abericyang@gmail.com
 */
public class DialogVast {

    /**
     * 普通弹窗
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
    public void showOnly(final Context context,String title, String msg, final OnDialogClickListener onDialogClickListener) {
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        if (null != onDialogClickListener) {
            new AlertDialog.Builder(context).setCustomTitle(textView).setMessage(msg).setPositiveButton(context.getApplicationContext().getResources().getString(R.string.vast_sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDialogClickListener.resultString("");
                }
            }).show();
        } else {
            new AlertDialog.Builder(context).setCustomTitle(textView).setMessage(msg).setPositiveButton(context.getApplicationContext().getResources().getString(R.string.vast_sure), null).show();
        }
    }

    /**
     * 普通双选弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param msg
     *         弹窗内容
     * @param sure
     *         自定义确定文字
     * @param cancel
     *         自定义取消文字
     * @param onDialogClickListener
     *         回调函数
     */
    public void showMulti(final Context context, String title, String msg, String sure, String cancel, final OnDialogClickListener onDialogClickListener) {
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        new AlertDialog.Builder(context).setCustomTitle(textView).setMessage(msg).
                setPositiveButton(sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.resultString("");
                    }
                }).setNegativeButton(cancel, null).show();
    }

    /**
     * 普通三选弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param msg
     *         弹窗内容
     * @param first
     *         第一个按钮名称
     * @param middle
     *         第二个按钮名称
     * @param end
     *         第三个按钮名称
     * @param onDialogThreeClickListener
     *         回调函数
     */
    public void showThree(final Context context, String title, String msg, String first, String middle, String end, final OnDialogThreeClickListener onDialogThreeClickListener) {
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        new AlertDialog.Builder(context).setCustomTitle(textView).setMessage(msg).
                setPositiveButton(first, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogThreeClickListener.click(OnDialogThreeClickListener.FIRST);
                    }
                }).setNeutralButton(middle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogThreeClickListener.click(OnDialogThreeClickListener.MIDDLE);
            }
        }).setNegativeButton(end, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogThreeClickListener.click(OnDialogThreeClickListener.END);
            }
        }).show();
    }

    /**
     * 修改文本弹窗(可选输入类型)
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param hintContent
     *         弹窗默认（或原有）内容
     * @param inputType
     *         输入类型
     * @param onDialogClickListener
     *         回调函数
     */
    public void showChangeText(final Context context, String title, String hintContent, int inputType, final OnDialogClickListener onDialogClickListener) {
        final AppCompatEditText editText = new AppCompatEditText(context);
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        editText.setHint(hintContent);
        editText.setInputType(inputType);
        editText.setTextColor(AVast.obtain().util.method.getColor(context, R.color.C_3));
        editText.setHintTextColor(AVast.obtain().util.method.getColor(context, R.color.C_A));
        new AlertDialog.Builder(context).setCustomTitle(textView).setView(editText, 64, 0, 64, 0).
                setPositiveButton(context.getApplicationContext().getResources().getString(R.string.vast_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(editText.getText().toString())) {
                            AVast.obtain().util.toast.showShort("内容为空，未做任何修改");
                        } else {
                            onDialogClickListener.resultString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton(context.getApplicationContext().getResources().getString(R.string.vast_cancel), null).show();
    }

    /**
     * 修改文本弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param hintContent
     *         弹窗默认（或原有）内容
     * @param onDialogClickListener
     *         回调函数
     */
    public void showChangeText(final Context context, String title, String hintContent, final OnDialogClickListener onDialogClickListener) {
        final AppCompatEditText editText = new AppCompatEditText(context);
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        editText.setHint(hintContent);
        editText.setTextColor(AVast.obtain().util.method.getColor(context, R.color.C_3));
        editText.setHintTextColor(AVast.obtain().util.method.getColor(context, R.color.C_A));
        new AlertDialog.Builder(context).setCustomTitle(textView).setView(editText, 64, 0, 64, 0).
                setPositiveButton(context.getApplicationContext().getResources().getString(R.string.vast_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(editText.getText().toString())) {
                            AVast.obtain().util.toast.showShort("内容为空，未做任何修改");
                        } else {
                            onDialogClickListener.resultString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton(context.getApplicationContext().getResources().getString(R.string.vast_cancel), null).show();
    }

    /**
     * 修改文本弹窗,带提示
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param hintContent
     *         弹窗默认（或原有）内容
     * @param onDialogInputLayoutClickListener
     *         回调函数
     */
    public void showChangeText(final Context context, String title, String hintContent, final OnDialogInputLayoutClickListener onDialogInputLayoutClickListener) {
        final TextInputLayout inputLayout = new TextInputLayout(context);
        final TextInputEditText inputEditText = new TextInputEditText(context);
        inputEditText.setHint(hintContent);
        inputEditText.setTextColor(AVast.obtain().util.method.getColor(context, R.color.C_3));
        inputEditText.setHintTextColor(AVast.obtain().util.method.getColor(context, R.color.C_A));

        new AlertDialog.Builder(context).setTitle(title).setView(inputLayout, 64, 0, 64, 0).
                setPositiveButton(context.getApplicationContext().getResources().getString(R.string.vast_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(inputEditText.getText().toString())) {
                            AVast.obtain().util.toast.showShort("内容为空，未做任何修改");
                        } else {
                            onDialogInputLayoutClickListener.resultString(inputEditText.getText().toString(), inputLayout);
                        }
                    }
                }).setNegativeButton(context.getApplicationContext().getResources().getString(R.string.vast_cancel), null).show();
    }

    /**
     * 单选弹窗
     *
     * @param context
     *         上下文
     * @param title
     *         弹窗标题
     * @param strArray
     *         单选数组
     * @param position
     *         当前选项
     * @param onDialogClickListener
     *         回调函数
     */
    public void showSelect(final Context context, String title, String[] strArray, int position, final OnDialogClickListener onDialogClickListener) {
        AppCompatTextView textView= (AppCompatTextView) LayoutInflater.from(context).inflate(R.layout.custom_view_title,null);
        textView.setText(title);
        new AlertDialog.Builder(context).setCustomTitle(textView).setSingleChoiceItems(strArray, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClickListener.resultString(String.valueOf(which));
                dialog.dismiss();
            }
        }).setNegativeButton(context.getApplicationContext().getResources().getString(R.string.vast_cancel), null).show();
    }

    /**c
     * 弹窗传值接口
     *
     * @author aberic
     */
    public interface OnDialogClickListener {

        /**
         * 回调结果
         *
         * @param str
         *         传值String类型，后续转换
         */
        void resultString(String str);

    }

    /**
     * 三按钮弹窗传值接口
     *
     * @author aberic
     */
    public interface OnDialogThreeClickListener {

        int FIRST = 0;
        int MIDDLE = 1;
        int END = 2;

        /**
         * 回调结果
         *
         * @param position
         *         单击选项
         */
        void click(int position);

    }

    /**
     * 弹窗传值接口
     *
     * @author aberic
     */
    public interface OnDialogInputLayoutClickListener {

        /**
         * 回调结果
         *
         * @param str
         *         传值String类型，后续转换
         * @param inputLayout
         *         输入控件
         */
        void resultString(String str, TextInputLayout inputLayout);

    }

}
