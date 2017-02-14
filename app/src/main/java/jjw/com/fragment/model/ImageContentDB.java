package jjw.com.fragment.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.toast.android.analytics.common.utils.StringUtil;
import com.toast.android.analytics.common.utils.Tracer;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by JJW on 2016-12-27.
 */


public class ImageContentDB extends SQLiteOpenHelper {
    private static final String TAG = "ImageContentDB";
    private static final String DATABASE_NAME = "IMAGE_DATA.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "key";
    private static final String KEY_RESOURCE = "resource";
    private static final String KEY_EXPIRE = "expire";

    @SuppressLint({"SimpleDateFormat"})
    public ImageContentDB(Context applicationContext) {
            super(applicationContext, "IMAGE_DATA.db", (SQLiteDatabase.CursorFactory)null, DATABASE_VERSION);
        Log.d(TAG, "ImageContentDB");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "jjw onCreate");
        System.out.println("jjw ImageContentDB oncreate");
        String CREATE_IMGCONTENT_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS ImageContentCache" +
                "(seq INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", food_stuffs TEXT" +
                ", web_link TEXT" +
                ", img_url TEXT" +
                ", title TEXT )";
        String CREATE_CHECKUPDATE_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS CheckUpdateCache(update_time LONG NULL)";
//        String CREATE_PROMOTION_VISIVILITY_TABLE = " create table if not exists promotion_visivility  ( adspace_name text not null primary key,    visible_type text not null,    regdate      datetime not null default CURRENT_TIMESTAMP )";
        sqLiteDatabase.execSQL(CREATE_IMGCONTENT_CACHE_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHECKUPDATE_CACHE_TABLE);
//        sqLiteDatabase.execSQL(CREATE_PROMOTION_VISIVILITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        String DROP_TABLE_IF_IMGCONTENT_CACHE_EXIST = "DROP TABLE IF EXISTS ImageContentCache";
        String DROP_TABLE_IF_CHECKUPDATE_CACHE_EXIST = "DROP TABLE IF EXISTS CheckUpdateCache";
//        String DROP_PROMOTION_VISIVILITY_TABLE = "DROP TABLE IF EXISTS promotion_visivility";
        sqLiteDatabase.execSQL(DROP_TABLE_IF_IMGCONTENT_CACHE_EXIST);
        sqLiteDatabase.execSQL(DROP_TABLE_IF_CHECKUPDATE_CACHE_EXIST);
//        sqLiteDatabase.execSQL(DROP_PROMOTION_VISIVILITY_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    public long addImgContent(ImageContent.OneImageItem oneImg) {
        SQLiteDatabase db = null;
        long result = -1L;

        try {
            db = this.getWritableDatabase();
            ContentValues e = new ContentValues();
            e.put("food_stuffs", oneImg.food_stuffs);
            e.put("web_link", oneImg.web_link);
            e.put("img_url", oneImg.img_url);
            e.put("title", oneImg.title);
            result = db.insert("ImageContentCache", (String)null, e);
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@addImgContent" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }

        }

        return result;
    }

    public void deleteImgContent() {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.delete("ImageContentCache", null, null);
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@deleteImgContent" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

        }

    }

    /**
     *
     * @param column : title, web_link, img_url
     * @param keyword
     * @return
     */
    public List<ImageContent.OneImageItem> getImgContentList(String column, String keyword) {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        ArrayList list = new ArrayList();
        ArrayList<ImageContent.OneImageItem> resultList = new ArrayList<ImageContent.OneImageItem>();

        try {
            String sqlquery = " select seq, title, img_url, web_link, food_stuffs FROM ImageContentCache ";
            if(StringUtil.isEmpty(keyword) != true){
                sqlquery = sqlquery + "where " + column + " like '%" + keyword + "%'";
            }
            Log.d(TAG, sqlquery);

            db = this.getReadableDatabase();
            cursor = db.rawQuery(sqlquery, (String[])null);

            while(cursor.moveToNext()) {

                ImageContent.OneImageItem item = new ImageContent.OneImageItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                resultList.add(item);
            }
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@getImgContentList" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

            if(cursor != null) {
                cursor.close();
            }

        }

        return resultList;
    }

    public long getLastUpdateMilliTime() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        ArrayList list = new ArrayList();
        ArrayList<ImageContent.OneImageItem> resultList = new ArrayList<ImageContent.OneImageItem>();

        try {
            String e = " select update_time FROM CheckUpdateCache ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(e, (String[])null);
            cursor.moveToLast();

            return cursor.getLong(0);

        } catch (Exception e) {
            Tracer.error("CheckUpdateCacheSQLHelper", "@getLastUpdateMilliTime" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

            if(cursor != null) {
                cursor.close();
            }

        }

        return 0;
    }


    public long addLastUpdateMilliTime(long updateTime) {

        SQLiteDatabase db = null;
        long result = -1L;

        try {
            db = this.getWritableDatabase();
            ContentValues e = new ContentValues();
            e.put("update_time", updateTime);
            result = db.insert("CheckUpdateCache", (String)null, e);
        } catch (Exception e) {
            Tracer.error("CheckUpdateCacheSQLHelper", "@addLastUpdateMilliTime" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }

        }

        return result;
    }

    public long updateLastUpdateMilliTime(long updateTime) {
        SQLiteDatabase db = null;
        long result = -1L;

        try {
            db = this.getWritableDatabase();
            ContentValues e = new ContentValues();
            e.put("update_time", updateTime);
            result = db.update("CheckUpdateCache", e, null, null);
        } catch (Exception e) {
            Tracer.error("CheckUpdateCacheSQLHelper", "@addLastUpdateMilliTime" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }

        }

        return result;
    }

    public void deleteLastUpdateMilliTime() {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.delete("CheckUpdateCache", null, null);
        } catch (Exception e) {
            Tracer.error("CheckUpdateCacheSQLHelper", "@deleteLastUpdateMilliTime" + e.getMessage());
            Log.e(TAG, e.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

        }
    }


}
