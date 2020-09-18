package com.hkrt.androidaspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2020/9/17
 * @修改人和其它信息
 */
@Aspect
public class DoubleClickAspect {

    @Pointcut("execution(@com.hkrt.androidaspect.DoubleClick * *(..))")
    public void  doubleClick(){

    }

    long lastClickTime = 0;
    int timeDuration = 2000;
    @Around("doubleClick()")
    public Object checkDoubleClick(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        DoubleClick doubleClick = signature.getMethod().getAnnotation(DoubleClick.class);
        if(doubleClick != null){
            long currentTime = System.currentTimeMillis();
            if(lastClickTime == 0){
                lastClickTime = currentTime;
                return  proceedingJoinPoint.proceed();
            }
            if(currentTime - lastClickTime < timeDuration){
                Log.e("TAG","重复点击");
                return null;
            }
            lastClickTime = currentTime;
        }

        return  proceedingJoinPoint.proceed();
    }

}
