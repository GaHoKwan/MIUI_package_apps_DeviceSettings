package com.luo2888.providers.devicesettings;

import android.util.Log;
import java.io.DataOutputStream;

public class Tools {

    private static final String LOG_TAG = "DeviceSettingsProvider";

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
}
