package com.example.everydayis.activity;
/**
 * DESC:
 * Author:七月无雨
 * Data: 专门对数据库进行增删查改操作
 *       目的就是为了与主要代码分开，便于增删查改的操作
 **/
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DiaryService {
    private DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase = null;
    public DiaryService(Context context) {
        dbHelper = new DBHelper(context);
    }
    /**
     * 保存日记
     *
     */
    public void save(Diary diary) {
        //我们先调用帮助类的getWritableDatabase()方法，获取可写入的权限。
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "insert into tb_diary(title,content,pubdate) values (?,?,?)";
        sqLiteDatabase.execSQL(sql, new String[] { diary.getTitle(), diary.getContent(), diary.getPubdate() });
        sqLiteDatabase.close();
    }
    /**
     * 查询日记
     *
     */
    public List<Diary> getAllDiary() {
        Diary diary = null;
        List<Diary> diaryList = new ArrayList<Diary>();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        // 得到游标，最多只有一条数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from tb_diary", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
            diary = new Diary(id, title, content, pubdate);
            diaryList.add(diary);
        }
        cursor.close();
        sqLiteDatabase.close();
        return diaryList;
    }
    /**
     * 根据id删除日记
     *
     * @param id
     * 日记的id号
     */
    public void delete(Integer id) {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.execSQL("delete from tb_diary where _id=?",
                new Object[] { id });
        sqLiteDatabase.close();
    }

    /**
     * 更新日记
     *
     * @param diary
     */
    public void update(Diary diary) {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.execSQL(
                "update tb_diary set title=?,content=?,pubdate=? where _id=?",
                new Object[] { diary.getTitle(), diary.getContent(),
                        diary.getPubdate(), diary.getId() });
        sqLiteDatabase.close();

    }


}

