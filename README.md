# AVast
个人学习项目

#### 封装
* http 网络请求框架
* image loader 图片异步加载框架
* cache 缓存应用
* pool 线程池简单应用
* shape 图片形状（圆角、圆形）处理
* util-TimerUtil 定时器
* vast-AUtil 常用工具类
* lockView 手势密码
* loopBanner 轮播
* LoadContentLayout 页面默认加载状态（加载中、加载失败、加载完成、加载数据为空）

#### 用例
##### http
```java
RequestParams params = new RequestParams();
params.put("key", "value");
……
AVast.obtain().http.post(String url, RequestParams params, new AHttp.HttpCallback() {
    @Override
    public void onFailure(int errorCode, String result) {
        // 失败操作
    }

    @Override
    public void onSuccess(String result) {
        // 成功操作
    }
});
```

```java
AVast.obtain().http.get(String url, new AHttp.HttpCallback() {
    @Override
    public void onFailure(int errorCode, String result) {
        // 失败操作
    }

    @Override
    public void onSuccess(String result) {
        // 成功操作
    }
});
```

```java
AVast.obtain().http.download(String url, String fileName, String fileType, new Request.RequestListener<File>() {
    @Override
    public void onComplete(int stCode, File response) {
        // File 下载完成
    }
}, new Request.LoadListener() {
    @Override
    public void load(int total, int loaded) {
        Log.d(TAG, "loading = " + loaded + "/" + total);
    }
});
```
##### image loader
```java
// 普通加载
AVast.obtain().image.bindImage(ImageView imageView, String uri);
// 带配置加载
AVast.obtain().image.bindImage(ImageView imageView, String uri, DisplayConfig config);
// 带进度监听加载
AVast.obtain().image.bindImage(ImageView imageView, String uri, ImageListener listener);
// 带配置及进度监听加载
AVast.obtain().image.bindImage(ImageView imageView, String uri, DisplayConfig config, ImageListener listener);
// 圆角加载
AVast.obtain().image.bindImageInCorner(ImageView imageView, String uri);
// 圆形加载
AVast.obtain().image.bindImageInCircular(ImageView imageView, String uri);
```
##### cache
```java
// 缓存 String
AVast.obtain().cache.stringCache.get(CacheRequest key);
// 获取缓存 String
AVast.obtain().cache.stringCache.put(CacheRequest key, T t);
// 缓存 bitmap
AVast.obtain().cache.bitmapCache.get(CacheRequest key);
// 获取缓存 bitmap
AVast.obtain().cache.bitmapCache.put(CacheRequest key, T t);
```
##### pool
```java
// 在已有线程池中执行耗时任务
AVast.obtain().pool.submitFixed(new Runnable() {
    @Override
    public void run() {
        // 耗时任务
    }
});
// 启动可执行定时任务线程池
AVast.obtain().pool.startScheduledThreadPool();
// 在已启动定时任务线程池中执行任务
AVast.obtain().pool.submitScheduled(new Runnable() {
    @Override
    public void run() {
        // 定时任务
    }
}, long initialDelay, long period, TimeUnit unit);
```
##### lockView 手势密码
```xml
<cn.aberic.avast.view.lockView.GestureLockViewGroup
    xmlns:avast="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_below="@id/activity_lock_brief"
    android:gravity="center"
    avast:count="3" // 每个边上的GestureLockView的个数
    avast:tryTimes="5" // 最大尝试次数
/>
```
```java
avast:color_no_finger_inner_circle="0xFF939090" // 手指没有按住时内圆颜色
avast:color_no_finger_outer_circle="0xFF939090" // 手指没有按住时外圆颜色
avast:color_finger_on="0xFF939090" // 手指按住时内外圆颜色
avast:color_finger_up_right="0xFF939090" // 手指离开时内外圆颜色(密码正确)
avast:color_finger_up_wrong="0xFF939090" // 手指离开时内外圆颜色(密码错误)
```
##### loopBanner 轮播
```xml
<cn.aberic.avast.view.loopBanner.AViewPager
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:avast="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="150dp
    avast:isAutoPlay="false" // 自动播放
    avast:isLoop="false" // 自动循环
    avast:showDots="true" // 显示小圆点
/>
```
