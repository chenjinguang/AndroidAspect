package com.hkrt.androidaspect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2020/9/16
 * @修改人和其它信息
 */
@Aspect
public class SectionAspect {

    @Pointcut("execution(@com.hkrt.androidaspect.CheckNet * *(..))")
    public void checkNetBehavior(){

    }

    @Pointcut("execution(@com.hkrt.androidaspect.CheckPermission * *(..))")
    public void checkPermissionBehavior(){

    }

    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{


        Log.e("TAG","heere");

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        CheckNet checkNet= methodSignature.getMethod().getAnnotation(CheckNet.class);
        if(checkNet != null){
            Object object = proceedingJoinPoint.getThis();
            Context context  = getContext(object);
            if(context != null){
                if(CheckNetUtil.isNetworkAvailable(context)){
                    Toast.makeText(context,"请检查你的网络",Toast.LENGTH_SHORT).show();
                     return null;
                }
            }
        }
        return proceedingJoinPoint.proceed();

    }

    private Context getContext(Object object) {
        if(object instanceof Activity){
            return (Activity)object;
        }else if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        }else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }

    @SuppressLint("CheckResult")
    @Around("checkPermissionBehavior()")
    public void checkPermission(final ProceedingJoinPoint joinPoint) throws  Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckPermission checkPermission  = signature.getMethod().getAnnotation(CheckPermission.class);
        String[] permissions = checkPermission.value();
        RxPermissions rxPermissions = new RxPermissions((Activity) getContext(joinPoint.getThis()));
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean){
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });

    }

    public static boolean hasPermission(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            return hasPermission(context, permission);
        }
        return true;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static void requestPermission(Activity activity, String[] permissions, int request) {
        ActivityCompat.requestPermissions(activity, permissions, request);
    }

}
