package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.AlertDialog;
import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static String TAG = MainActivity.class.getSimpleName();
    private ListView listview;
    private List<Map<String, Object>> dataList;//内容，时间
    private Button add_Diary;//按钮新增
    private TextView diary_ID ;
    private NoteDB dbHelper;
    private SQLiteDatabase db;


    private String account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView(); //初始化界面
        account = getIntent().getStringExtra("username");

    }



    private void InitView() {
        listview = findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();
        add_Diary = findViewById(R.id.editnote);

        dbHelper = new NoteDB(this, db);

        //设置监听点击事件
        listview.setOnItemClickListener(this);
        //长按
        listview.setOnItemLongClickListener(this);


        add_Diary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //点击新建日记，页面跳转到编辑日记界面
                Intent intent = new Intent(MainActivity.this, NoteEdit.class);
                intent.putExtra("editModel", "add");
                intent.putExtra("username", account);
                startActivity(intent);
            }
        });
    }


    //重新加载listView的资源显示
    @Override
    protected void onStart() {
        super.onStart();
        show_DairyList();
    }


    private void show_DairyList(){   //显示主界面日记列表
        dbHelper.create_db();
        Cursor cursor;
        Bundle bundle = new Bundle();
        cursor=dbHelper.query_db(account);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item,
                cursor,
                new String[]{"_id", "title","author", "time"}, new int[]{R.id.diary_ID, R.id.diary_Title,R.id.et_author, R.id.diary_Time});
        listview.setAdapter(adapter);
        dbHelper.close_db();
    }

    //点击列表中已存在的日记
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        diary_ID = view.findViewById(R.id.diary_ID);
//        account = getIntent().getStringExtra("username");
        int diary_Id = Integer.parseInt(diary_ID.getText().toString());    //获取所点击日记ID
        Intent intent = new Intent(MainActivity.this, NoteEdit.class);
        intent.putExtra("editModel", "update");
        intent.putExtra("noteId", diary_Id);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    @Override
    //长按已存在的日记
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        diary_ID = view.findViewById(R.id.diary_ID);
        final int diary_Id = Integer.parseInt(diary_ID.getText().toString());
        Builder builder = new Builder(this);    //AlertDialog(对话框)
        builder.setTitle("删除日记");           //弹出窗口标题
        builder.setMessage("确认删除日记吗？");     //弹出窗口
        builder.setCancelable(true);           //点击对话框之外的地方，对话框消失
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {    //确定删除日记
                dbHelper.create_db();
                dbHelper.delete_db(diary_Id);
                dbHelper.close_db();
                listview.invalidate();      //重绘所有组件
                show_DairyList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消删除日记
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "删除对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "删除对话框消失了");
            }
        });
        dialog.show();
        return true;
    }



}
