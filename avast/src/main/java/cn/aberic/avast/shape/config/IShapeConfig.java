package cn.aberic.avast.shape.config;

/**
 * 图片图形配置器
 * 作者：Aberic on 16/2/20 00:24
 * 邮箱：abericyang@gmail.com
 */
public interface IShapeConfig {

    /**
     * 设置圆角角度
     *
     * @param cornerRadius
     *         角度
     */
    void setCornerRadius(float cornerRadius);

    /**
     * 设置抗锯齿
     *
     * @param aa
     *         抗锯齿
     */
    void setAa(boolean aa);

}
