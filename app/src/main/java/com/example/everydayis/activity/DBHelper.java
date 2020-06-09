package com.example.everydayis.activity;

/**
 * Author:七月无雨
 * Data:${DATA}
 * DESC:
 * 这个类主要用于建立数据库和更新数据库
 * 实例化这个类的时候构造函数传入上下文，并继承父类构造函数，
 * 数据库名称和版本号都已经写成了常量。onCreate()方法在创建时和创建后只会调用一次，
 * 所以在这里面创建一次数据表就行了，
 * 而onUpgrade()方法只有在数据库版本号大于上次传入的值时调用，
 * 用于对数据库进行更新操作，如新建表，更改列等。
 **/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Android专门提供了一个SQLiteOpenHelper帮助类，可以非常简单地对数据库进行创建和升级。
public class DBHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME="diary.db";  //数据库名
    private static int DATABASE_VERSION=1;   //数据库的版本号
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //建立数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_diary(_id integer primary key autoincrement,title verchar(20),content verchar(1000),pubdate)");
        //建立文章内容的数据库
        db.execSQL("create table tb_diarycontent(_id integer primary key autoincrement,content1 verchar(1000),pubdate)");
    }
    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


