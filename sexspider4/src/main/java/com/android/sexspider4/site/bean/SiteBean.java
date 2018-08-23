package com.android.sexspider4.site.bean;

import com.android.sexspider4.utils.StringUtils;

/**
 * Created by feng on 2017/5/5.
 */

public class SiteBean {
    public int siteId;
    public String siteRank;
    public int vipLevel;
    public int isHided;
    public String siteName;
    public String listPage;
    public String pageEncode;
    public String domain;
    public String siteLink;
    public String siteFilter;
    public String siteReplace;
    public String mainDiv;
    public String thumbDiv;
    public String listDiv;
    public String listFilter;
    public String imageDiv;
    public String imageFilter;
    public String pageLevel;
    public String pageDiv;
    public String pageFilter;
    public int listNum = 1;
    public int isFirst = 0;
    public int isUpdated;//0 未更新，1 成功，2 失败，3 有内容，4 有更新
    public int isDowning;
    public String docType;
    public String siteNotes;
    public String lastStart;

    public String getSiteTitleAndDomain() {
        return siteRank + ". " + siteName + " " + StringUtils.substringLeft(domain.replaceAll("http://|https://", ""), "/");
    }

    public String getSiteTitle() {
        return siteName + " (" + (listNum - 1) + ")";
    }

    public boolean IsVideo() {
        return siteRank.startsWith("C");
    }

    public boolean IsVip() {
        return vipLevel == 0;
    }

    public boolean IsAjax() {
        return docType.contains("ajax");
    }
}
