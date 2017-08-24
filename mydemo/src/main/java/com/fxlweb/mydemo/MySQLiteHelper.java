package com.fxlweb.mydemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by feng on 2016/6/8.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sexspider3.db";
    private static final int DB_VERSION = 6;
    private static final String SITE_TABLE = "SiteTable";
    private static final String LIST_TABLE = "ListTable";
    private static final String IMAGE_TABLE = "ImageTable";
    private static MySQLiteHelper sqlHelper = null;

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SQLiteDatabase getInstance(Context context) {
        if(sqlHelper == null) {
            sqlHelper = new MySQLiteHelper(context);
        }
        return sqlHelper.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SITE_TABLE + " ("
                + "SiteID         INTEGER          PRIMARY KEY, "
                + "SiteRank       VARCHAR(10)      NULL, "
                + "Domain         VARCHAR(50)      NOT NULL, "
                + "SiteLink       VARCHAR(200)     NOT NULL, "
                + "SiteName       VARCHAR(50)      NOT NULL, "
                + "ListPage       VARCHAR(20)      NOT NULL, "
                + "PageEncode     VARCHAR(10)      NOT NULL, "
                + "ListNum        INTEGER          DEFAULT 1, "
                + "ListStart      VARCHAR(50)      NOT NULL, "
                + "ListEnd        VARCHAR(50)      NOT NULL, "
                + "ImageStart     VARCHAR(50)      NOT NULL, "
                + "ImageEnd       VARCHAR(50)      NOT NULL, "
                + "OnInclude      VARCHAR(50)      NULL, "
                + "NotInclude     VARCHAR(50)      NULL, "
                + "PageStart      VARCHAR(50)      NULL, "
                + "PageEnd        VARCHAR(50)      NULL, "
                + "IsHided        INTEGER          DEFAULT 0, "
                + "IsUpdated      INTEGER          DEFAULT 0, "
                + "IsDowning      INTEGER          DEFAULT 0) ");
        db.execSQL("CREATE TABLE " + LIST_TABLE + " ("
                + "ListID         INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "ListTitle      VARCHAR(100)     NOT NULL, "
                + "ListLink       VARCHAR(200)     NOT NULL, "
                + "ListPicture    VARCHAR(50)      NULL, "
                + "IsRecommend    INTEGER          NULL, "
                + "SiteID         INTEGER          NOT NULL, "
                + "ListNum        INTEGER          DEFAULT 1, "
                + "IsDown         INTEGER          DEFAULT 0, "
                + "IsDowning      INTEGER          DEFAULT 0, "
                + "IsShow         INTEGER          DEFAULT 1, "
                + "IsRead         INTEGER          DEFAULT 0)");
        db.execSQL("CREATE TABLE " + IMAGE_TABLE + " ("
                + "ImageID        INTEGER          NOT NULL, "
                + "ImageName      VARCHAR(100)     NOT NULL, "
                + "ImageLink      VARCHAR(200)     NOT NULL, "
                + "ListID         INTEGER          NOT NULL, "
                + "SiteID         INTEGER          NOT NULL, "
                + "IsDown         INTEGER          DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i=oldVersion; i<newVersion; i++) {
            switch (i) {
                case 4:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD SiteRank VARCHAR(10) NULL");
                    break;
                case 5:
                    db.execSQL("ALTER TABLE " + LIST_TABLE + " ADD ListPicture VARCHAR(50) NULL");
                    db.execSQL("ALTER TABLE " + LIST_TABLE + " ADD IsRecommend INTEGER NULL");
                    break;
            }
        }
    }
}
