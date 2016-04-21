package cn.aberic.avast.http.response;

import java.io.File;

import cn.aberic.avast.http.base.Response;

/**
 * User: Aberic Yang(abericyang@gmail.com)
 * Date: 2016-04-11
 * Time: 10:35
 */
public class FileResponse extends Response{

    /** 请求返回附件转 File 类 */
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
