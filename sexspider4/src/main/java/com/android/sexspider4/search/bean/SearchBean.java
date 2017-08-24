package com.android.sexspider4.search.bean;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchBean {
    public int searchId;
    public String searchName;
    public int searchType = 0;//0 正常，1 词典，2 停止词, 3 SEG分词, 4 删除词
    public int searchCount;

    public SearchBean() { }

    public SearchBean(String searchName) {
        this.searchName = searchName;
    }

    public SearchBean(String searchName, int searchType) {
        this.searchName = searchName;
        this.searchType = searchType;
    }

}
