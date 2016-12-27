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
    private static final String TAG = "ImageContentSQLHelper";
    private static final String DATABASE_NAME = "IMAGE_DATA.db";
    private static final int DATABASE_VERSION = 1;
    SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String KEY_ID = "key";
    private static final String KEY_RESOURCE = "resource";
    private static final String KEY_EXPIRE = "expire";

    @SuppressLint({"SimpleDateFormat"})
    public ImageContentDB(Context applicationContext) {
        super(applicationContext, "IMAGE_DATA.db", (SQLiteDatabase.CursorFactory)null, 1);
        this.mFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_IMGCONTENT_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS ImageContentCache" +
                "(seq INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", web_link TEXT" +
                ", img_url TEXT" +
                ", title TEXT )";
//        String CREATE_CHECKUPDATE_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS CheckUpdateCache(seq_id INTEGER PRIMARY KEY AUTOINCREMENT, promotion_id INTEGER NOT NULL, json TEXT NOT NULL, expire DATE)";
//        String CREATE_PROMOTION_VISIVILITY_TABLE = " create table if not exists promotion_visivility  ( adspace_name text not null primary key,    visible_type text not null,    regdate      datetime not null default CURRENT_TIMESTAMP )";
        sqLiteDatabase.execSQL(CREATE_IMGCONTENT_CACHE_TABLE);
//        sqLiteDatabase.execSQL(CREATE_CHECKUPDATE_CACHE_TABLE);
//        sqLiteDatabase.execSQL(CREATE_PROMOTION_VISIVILITY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        String DROP_TABLE_IF_IMGCONTENT_CACHE_EXIST = "DROP TABLE IF EXSITS ImageContentCache";
//        String DROP_TABLE_IF_CHECKUPDATE_CACHE_EXIST = "DROP TABLE IF EXSITS CheckUpdateCache";
//        String DROP_PROMOTION_VISIVILITY_TABLE = "DROP TABLE IF EXSITS promotion_visivility";
        sqLiteDatabase.execSQL(DROP_TABLE_IF_IMGCONTENT_CACHE_EXIST);
//        sqLiteDatabase.execSQL(DROP_TABLE_IF_CHECKUPDATE_CACHE_EXIST);
//        sqLiteDatabase.execSQL(DROP_PROMOTION_VISIVILITY_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    public long addImgContent(ImageContent.OneImageItem oneImg) {
        SQLiteDatabase db = null;
        long result = -1L;

        try {
            db = this.getWritableDatabase();
            ContentValues e = new ContentValues();
            e.put("web_link", oneImg.web_link);
            e.put("img_url", oneImg.img_url);
            e.put("title", oneImg.title);
            result = db.insert("ImageContentCache", (String)null, e);
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@addImgContent" + e.getMessage());
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }

        }

        return result;
    }

    public void deleteImgContent(String seq) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.delete("ImageContentCache", "seq = ?", new String[]{seq});
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@deleteImgContent" + e.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

        }

    }

    public List<ImageContent.OneImageItem> getImgContentList(String column, String keyword) {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        ArrayList list = new ArrayList();
        ArrayList<ImageContent.OneImageItem> resultList = new ArrayList<ImageContent.OneImageItem>();

        try {
            String e = " select seq, title, img_url, web_link FROM ImageContentCache ";
            if(StringUtil.isEmpty(keyword) != true){
                e = e + "where " + column + " = " + keyword;
            }
            db = this.getReadableDatabase();
            cursor = db.rawQuery(e, (String[])null);

            while(cursor.moveToNext()) {
                StringBuffer buffer = new StringBuffer();

                ImageContent.OneImageItem item = new ImageContent.OneImageItem(cursor.getString(1), cursor.getString(2), cursor.getString(3));

                Log.d(TAG, "jjw : " + item.toString());

                resultList.add(item);
            }
        } catch (Exception e) {
            Tracer.error("ImageContentSQLHelper", "@getImgContentList" + e.getMessage());
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










    public boolean isPromotionVisible(String adspaceName) {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        int count = 0;

        try {
            String e = " select count(1) FROM promotion_visivility  where adspace_name = \'" + adspaceName + "\' " + " and ( visible_type = \'INVISIBLE_FOREVER\' or " + "       (visible_type = \'INVISIBLE_24HR\' and regdate > datetime(\'now\', \'-1 day\'))  " + "     )";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(e, (String[])null);
            if(cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception var9) {
            Tracer.error("ImageContentSQLHelper", "@isPromotionVisible" + var9.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

            if(cursor != null) {
                cursor.close();
            }

        }

        return count <= 0;
    }

    public List<String> getBlockedPromotionList() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        ArrayList list = new ArrayList();

        try {
            String e = " select adspace_name, visible_type, regdate FROM promotion_visivility ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(e, (String[])null);

            while(cursor.moveToNext()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(cursor.getString(0)).append("+");
                buffer.append(cursor.getString(1)).append("+");
                buffer.append(cursor.getString(2));
                list.add(buffer.toString());
            }
        } catch (Exception var9) {
            Tracer.error("ImageContentSQLHelper", "@getBlockedPromotionList" + var9.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

            if(cursor != null) {
                cursor.close();
            }

        }

        return list;
    }

    public void cleanupExpiredResource() {
        this.cleanupExpiredResourceCache();
    }

    private void cleanupExpiredResourceCache() {
        SQLiteDatabase db = null;
        Date currentDate = Calendar.getInstance().getTime();

        try {
            db = this.getWritableDatabase();
            db.delete("ResourceCache", "expire < ?", new String[]{String.valueOf(this.mFormatter.format(currentDate))});
        } catch (Exception var7) {
            Tracer.error("ImageContentSQLHelper", var7.getMessage());
        } finally {
            if(db != null) {
                db.close();
            }

        }

    }
}
