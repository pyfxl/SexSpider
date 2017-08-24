package com.android.sexspider4.list.model;

import android.util.Log;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.helper.HtmlHelper;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.db.ImageAccess;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.db.ListAccess;
import com.android.sexspider4.list.listener.OnListDataLoadListener;
import com.android.sexspider4.list.listener.OnImageDownListener;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.db.SearchAccess;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.db.SiteAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListModelImpl implements IListModel {
    private SiteAccess siteAccess;
    private ListAccess listAccess;
    private ImageAccess imageAccess;
    private SearchAccess searchAccess;

    public ListModelImpl() {
        siteAccess = new SiteAccess(MyApplication.getAppContext());
        listAccess = new ListAccess(MyApplication.getAppContext());
        imageAccess = new ImageAccess(MyApplication.getAppContext());
        searchAccess = new SearchAccess(MyApplication.getAppContext());
    }

    @Override
    public SiteBean getSiteById(int siteId) {
        return siteAccess.queryById(siteId);
    }

    @Override
    public List<ListBean> getListsBySiteId(int siteId) {
        return listAccess.queryAllById(siteId);
    }

    @Override
    public List<ListBean> getListsAllDownBySiteId(int siteId) {
        return listAccess.queryAllDownById(siteId);
    }

    @Override
    public List<ListBean> getListsByAllDown() {
        return listAccess.queryAllDown();
    }

    @Override
    public List<ListBean> getListsByAll() {
        return listAccess.queryAll();
    }

    @Override
    public List<ListBean> getListsByFavorite() {
        return listAccess.queryAllFavorite();
    }

    @Override
    public List<ListBean> getListsByRecommend() {
        return listAccess.queryAllRecommend();
    }

    @Override
    public List<ListBean> getListsByRecommendDown() {
        return listAccess.queryAllRecommendDown();
    }

    @Override
    public List<ListBean> getListsByNotRead() {
        return listAccess.queryAllNotRead();
    }

    @Override
    public List<ListBean> getListsByQueryKey(String queryKey) {
        return listAccess.queryIsShowByKey(queryKey);
    }

    @Override
    public List<ListBean> getListsDownByQueryKey(String queryKey) {
        return listAccess.queryIsDownByKey(queryKey);
    }

    @Override
    public void loadListDataBySite(final SiteBean site, final OnListDataLoadListener listener) {
        Log.d("TAG", site.siteName + " -> " + site.siteLink);
        listener.onListLoadStart();
        listener.onListLoadProgress();

        listener.onListLoadStart(site);

        List<ListBean> newLists = new ArrayList<>();

        //删除词典
        List<String> delDics = searchAccess.queryDicsByType(4);

        //取列表数据
        List<String[]> lists = HtmlHelper.getListArrayFromHtml(site, delDics);
        for (String[] arr : lists) {
            ListBean list = new ListBean();
            list.listNum = (site.isFirst == 1 ? 1 : site.listNum);
            list.isDown = 0;
            list.isDowning = 0;
            list.isRead = 0;
            list.isShow = 1;
            list.listTitle = arr[0];
            list.listLink = arr[1];
            list.siteInfo = site;

            newLists.add(list);
        }

        if (lists.size() > 0) {
            site.isUpdated = 3;

            //过滤重复项
            List<ListBean> listsAll = listAccess.queryAllById(site.siteId);
            newLists = repeatNewLists(listsAll, newLists);

            //将不重复项插入db
            if (newLists.size() > 0) {
                Collections.reverse(newLists);

                for (ListBean list : newLists) {
                    if (site.isFirst == 1) list.isNew = 1;
                    listAccess.insert(list);
                }

                site.isUpdated = 4;
                listener.onListLoadSuccess();

                if (site.isFirst == 1 && site.listNum == 1) {
                    site.listNum++;
                }
            } else {
                listener.onListLoadNoUpdate();
            }

            if (site.isFirst == 0) {
                site.listNum++;
            }

            Log.d("TAG", site.siteName + " -> Success.");
        } else {
            site.isUpdated = 2;
            listener.onListLoadError();
            Log.d("TAG", site.siteName + " -> Error!");
        }

        siteAccess.update(site);
        listener.onListLoadEnd();

        listener.onListLoadEnd(site);
    }

    //过滤列表重复
    private List<ListBean> repeatNewLists(List<ListBean> listsAll, List<ListBean> newLists) {
        for (ListBean list : listsAll) {
            for (ListBean newList : newLists) {
                if (list.listTitle.equals(newList.listTitle)) {
                    newLists.remove(newList);
                    break;
                }
            }
        }

        return newLists;
    }

    @Override
    public void downImageByList(ListBean list, int position, OnImageDownListener listener) {
        Log.d("TAG", list.listTitle + " -> " + list.listLink);

        list.isNew = 0;
        list.loadNum = 0;
        list.isDowning = 1;
        listAccess.update(list);
        listener.onDownStart(position);

        String success = "";
        List<ImageBean> images = getDownImages(list);
        list.loadNum = images.size();
        listener.onDownSuccess(position);

        if (images.size() > 0) {
            //下载图片
            for(ImageBean image : images) {
                boolean download = ImageHelper.downImage(image, list.siteInfo.domain);
                if (download) {
                    image.isDown = 1;
                    imageAccess.update(image);
                    success += "1";

                    list.downStatus = 2;
                    list.listPicture = image.imageName;
                } else {
                    success += "0";

                    list.downStatus = 3;
                }
                listAccess.update(list);

                list.loadNum--;

                //取消下载
                if (list.isDowning == 0) list.loadNum = 0;

                listener.onDownProgress(position);

                //取消下载
                if (list.isDowning == 0) break;
            }
        }

        Log.d("TAG", list.listTitle + " -> " + success);

        //是否下载成功
        if (images.size() > 0 && success.length() == images.size() && !success.contains("0")) {
            list.isDown = 1;
            list.isRead = 0;
        } else if (success.contains("1")) {
            list.isDown = 2;
            list.isRead = 0;
        } else if (!success.contains("1")) {
            if (list.isRedown == 0) {
                list.isDown = 3;
                list.listPicture = "";
            }
        } else {
            if (list.isRedown == 0) {
                list.isDown = 0;
                list.listPicture = "";
            }
        }

        list.loadNum = 0;
        list.downStatus = 0;

        //是否取消下载
        if (list.isDowning == 0) {
            listAccess.update(list);
            listener.onDownCancel();
            listener.onDownEnd(list, position);
            return;
        }

        list.isDowning = 0;
        listAccess.update(list);

        //下载成功
        if (list.isDown == 0) {
            listener.onDownError(position);
        } else {
            listener.onDownComplete(position);
        }

        listener.onDownEnd(list, position);
    }

    @Override
    public void updateList(ListBean list) {
        listAccess.update(list);
    }

    @Override
    public void updateList(List<ListBean> lists) {
        listAccess.update(lists);
    }

    @Override
    public void cancelDown() {
        listAccess.cancelDown();
    }

    @Override
    public void deleteList(ListBean list) {
        listAccess.delete(list);
    }

    @Override
    public void insertSearch(SearchBean search) {
        searchAccess.insert(search);
    }

    @Override
    public void updateIsNew(int siteId) {
        listAccess.updateIsNew(siteId);
    }

    //获取要下载的图片
    private List<ImageBean> getDownImages(ListBean list) {
        //查找未下载的图片
        List<ImageBean> images = imageAccess.queryNotDownById(list.listId);
        if (images.size() <= 0) {
            List<String> pages = new ArrayList<>();

            if(list.siteInfo.pageLevel.equals("0")) {
                pages.add(list.getListLink());
            } else {
                //取得所有页面
                pages = HtmlHelper.getPageArrayFromHtml(list);

                if (list.siteInfo.pageLevel.equals("2")) {
                    pages = HtmlHelper.getPageMany(list.listLink, pages);
                }

                //添加主页面
                pages.add(0, list.listLink);
            }

            //取得所有图片
            List<String> imgs = new ArrayList<>();
            for (String link : pages) {
                list.listLink = link;
                imgs.addAll(HtmlHelper.getImageArrayFromHtml(list));
            }

            //插入图片到数据库
            int i = 0;
            for(String link : imgs) {
                int imageId = i + 1;
                ImageBean image = new ImageBean();
                image.imageId = imageId;
                image.imageLink = link;
                image.listId = list.listId;
                image.siteId = list.siteInfo.siteId;
                image.isDown = 0;
                image.imageName = ImageHelper.getNewFileName(link, imageId);

                imageAccess.insert(image);
                images.add(image);
                i++;
            }
        }

        return images;
    }

}
