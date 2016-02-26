package cn.aberic.avast.http.requests;

import java.util.HashMap;

/**
 * 作者：Aberic on 16/1/25 14:10
 * 邮箱：abericyang@gmail.com
 */
public class RequestParams {

    private HashMap<String, String> map;

    public RequestParams() {
        map = new HashMap<>();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void put(String key, int value) {
        map.put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        map.put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        map.put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        map.put(key, String.valueOf(value));
    }

    public HashMap<String, String> get() {
        return map;
    }

}
