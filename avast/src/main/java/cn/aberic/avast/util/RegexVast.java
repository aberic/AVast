package cn.aberic.avast.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证相关操作的类.
 */
public class RegexVast {

    /**
     * 正则验证
     *
     * @param toCheckStr
     *         待验证的字符串
     * @param patternStr
     *         验证格式字符串
     *
     * @return 是否通过验证
     */
    private boolean canMatch(String toCheckStr, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(toCheckStr);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 匹配全网IP的正则表达式
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isIp(String toCheckStr) {
        return canMatch(toCheckStr, "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$");
    }

    /**
     * 是否为规范的手机电话号码
     * <p/>
     * 支持130——139、150——153、155——159、180、183、185、186、188、189号段
     *
     * @param toCheckStr
     *         toCheckStr
     *
     * @return 是否为规范的手机电话号码 ，以13/15/18开头
     */
    public boolean isMobile(String toCheckStr) {
        return canMatch(toCheckStr,  "^1{1}(3{1}\\d{1}|5{1}[012356789]{1}|8{1}[035689]{1})\\d{8}$");
    }

    /**
     * 邮箱验证
     *
     * @param toCheckStr
     *         toCheckStr
     *
     * @return 邮箱验证
     */
    public boolean isEmail(String toCheckStr) {
        return canMatch(toCheckStr, "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }

    /**
     * 验证是否为汉字.
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isCH(String toCheckStr) {
        return canMatch(toCheckStr, "^[\u4e00-\u9fa5]+$");
    }

    /**
     * 匹配正整数的正则表达式，个数限制为一个或多个
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isNumeric(String toCheckStr) {
        return canMatch(toCheckStr, "^\\d+$");
    }

    /**
     * 验证是否为整数或字母.
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isNumOrChar(String toCheckStr) {
        return canMatch(toCheckStr, "[a-zA-Z0-9][a-zA-Z0-9]*");
    }

    /**
     * 验证是否为身份证号
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isIDCard(String toCheckStr) {
        return canMatch(toCheckStr, "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$");
    }

    /**
     * 验证是否为电话号码
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isTeleNo(String toCheckStr) {
        return canMatch(toCheckStr, "(^[0-9]{3,4}\\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\\([0-9]{3,4}\\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)");
    }

    /**
     * 验证是否为合法的用户名. 用户名只能由汉字、数字、字母、下划线组成，且不能为空.
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isUserName(String toCheckStr) {
        return canMatch(toCheckStr, "^[a-zA-Z0-9_\u4e00-\u9fa5]+$");
    }

    /**
     * 验证是否为正常的文本内容. 内容只能为：汉字、数字、字母、下划线、 中文标点符号，英文标点符号，且不能为空.
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isNormalText(String toCheckStr) {
        return canMatch(toCheckStr, "^[a-zA-Z0-9_\u4e00-\u9fa5" // 汉字、数字、字母、下划线
                // 中文标点符号（。 ； ， ： “ ”（ ） 、 ！ ？ 《 》）
                + "\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff01\uff1f\u300a\u300b"
                // 英文标点符号（. ; , : ' ( ) / ! ? < >）
                + "\u002e\u003b\u002c\u003a\u0027\u0028\u0029\u002f\u0021\u003f\u003c\u003e\r\n"
                + "]+$");
    }

    /**
     * 验证是否为Url的文本内容. 内容只能为：数字、字母、英文标点符号（. : / ），且不能为空.
     *
     * @param toCheckStr
     *         待验证的字符串
     *
     * @return 是否通过验证
     */
    public boolean isUrl(String toCheckStr) {
        return canMatch(toCheckStr, "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$");
    }

    /**
     * 是否规范的邮编
     *
     * @param toCheckStr
     *         toCheckStr
     *
     * @return 是否规范的邮编
     */
    public boolean iszipCode(String toCheckStr) {
        return canMatch(toCheckStr, "^\\d{6}$");
    }

    /**
     * 办公电话验证 格式：区号(可选)-主机号-分机号(可选)
     *
     * @param toCheckStr
     *         toCheckStr
     *
     * @return 办公电话验证 格式：区号(可选)-主机号-分机号(可选)
     */
    public boolean isWorkPhone(String toCheckStr) {
        return canMatch(toCheckStr, "(^[0-9]{3,4}-[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{7,8}$)");
    }

    /**
     * 常用固定电话验证 格式：区号(可选)-主机号
     *
     * @param toCheckStr
     *         toCheckStr
     *
     * @return 常用固定电话验证 格式：区号(可选)-主机号
     */
    public boolean isPhoneNumber(String toCheckStr) {
        return canMatch(toCheckStr, "(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}$)");
    }

    /**
     * 将身份证后六位隐藏,不显示
     *
     * @param identityID
     *         identityID
     *
     * @return String
     */
    public String hideIdentityID(String identityID) {
        if (identityID != null && identityID.length() > 6) {
            identityID = identityID.substring(0, identityID.length() - 6)
                    + "******";
        }
        return identityID;
    }
}
