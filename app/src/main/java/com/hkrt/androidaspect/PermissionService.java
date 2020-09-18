package com.hkrt.androidaspect;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.hkrt.aopermission.permissionlib.annotation.NeedPermission;
import com.hkrt.aopermission.permissionlib.annotation.PermissionCanceled;
import com.hkrt.aopermission.permissionlib.annotation.PermissionDenied;
import com.hkrt.aopermission.permissionlib.bean.CancelBean;
import com.hkrt.aopermission.permissionlib.bean.DenyBean;
import com.hkrt.aopermission.permissionlib.util.SettingUtil;


public class PermissionService extends Service {

    public PermissionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCamera();
            }
        }, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    @NeedPermission(value = {Manifest.permission.CAMERA})
    private void requestCamera() {
        Toast.makeText(PermissionService.this, "相机权限已经被同意", Toast.LENGTH_SHORT).show();
    }


    @PermissionDenied
    public void deniedCallBack(DenyBean bean) {
        Toast.makeText(PermissionService.this, "相机权限已经被禁止", Toast.LENGTH_SHORT).show();
        SettingUtil.go2Setting(PermissionService.this);
    }

    @PermissionCanceled
    public void canceledCallBack(CancelBean bean) {
        Toast.makeText(PermissionService.this, "相机权限已经被取消", Toast.LENGTH_SHORT).show();
    }
}
