package com.luo2888.devicesettings.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Created by xs on 15-8-19.
 */
public class Tools {

    private static final String LOG_TAG = "StockSettings";

    public static int Shell(String paramString) {
        try {
            Log.i(LOG_TAG, paramString);
            Process localProcess = Runtime.getRuntime().exec("su");
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    localProcess.getOutputStream());
            String str = paramString + "\n";
            localDataOutputStream.writeBytes(str);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            return localProcess.exitValue();

        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return 1;
    }

    public static boolean IsInstall( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
