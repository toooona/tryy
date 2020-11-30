package com.example.diary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class NoteDB {
    private SQLiteDatabase db;
    private Context context;
    public NoteDB(Context context, SQLiteDatabase db) {
        this.db = db;
        this.context = context;
    }
    public void create_db(){//创建或打开数据库
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString()+"/myDiary.db3", null);
//        db.execSQL("DROP TABLE IF EXISTS diary");
        if(db == null){
            Toast.makeText(context,"数据库创建不成功",Toast.LENGTH_LONG).show();
        }
        db.execSQL("create table if not exists diary(_id integer primary key autoincrement," +
                "account varchar(30)," +
                "title varchar(100)," +
                "author text,"+
                "content text," +
                "time varchar(20))");//建表
    }

    public Cursor query_db(int diary_Id){
        Cursor cursor = db.rawQuery("select * from diary where _id='"+diary_Id+"';",null);
        return cursor;//定义一个id方便后面查询删除
    }

    public void insert_db(String account, String title,String author,String text,String time){
        if(text.isEmpty()){
            Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
        }
        else{
            db.execSQL("insert into diary(account,title,author,content,time) values('"+ account +"','"+ title+"','"+ author+ "','"+ text+ "','"+time+"');");
        }
    }

    public void update_db(int diary_Id, String account, String title,String author,String text,String time){//改
        if( text.isEmpty()){
            Toast.makeText(context, "各字段不能为空", Toast.LENGTH_LONG).show();
        }
        else{
            db.execSQL("update diary set account='" + account + "',content='"+text+ "',title='"+title+"',author='"+author+"',time='"+time+"'where _id='" + diary_Id+"'");
        }
    }

    public void delete_db(int diary_Id){
        db.execSQL("delete from diary where _id='" + diary_Id+"'");
    }

    public Cursor query_db(String account){
        Cursor cursor = db.rawQuery("select * from diary where account='" + account + "' order by time desc",null);
        return cursor;
    }


    public void close_db(){
        db.close();
    } //关闭数据库
}
