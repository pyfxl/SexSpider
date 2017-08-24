package com.fxlweb.mydemo.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fxlweb.mydemo.MySQLiteHelper;

/**
 * Created by feng on 2016/6/8.
 */

public class SQLiteCommon {
    public SQLiteDatabase db = null;

    public SQLiteCommon(Context context) {
        this.db = MySQLiteHelper.getInstance(context);
    }

}
