package com.xiaomi.problemtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaomi.problemtest.bean.User;
import com.xiaomi.problemtest.util.MOKHttpTools;
import com.xiaomi.problemtest.util.MyToast;
import com.xiaomi.problemtest.util.Tools;

public class LoginActivity extends AppCompatActivity {

    private TextView tvIcon;
    private EditText tvUserAccount;
    private EditText tvUserPassword;
    private TextView tvLogin;
    private TextView tvReset;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                MyToast.showText(LoginActivity.this,"登录成功");
                Intent intent = new Intent(LoginActivity.this,PublishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",new User(tvUserAccount.getText().toString().trim(),
                                tvUserPassword.getText().toString().trim()));
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                MyToast.showText(LoginActivity.this,"登录失败");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setViewEventListent();

    }

    private void setViewEventListent() {
        // 点击图标跳转注册页面
//        tvIcon.setOnClickListener(v -> {
//            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            finish();//关闭并销毁当前activity
//        });

        tvUserAccount.setOnFocusChangeListener((v, hasFocus) -> {

            Log.d("TAG", "hasFocus ");
            if(!hasFocus){
                String userAccount = tvUserAccount.getText() == null ? "" : tvUserAccount.getText().toString().trim();
                //验证数
                if (!Tools.isMobile(userAccount)) {
                    tvUserAccount.setError("请输入正确的手机号");
                    return;
                }
            }
        });

        tvUserPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String userPassword = tvUserPassword.getText() == null ? "" : tvUserPassword.getText().toString().trim();
                //验证数
                if (userPassword.length() < 6) {
                    tvUserPassword.setError("密码长度应该大于等于6");
                    return;
                }
            }
        });

        // 点击登录
        tvLogin.setOnClickListener(v -> {
            //获取手机号
            String userAccount = tvUserAccount.getText() == null ? "" : tvUserAccount.getText().toString().trim();
            String userPassword = tvUserPassword.getText() == null ? "" : tvUserPassword.getText().toString().trim();
            //验证数
            if (!Tools.isMobile(userAccount)) {
                tvUserAccount.setError("请输入正确的手机号");
                return;
            }
            if (userPassword.length() < 6) {
                tvUserPassword.setError("密码长度应该大于等于6");
                return;
            }
            String url = "http://mini-group7.g.mi.com/api/user/login";
            //创建线程请求服务器
            login(url,userAccount,userPassword);
        });
        // 点击忘记密码
        tvReset.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            //获取手机号
            String userAccount = tvUserAccount.getText() == null ? "" : tvUserAccount.getText().toString().trim();
            // 如果账号合法，数据一起传递
            if(Tools.isMobile(userAccount)){
                intent.putExtra("account",userAccount);
            }else{
                intent.putExtra("account","");
            }
            startActivity(intent);
            finish();
        });


    }

    private void login(String url, String userAccount, String userPassword) {
        new Thread(){
            @Override
            public void run() {
                MOKHttpTools tools = new MOKHttpTools();
                String result = tools.login(url,userAccount,userPassword);
                Log.d("TAG", "result"+result);;
                String flag="";
                // 判断返回结果
                if (result.equals(result)){
                    flag="success";
                }
                if (flag.equals("success")){
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }

    private void initView() {
        tvIcon = findViewById(R.id.tv_icon);
        tvUserAccount = findViewById(R.id.tv_user_account);
        tvUserPassword = findViewById(R.id.tv_user_password);
        tvLogin = findViewById(R.id.tv_login);
        tvReset = findViewById(R.id.tv_resetPassword);

    }


}