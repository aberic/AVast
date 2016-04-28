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
##### http 网络请求
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
##### image loader 图片异步加载
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
######或
```xml
<cn.aberic.avast.view.AImageView
    android:id="@+id/img"
    xmlns:avast="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```
```java
AImageView aImageView = (AImageView) findViewById(R.id.img);
aImageView.setImageUrl(String url); // 设置加载图片(允许类型(http://-mipmap://-drawable://-file://))
```
##### cache 缓存应用
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
##### pool 线程池简单应用
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
##### shape 图片形状（圆角、圆形）处理
```java
AVast.obtain().shape.circularShape.makeShape(ImageView imageView, int resId, ShapeConfig config);
AVast.obtain().shape.circularShape.makeShape(ImageView imageView, Bitmap bitmap, ShapeConfig config);
AVast.obtain().shape.circularShape.makeShape(ImageView imageView, Bitmap bitmap);
AVast.obtain().shape.circularShape.makeShape(ImageView imageView, int resId);
AVast.obtain().shape.cornerShape.makeShape(…);
```
##### util-TimerUtil 定时器
```java
TimerUtil timerUtil;
timerUtil = new TimerUtil(false);
timerUtil.start(3 * 1000, 3 * 1000);
timerUtil.monitorTimer(new TimerUtil.TimerListener() {
    @Override
    public void onTimerListener() {
        // 执行定时任务
    }
});
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
```java
GestureLockViewGroup gestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.gestureLockViewGroup)；
gestureLockViewGroup.setAnswer(String src); // 设置密码
gestureLockViewGroup.setOnGestureLockViewListener(
      new GestureLockViewGroup.OnGestureLockViewListener() {
          @Override
          public void onLockInputResult(GestureLockViewGroup.LockInputResult lockResultType) {
                switch (lockResultType) {
                    case LOCK_INPUT_RIGHT: // 手势输入正确
                        break;
                    case LOCK_INPUT_WRONG: // 手势输入错误
                        break;
                    case LOCK_INPUT_LESS: // 手势输入少于4位
                        break;
                    case LOCK_INPUT_OVER_TIME: // 手势输入次数超过5次
                        break;
                    case LOCK_INPUT_PRESS: // 当按压屏幕时
                        break;
                 }
            }
        }
);
gestureLockViewGroup.reset(); // 重置手势密码界面
gestureLockViewGroup.setMaxTryTimes(5);// 设置最大尝试次数
gestureLockViewGroup.getMaxTryTimes(); // 得到剩余尝试次数
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
```java
avast:showDots="true" // 显示圆点指标与否
avast:isLoop="false" // 循环与否
avast:isAutoPlay="false" // 自动播放与否
avast:marginBottom="12" // 距离底部高度
avast:dotBackground="0xFF939090" // 小圆点背景色
avast:dotRes="0xFF939090" // 圆点未选中颜色
avast:dotChooseRes="0xFF939090" // 圆点选中颜色
avast:dotMargins="2" // 圆点间距
```
```java
AViewPager aViewPager =  (AViewPager) itemView.findViewById(R.id.loop);
ArrayList<String> imgPaths = new ArrayList<>();
int size = banners.size();
for (int i = 0; i < size; i++) {
    imgPaths.add(banners.get(i).imgPath);
}
aViewPager.initAViewPagerOnItemClick(imgPaths, new CallBack.OnItemClickListener() {
    @Override
    public void onItemClick(View v, int position) {
        listener.bannerClick(banners.get(position).url);
    }
});
```
##### LoadContentLayout 页面默认加载状态（加载中、加载失败、加载完成、加载数据为空）
```xml
<cn.aberic.avast.view.LoadContentLayout
    android:id="@+id/loadContent"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    avast:emptyView="@layout/view_empty" // 设置加载为空界面
    avast:errorView="@layout/view_error" // 设置加载失败界面
    avast:loadingView="@layout/view_loading" // 设置加载中界面
    >

    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</cn.aberic.avast.view.LoadContentLayout>
```
```java
LoadContentLayout loadContentRoot = (LoadContentLayout) findViewById(R.id.loadContent);
loadContentRoot.setViewState(LoadContentLayout.ContentState.VIEW_CONTENT);
loadContentRoot.setViewState(LoadContentLayout.ContentState.VIEW_ERROR);
loadContentRoot.setViewState(LoadContentLayout.ContentState.VIEW_EMPTY);
loadContentRoot.setViewState(LoadContentLayout.ContentState.VIEW_LOADING);
// 设置重新加载监听，当且仅当VIEW_ERROR生效
loadContentRoot.setReLoad(new LoadContentLayout.LoadingListener() {
    @Override
    public void load() {
        // 重新加载数据操作
    }
});
```
##### vast-AUtil 常用工具类用法
```java
AVast.obtain().util.toast.showShort(String str); // 短时间显示Toast
AVast.obtain().util.toast.showLong(String str); // 长时间显示Toast
AVast.obtain().util.imageCompressSize… // 图片压缩功能类
AVast.obtain().util.file… // 与File相关方法
AVast.obtain().util.date… // 一般日期（时间）处理通用公共方法类
AVast.obtain().util.md5… // MD5加密处理工具类
AVast.obtain().util.sharePfs… // SharedPreferences存储工具类
AVast.obtain().util.method… // 一般通用公共方法类
AVast.obtain().util.bitmap… // 一般 Bitmap（图片）处理通用公共方法类
AVast.obtain().util.zip… // 实现的Zip工具
AVast.obtain().util.sdCard… // SD卡工具箱
AVast.obtain().util.webView… // WebView管理器，提供常用设置
AVast.obtain().util.regex… // 正则验证相关操作
AVast.obtain().util.netWork… // 网络工作工具类
AVast.obtain().util.input… // 软键盘工具类
AVast.obtain().util.dialog… // 弹窗工具类
AVast.obtain().util.notification… // 通知工具类
```

#### 感谢
* [kaitiren](http://blog.csdn.net/kaitiren/article/details/38513715)
* [CheeRok](http://blog.csdn.net/u012403246/article/details/47723365)
* [MrSimp1e](http://blog.csdn.net/bboyfeiyu/article/details/44172273)
* [MrSimp1e](http://blog.csdn.net/bboyfeiyu/article/details/43152997)
* [鸿洋_](http://blog.csdn.net/lmj623565791/article/details/36236113)
* [Kennyc1012](https://github.com/Kennyc1012/MultiStateView)
* [Biliping](https://github.com/Biliping)
* 其他相关参考来源和链接整理中…

#### 尾巴
AVast 很多内容都是参考学习其他大神的文章或项目，其中手势密码仅修改部分功能和添加注释，LoadContentLayout源码摘自MultiStateView。
很多引用来源的注释尚未补全，正在整理更新。
