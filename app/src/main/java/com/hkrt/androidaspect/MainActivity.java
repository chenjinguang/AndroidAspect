package com.hkrt.androidaspect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hkrt.aopermission.permissionlib.annotation.NeedPermission;
import com.hkrt.aopermission.permissionlib.annotation.PermissionCanceled;
import com.hkrt.aopermission.permissionlib.annotation.PermissionDenied;
import com.hkrt.aopermission.permissionlib.bean.CancelBean;
import com.hkrt.aopermission.permissionlib.bean.DenyBean;
import com.hkrt.aopermission.permissionlib.util.SettingUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkNet();
                callPhone();
            }
        });

    }

    @CheckNet
    private void checkNet() {
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
    }


    @CheckPermission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    private void doSomething() {

    }


    /**
     * 申请多个权限
     */
    @NeedPermission(value = {Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, requestCode = 10)
    public void callPhone() {
        Toast.makeText(this, "电话、相机权限申请通过", Toast.LENGTH_SHORT).show();
    }

    /**
     * 申请单个权限
     */
    @NeedPermission(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 0)
    private void callMap() {
        Toast.makeText(this, "定位权限申请通过", Toast.LENGTH_SHORT).show();
    }

    /**
     * 权限被拒绝
     *
     * @param bean DenyBean
     */
    @PermissionDenied
    public void dealPermission(DenyBean bean) {
        if (bean == null) {
            return;
        }
        List<String> denyList = bean.getDenyList();
        switch (bean.getRequestCode()) {
            case 10:
                //多个权限申请返回结果
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < denyList.size(); i++) {
                    if (Manifest.permission.CALL_PHONE.equals(denyList.get(i))) {
                        builder.append("电话");
                    } else if (Manifest.permission.CAMERA.equals(denyList.get(i))) {
                        builder.append("相机");
                    }
                }
                builder.append("权限被禁止，需要手动打开");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage(builder)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(MainActivity.this);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                break;
            case 0:
                //单个权限申请返回结果
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("定位权限被禁止，需要手动打开")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(MainActivity.this);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 权限被取消
     *
     * @param bean CancelBean
     */
    @PermissionCanceled
    public void dealCancelPermission(CancelBean bean) {
        Toast.makeText(this, "权限申请被取消，请求码 :" + bean.getRequestCode(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 在Service中申请权限
     *
     * @param view
     */
    public void startPermissionService(View view) {
        Intent intent = new Intent(this, PermissionService.class);
        startService(intent);
    }

    /**
     * 在util类中的非静态方法中申请
     *
     * @param view
     */
    public void requestByUtil(View view) {
        RequestPermissionByUtil util = new RequestPermissionByUtil();
        util.requestPermission(this);
    }


}