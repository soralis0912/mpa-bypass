package org.soralis_0912.mpa.bypass;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedBridge;

import android.os.Build;
import android.app.Application;

import java.io.File;


public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("jp.go.cas.mpa")) {
            return;
        }

        String className = getPackageVersion(lpparam) < 1395 ? "j.a.a.a.b.a" : "p6.a";
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "l", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }

    private int getPackageVersion(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> parserCls = XposedHelpers.findClass("android.content.pm.PackageParser", lpparam.classLoader);
        Object parser = null;
        try {
            parser = parserCls.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        File apkPath = new File(lpparam.appInfo.sourceDir);
        Object pkg = XposedHelpers.callMethod(parser, "parsePackage", apkPath, 0);

        return XposedHelpers.getIntField(pkg, "mVersionCode");
    }
}
