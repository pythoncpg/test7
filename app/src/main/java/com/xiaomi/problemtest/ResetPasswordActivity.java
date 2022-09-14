package com.xiaomi.problemtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaomi.problemtest.util.MOKHttpTools;
import com.xiaomi.problemtest.util.MyToast;
import com.xiaomi.problemtest.util.Tools;


public class ResetPasswordActivity extends AppCompatActivity {

    private EditText tvUserAccount;
    private EditText tvUserPassword;
    private EditText tvUserPasswordAgain;
    private TextView tvReset;
    private TextView tvSubmit;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 密码重置成功 携带账号信息跳转登录页面
            if (msg.what == 1) {
                MyToast.showText(ResetPasswordActivity.this,"密码重置成功，请登录");
            }else{
                MyToast.showText(ResetPasswordActivity.this,"用户名不存在，请先注册账户");
            }
            Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
            intent.putExtra("userCount",tvUserAccount.getText().toString().trim());
            startActivity(intent);
        }
    };
    private String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Bundle bundleExtra = getIntent().getExtras();
        userAccount = bundleExtra.getString("account");
        initView();
        initViewEventListener();
    }

    private void initViewEventListener() {
        // 提交按钮
        tvSubmit.setOnClickListener(v -> {
            //获取手机号
            String userAccount = tvUserAccount.getText() == null ? "" : tvUserAccount.getText().toString().trim();
            String userPassword = tvUserPassword.getText() == null ? "" : tvUserPassword.getText().toString().trim();
            String userPasswordAgain = tvUserPasswordAgain.getText() == null ? "" : tvUserPassword.getText().toString().trim();
            //验证数
            if (!Tools.isMobile(userAccount)) {
                tvUserAccount.setError("请输入正确的手机号");
                return;
            }
            if (userPassword.length() < 6) {
                tvUserPassword.setError("密码长度应该大于等于6");
                return;
            }
            if (!userPassword.equals(userPasswordAgain)){
                tvUserPasswordAgain.setError("密码和确认密码不一致");
                return;
            }
            String url = "";
            //创建线程请求服务器
            resetPassword(url,userAccount,userPassword);
        });

        // 重置按钮
        tvReset.setOnClickListener(v -> {
            tvUserAccount.setText("");
            tvUserPassword.setText("");
            tvUserPasswordAgain.setText("");
        });
    }

    private void resetPassword(String url, String userAccount, String userPassword) {
        new Thread(){
            @Override
            public void run() {
                MOKHttpTools tools = new MOKHttpTools();
                String result = tools.resetPassword(url,userAccount,userPassword);
                String flag="";
                // 判断返回结果
                if (result.equals(result)){
                    flag="true";
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
        tvUserAccount = findViewById(R.id.tv_user_account);
        if (userAccount.length()>0){
            tvUserAccount.setText(userAccount);
        }
        tvUserPassword = findViewById(R.id.tv_user_password);
        tvUserPasswordAgain = findViewById(R.id.tv_user_password_again);
        tvReset = findViewById(R.id.tv_reset);
        tvSubmit = findViewById(R.id.tv_submit);
    }
}