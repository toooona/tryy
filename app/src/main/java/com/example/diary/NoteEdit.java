package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.database.Cursor;



public class NoteEdit extends AppCompatActivity implements View.OnClickListener{
    private TextView diary_Time;
    private NoteDB dbHelper;
    private TextView account;
    private EditText et_title;
    private EditText et_author;
    private EditText et_content;
    private Button confirm;
    private ImageView imageView;
    Intent intent;
    String editModel = null;
    int diary_Id;
    private SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Init_EditView();
    }

    private void Init_EditView() {  //初始化日记编辑界面
        diary_Time = findViewById(R.id.diary_Time);
        account = findViewById(R.id.account);
        et_title = findViewById(R.id.et_title);
        et_author = findViewById(R.id.et_author);
        et_content= findViewById(R.id.et_content);
        confirm = findViewById(R.id.confirm);

        dbHelper = new NoteDB(this,db);

        intent = getIntent();
        editModel = intent.getStringExtra("editModel");
        //获取编辑日记模式（新建/更改）
        diary_Id = intent.getIntExtra("noteId", 0);
        //获取列表中所点击日记ID
        loadData();
        Date date = new Date(); //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //设置时间格式
        String current_Time = sdf.format(date);   //格式化时间
        diary_Time.setText(current_Time);

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm: //确认更改/添加的信息
                String title= et_title.getText().toString();
                String author= et_author.getText().toString();
                String content = et_content.getText().toString();
                String acc = account.getText().toString();

                if(title.isEmpty()){    //设置标题长度小于20
                    Toast.makeText(NoteEdit.this, "标题不能为空！", Toast.LENGTH_LONG).show();
                }
                else if(title.length() > 20){    //设置标题长度小于20
                    Toast.makeText(NoteEdit.this, "日记标题不能超过20字", Toast.LENGTH_LONG).show();
                }
                else if(content.isEmpty()){
                    Toast.makeText(NoteEdit.this, "内容不能为空!", Toast.LENGTH_LONG).show();
                }
                else{
                    String time = (String) diary_Time.getText();
                    dbHelper.create_db();

                    if(editModel.equals("add")){    //新建日记
                        acc = getIntent().getStringExtra("username");
                        if(author.equals(""))
                            author = acc;
                        dbHelper.insert_db(acc, title, author, content, time);

                    }
                    else if(editModel.equals("update")){    //更新日记
                        dbHelper.update_db(diary_Id, acc, title, author, content, time);
                    }
                    dbHelper.close_db();//关闭数据库
                    NoteEdit.this.finish();//结束当前活动
                }
                break;

        }
    }

    private void loadData(){
        if(editModel.equals("update")){ //若为更新内容，从数据库中提取内容
            dbHelper.create_db();
            Cursor cursor = dbHelper.query_db(diary_Id);
            cursor.moveToFirst();

            //setText需要设置光标的位置到最后
            //append()附加到字符串的最后面无需设置光标的位置
            String acc = cursor.getString(cursor.getColumnIndex("account"));
            account.append(acc);
            String title = cursor.getString(cursor.getColumnIndex("title"));
            et_title.append(title);
            String author = cursor.getString(cursor.getColumnIndex("author"));
            et_author.append(author);
            String content = cursor.getString(cursor.getColumnIndex("content"));
            et_content.append(content);
            dbHelper.close_db();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageView.setImageURI(data.getData());

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

