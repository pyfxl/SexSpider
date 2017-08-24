package com.android.sexspider4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by feng on 2017/5/5.
 */

public class BaseAccess {
    protected static final String TABLE_SITE = "SiteTable";
    protected static final String TABLE_LIST = "ListTable";
    protected static final String TABLE_SEARCH = "SearchTable";

    protected SQLiteDatabase db;

    public BaseAccess(Context context) {
        this.db = MySQLiteHelper.getInstance(context);
    }

}
