package cn.aberic.avast.view.loopBanner.entity;

/**
 * 顶部轮播图片对象
 *
 * @author aberic
 */
public class BannerItem {

    /** 图片展示类型：0-贴子、1-网页连接、2-待定 **/
    public int type;
    /** 图片相关id **/
    public int id;
    /** 图片地址 **/
    public String imgPath;

    public BannerItem(){}

    public BannerItem(int type, int id, String imgPath) {
        this.type = type;
        this.id = id;
        this.imgPath = imgPath;
    }

}
