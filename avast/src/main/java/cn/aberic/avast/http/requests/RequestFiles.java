package cn.aberic.avast.http.requests;

import java.io.File;
import java.util.HashMap;

/**
 * 作者：Aberic on 16/1/25 14:10
 * 邮箱：abericyang@gmail.com
 */
public class RequestFiles {

    private HashMap<String, File> map;

    public RequestFiles() {
        map = new HashMap<>();
    }

    public void put(String key, File value) {
        map.put(key, value);
    }

    public HashMap<String, File> get() {
        return map;
    }

}
