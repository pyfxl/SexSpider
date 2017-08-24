package com.android.sexspider4.list.bean;

import com.android.sexspider4.site.bean.SiteBean;

/**
 * Created by feng on 2017/5/5.
 */

public class ListBean {
    public int listId;
    public String listTitle;
    public String listLink;
    public String listPicture;
    public int isFavorite;
    public int listNum;
    public int isDown;//0 未下载，1 下载完成，2 下载部分，3 下载失败
    public int downStatus;//0 未下载，1 下载完成，2 下载部分，3 下载失败
    public int isDowning;
    public int isShow;
    public int isRead;
    public int isRedown;
    public int loadNum;
    public int listRank;
    public int isNew;
    public SiteBean siteInfo;

    public String getListLink() {
        return listLink.startsWith("http") ? listLink : siteInfo.domain + listLink;
    }

}
