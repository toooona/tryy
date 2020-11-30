package com.example.diary;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity{

    private SharedPreferences pref;
    private SharedPreferences savePref;
    //调用SharedPreferences对象的edit()方法来获取一个SharedPreferences.Editor对象，用以添加要保存的数据
    private Button login;
    private Button register;
    private EditText accEdit;   //用户名
    private EditText pwdEdit;   //密码

    private CheckBox showPwd;//显示或隐藏密码复选框
    private CheckBox remPwd;//显示或隐藏密码复选框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref= getSharedPreferences("data", MODE_PRIVATE);
        savePref = getSharedPreferences("rem_Data", MODE_PRIVATE);
        //获取SharedPreferences对象（保存用户信息）
        accEdit=findViewById(R.id.admin);
        pwdEdit=findViewById(R.id.password);
        login=findViewById(R.id.login_button);
        register=findViewById(R.id.register_button);
        showPwd=findViewById(R.id.show_password);
        remPwd=findViewById(R.id.rem_Pwd);
        //获取当前“是否保存密码”的状态

        if(savePref.getBoolean("Auto", false)){ //判断用户是否选择了记住密码
            accEdit.setText(savePref.getString("Account", ""));
            pwdEdit.setText(savePref.getString("Password", ""));
            remPwd.setChecked(true);
        }
        showOrHide(pwdEdit, false);

        //登录监听器
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String account=accEdit.getText().toString();
                String old_Pwd=pwdEdit.getText().toString();
                //获取用户名和密码
                String password =  new MD5(old_Pwd).transform();;
                //使用MD5加密

                if (password.equals(pref.getString("password"+account,null))
                   &&account.equals(pref.getString("username"+account,""))){
                    //输入用户名、密码正确
                    Intent intent = new Intent();
                    intent.putExtra("username", account);
                    intent.setClass(Login.this, MainActivity.class);
                    startActivity(intent);
                }else { //输入用户名、密码错误
                    Toast.makeText(getApplicationContext(), "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //注册监听器
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        //用户点击'显示密码'复选框
        showPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPwd.isChecked()){
                    showOrHide(pwdEdit,true);
                }else{
                    showOrHide(pwdEdit,false);
                }
            }
        });

        remPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = accEdit.getText().toString();
                String pwd = pwdEdit.getText().toString();
                if(remPwd.isChecked()){
                    if(!acc.isEmpty() && !pwd.isEmpty())
                        isRemPwd(acc, pwd, true);
                }
                else
                    isRemPwd(null, null, false);

            }
        });


    }


    //显示或隐藏密码
    private void showOrHide(EditText pwdEdit,boolean isShow){

        //记住光标开始的位置
        int pos = pwdEdit.getSelectionStart();
        if(isShow){
            pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        pwdEdit.setSelection(pos);
    }

    private void isRemPwd(String acc, String pwd, boolean isSave){
        SharedPreferences.Editor editor = savePref.edit();
        if(isSave){
            editor.putString("Account", acc);
            editor.putString("Password", pwd);
            editor.putBoolean("Auto", true);
        }else{
            editor.putString("Account", null);
            editor.putString("Password", null);
            editor.putBoolean("Auto", false);
        }
        editor.commit();
    }


}