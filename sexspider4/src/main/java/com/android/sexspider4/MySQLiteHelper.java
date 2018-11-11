package com.android.sexspider4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by feng on 2017/5/5.
 */

class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sexspider4.db";
    private static final int DB_VERSION = 13;
    private static final String SITE_TABLE = "SiteTable";
    private static final String LIST_TABLE = "ListTable";
    private static final String IMAGE_TABLE = "ImageTable";
    private static final String SEARCH_TABLE = "SearchTable";
    private static MySQLiteHelper sqlHelper = null;

    private MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static SQLiteDatabase getInstance(Context context) {
        if(sqlHelper == null) {
            sqlHelper = new MySQLiteHelper(context);
        }
        return sqlHelper.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSiteTable(db);
        db.execSQL("CREATE TABLE " + LIST_TABLE + " ("
                + "ListID         INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "ListTitle      VARCHAR(100)     NOT NULL, "
                + "ListLink       VARCHAR(200)     NOT NULL, "
                + "ListPicture    VARCHAR(50)      NULL, "
                + "IsFavorite     INTEGER          DEFAULT 0, "
                + "ListRank       INTEGER          DEFAULT 0, "
                + "SiteID         INTEGER          NOT NULL, "
                + "ListNum        INTEGER          DEFAULT 1, "
                + "IsDown         INTEGER          DEFAULT 0, "
                + "IsDowning      INTEGER          DEFAULT 0, "
                + "IsShow         INTEGER          DEFAULT 1, "
                + "IsNew          INTEGER          NULL, "
                + "IsRead         INTEGER          DEFAULT 0, "
                + "ListDate       TIMESTAMP        DEFAULT (datetime('now', '+8 hour')), "
                + "ListNotes      VARCHAR(200)     NULL) ");
        db.execSQL("CREATE TABLE " + IMAGE_TABLE + " ("
                + "ImageID        INTEGER          NOT NULL, "
                + "ImageName      VARCHAR(100)     NOT NULL, "
                + "ImageLink      VARCHAR(200)     NOT NULL, "
                + "ListID         INTEGER          NOT NULL, "
                + "SiteID         INTEGER          NOT NULL, "
                + "IsDown         INTEGER          DEFAULT 0, "
                + "ImageNotes     VARCHAR(200)     NULL) ");
        db.execSQL("CREATE TABLE " + SEARCH_TABLE + " ("
                + "SearchID       INTEGER          PRIMARY KEY         AUTOINCREMENT, "
                + "SearchName     VARCHAR(100)     NOT NULL, "
                + "SearchType     INTEGER          DEFAULT 0) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN DocType VARCHAR(10) NULL");
                    break;
                case 2:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN ListNotes VARCHAR(200) NULL");
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " RENAME TO " + SITE_TABLE + "_1");
                    createSiteTable(db);
                    db.execSQL("INSERT INTO " + SITE_TABLE + " SELECT * FROM " + SITE_TABLE + "_1");
                    db.execSQL("DROP TABLE " + SITE_TABLE + "_1");

                    db.execSQL("ALTER TABLE " + LIST_TABLE + " ADD COLUMN ListNotes VARCHAR(200) NULL");
                    db.execSQL("ALTER TABLE " + IMAGE_TABLE + " ADD COLUMN ImageNotes VARCHAR(200) NULL");
                    break;
                case 3:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN LastStart VARCHAR(50) NULL");
                    break;
                case 4:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN MainDiv VARCHAR(50) NULL");
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN ThumbDiv VARCHAR(50) NULL");
                    break;
                case 5:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN SiteFilter VARCHAR(50) NULL");
                    break;
                case 6:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN SiteReplace VARCHAR(1000) NULL");
                    break;
                case 7:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN LoadNum INTEGER DEFAULT 1");
                    break;
                case 8:
                    db.execSQL("ALTER TABLE " + SITE_TABLE + " ADD COLUMN LoadLink VARCHAR(200) NULL");
                    break;
                case 9://更新此版本时，page没有处理，导致下载页数为0
                    db.execSQL("DROP TABLE " + SITE_TABLE);
                    createSiteTable(db);
                    break;
                case 10://处理page，默认为30
                    db.execSQL("UPDATE " + SITE_TABLE + " SET LoadNum = 30");
                    break;
                case 11://处理page，默认为30
                    db.execSQL("UPDATE " + SITE_TABLE + " SET ListNum = 30");
                    break;
                case 12://处理page，默认为30
                    db.execSQL("UPDATE " + SITE_TABLE + " SET ListNum = 31");
                    break;
            }
        }
    }

    private void createSiteTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SITE_TABLE + " ("
                + "SiteID         INTEGER          PRIMARY KEY, "
                + "SiteRank       VARCHAR(10)      NULL, "
                + "VipLevel       INTEGER          NOT NULL, "
                + "IsHided        INTEGER          DEFAULT 0, "
                + "SiteName       VARCHAR(50)      NOT NULL, "
                + "ListPage       VARCHAR(200)     NOT NULL, "
                + "PageEncode     VARCHAR(10)      NOT NULL, "
                + "Domain         VARCHAR(100)     NOT NULL, "
                + "SiteLink       VARCHAR(200)     NOT NULL, "
                + "LoadLink       VARCHAR(200)     NULL, "
                + "SiteFilter     VARCHAR(50)      NULL, "
                + "SiteReplace    VARCHAR(1000)    NULL, "
                + "MainDiv        VARCHAR(50)      NULL, "
                + "ThumbDiv       VARCHAR(50)      NULL, "
                + "ListDiv        VARCHAR(50)      NOT NULL, "
                + "ListFilter     VARCHAR(50)      NOT NULL, "
                + "ImageDiv       VARCHAR(50)      NOT NULL, "
                + "ImageFilter    VARCHAR(50)      NOT NULL, "
                + "PageLevel      VARCHAR(50)      NULL, "
                + "PageDiv        VARCHAR(50)      NULL, "
                + "PageFilter     VARCHAR(50)      NULL, "
                + "ListNum        INTEGER          DEFAULT 1, "
                + "LoadNum        INTEGER          DEFAULT 1, "
                + "IsUpdated      INTEGER          DEFAULT 0, "
                + "IsDowning      INTEGER          DEFAULT 0, "
                + "SiteDate       TIMESTAMP        DEFAULT (datetime('now', '+8 hour')), "
                + "DocType        VARCHAR(10)      NULL, "
                + "SiteNotes      VARCHAR(200)     NULL, "
                + "LastStart      VARCHAR(50)      NULL) ");
    }
}
