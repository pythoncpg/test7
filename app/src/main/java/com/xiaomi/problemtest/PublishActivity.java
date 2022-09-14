package com.xiaomi.problemtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.xiaomi.problemtest.util.MyToast;

public class PublishActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvPublish;
    private EditText tvContent;
    private ImageView tvAddPicture;
    private TextView tvAnimal;
    private TextView tvAnimalSpecies;
    private TextView tvWechat;
    private TextView tvAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initPermisson();

        initView();
        initViewEventListener();
    }




    private void initView() {
        tvBack = findViewById(R.id.tv_back);
        tvPublish = findViewById(R.id.tv_publish);
        tvContent = findViewById(R.id.tv_content);
        tvAddPicture = findViewById(R.id.tv_add_picture);
        tvAnimal = findViewById(R.id.tv_animal);
        tvAnimalSpecies = findViewById(R.id.tv_animal_species);
        tvWechat = findViewById(R.id.tv_weChat);
        tvAddress = findViewById(R.id.tv_address);

    }

    private void initViewEventListener() {
        // 添加图片
        tvAddPicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2);
        });

        // back
        tvBack.setOnClickListener(v -> {
            MyToast.showText(PublishActivity.this,"您取消了发帖");
            finish();
        });
        tvPublish.setOnClickListener(v -> {
            // 获取所有内容
            String animalText = tvAnimal.getText()== null ? "" : tvAnimal.getText().toString().trim();
            String animalSpeciesText = tvAnimal.getText()== null ? "" : tvAnimalSpecies.getText().toString().trim();
            String wechatText = tvAnimal.getText()== null ? "" : tvWechat.getText().toString().trim();
            String addressText = tvAnimal.getText()== null ? "" : tvAddress.getText().toString().trim();
            Bitmap imgBitmap = ((BitmapDrawable) tvAddPicture.getDrawable()).getBitmap();


        });

    }

    private void initPermisson() {
        // 申请图片权限
        if (ContextCompat.checkSelfPermission(PublishActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(PublishActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                Log.e("tag", "Result:" + data.toString());
                // 得到图片的全路径
                Uri uri=data.getData();
                String[] images={MediaStore.Images.Media.DATA};//将获取到的
                Cursor cursor=this.managedQuery(uri,images,null,null,null);
                int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String img_uri=cursor.getString(index);
                Log.d("TAG", "img_url :"+img_uri);
                tvAddPicture.setImageBitmap(BitmapFactory.decodeFile(img_uri));
            }
        }
    }

}
