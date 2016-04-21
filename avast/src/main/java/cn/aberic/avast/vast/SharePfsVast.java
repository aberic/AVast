package cn.aberic.avast.vast;

import android.content.Context;

/**
 * SharedPreferences存储工具类，存储对象为"common"
 *
 * @author Aberic
 */
public class SharePfsVast {

    private final String shareCommon = "aCommon";

    private Context mContext;

    private SharePfsVast() {
    }

    public SharePfsVast(Context context) {
        this.mContext = context;
    }

    /**
     * 保存内容（String）至SharedPreferences，所有使用此方法的均保存至名为common的share
     *
     * @param key
     *         保存键名
     * @param value
     *         保存内容
     */
    public void saveString(String key, String value) {
        mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 取出内容（String）至SharedPreferences，所有使用此方法的share均均来自同一个存储名common
     *
     * @param key
     *         键名
     * @param value
     *         默认值
     *
     * @return 值（默认值""）
     */
    public String getString(String key, String value) {
        return mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).getString(key, value);
    }

    /**
     * 保存内容（boolean）至SharedPreferences，所有使用此方法的均保存至名为common的share
     *
     * @param key
     *         保存键名
     * @param value
     *         保存内容
     */
    public void saveBoolean(String key, boolean value) {
        mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    /**
     * 取出内容（boolean）至SharedPreferences，所有使用此方法的share均均来自同一个存储名common
     *
     * @param key
     *         键名
     *
     * @return 值（默认值false）
     */
    public boolean getBoolean(String key) {
        return mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * 保存内容（Integer）至SharedPreferences，所有使用此方法的均保存至名为common的share
     *
     * @param key
     *         保存键名
     * @param value
     *         保存内容
     */
    public void saveInt(String key, int value) {
        mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * 取出内容（Integer）至SharedPreferences，所有使用此方法的share均均来自同一个存储名common
     *
     * @param key
     *         键名
     * @param value
     *         默认值
     *
     * @return 值（默认值-1）
     */
    public int getInteger(String key, int value) {
        return mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).getInt(key, value);
    }

    /**
     * 保存内容（Float）至SharedPreferences，所有使用此方法的均保存至名为common的share
     *
     * @param key
     *         保存键名
     * @param value
     *         保存内容
     */
    public void saveFloat(String key, float value) {
        mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
    }

    /**
     * 取出内容（Float）至SharedPreferences，所有使用此方法的share均均来自同一个存储名common
     *
     * @param key
     *         键名
     * @param value
     *         默认值
     *
     * @return 值（默认值-1）
     */
    public float getFloat(String key, float value) {
        return mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).getFloat(key, value);
    }

    /**
     * 保存内容（long）至SharedPreferences，所有使用此方法的均保存至名为common的share
     *
     * @param key
     *         保存键名
     * @param value
     *         保存内容
     */
    public void saveLong(String key, long value) {
        mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    /**
     * 取出内容（long）至SharedPreferences，所有使用此方法的share均均来自同一个存储名common
     *
     * @param key
     *         键名
     * @param value
     *         默认值
     *
     * @return 值（默认值-1）
     */
    public long getLong(String key, long value) {
        return mContext.getSharedPreferences(shareCommon, Context.MODE_PRIVATE).getLong(key, value);
    }
}
