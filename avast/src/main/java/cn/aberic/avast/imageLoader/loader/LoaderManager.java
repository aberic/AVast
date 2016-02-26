package cn.aberic.avast.imageLoader.loader;

import java.util.HashMap;

/**
 * 作者：Aberic on 16/2/16 23:57
 * 邮箱：abericyang@gmail.com
 */
public class LoaderManager {

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";
    public static final String DRAWABLE = "drawable";
    public static final String MIPMAP = "mipmap";

    private HashMap<String, Loader> mLoaderMap = new HashMap<>();
    private Loader mNullLoader = new NullLoader();
    private static LoaderManager instance;

    /**
     * 在初始化 AImage 时默认将几个Loader注入到LoaderManager中，然后在加载图片时 AImage 会根据图片的 schema 来获取对应 Loader 来完成加载功能
     */
    private LoaderManager() {
        register(HTTP, new UrlLoader());
        register(HTTPS, new UrlLoader());
        register(FILE, new FileLoader());
        register(DRAWABLE, new DrawableLoader());
        register(MIPMAP, new MipmapLoader());
    }

    public static LoaderManager getInstance() {
        if (instance == null) {
            synchronized (LoaderManager.class) {
                if (instance == null) {
                    instance = new LoaderManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册 Loader,将 Loader 注入到 LoaderManager
     *
     * @param schema
     *         模式
     * @param loader
     *         Loader
     */
    public final synchronized void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }

}
