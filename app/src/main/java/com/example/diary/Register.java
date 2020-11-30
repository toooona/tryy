package com.example.diary;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Register  extends AppCompatActivity{
    private SharedPreferences pref;
    private EditText account;
    private EditText Pwd1;
    private EditText Pwd2;
    private Button confirm;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pref= getSharedPreferences("data",MODE_PRIVATE);
        account = findViewById(R.id.account);
        Pwd1 = findViewById(R.id.Pwd1);
        Pwd2 = findViewById(R.id.Pwd2);
        Pwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Pwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String acc=account.getText().toString();
                String old_Pwd1 = Pwd1.getText().toString();
                String old_Pwd2 = Pwd2.getText().toString();
                //获取用户名和密码
                String password = new MD5(old_Pwd1).transform();
                //使用MD5加密
                if(acc.equals("")){ //  用户名为空
                    Toast.makeText(Register.this, "请填写用户名！", Toast.LENGTH_SHORT).show();
                }
                else if(old_Pwd1.equals("")) {   //密码为空
                    Toast.makeText(Register.this, "请填写密码！", Toast.LENGTH_SHORT).show();
                }
                else if(!old_Pwd2.equals(old_Pwd1)) {   //密码为空
                    Toast.makeText(Register.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
                else if (pref.getString("username"+acc, "").equals("")) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username"+acc, acc);
                    editor.putString("password"+acc, password);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Register.this.finish();//结束当前活动
                }
                else {
                    Toast.makeText(getApplicationContext(), "用户名已经存在", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //结束当前活动
    }


}
