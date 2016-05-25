package com.luo2888.devicesettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.luo2888.devicesettings.utils.Tools;

/**
 * Created by xs on 15-8-25.
 */
/**
 * Edit by GaHoKwan on 16-1-30.
 */
public class Sounds extends android.preference.PreferenceActivity {

    private static final String CameraSound = "camera_sound_key";
    private static final String PowerSound = "power_sound_key";
    private static final String ScreenshotSound = "screenshot_sound_key";


    private CheckBoxPreference mCameraSound;
    private CheckBoxPreference mPowerSound;
    private CheckBoxPreference mScreenshotSound;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sounds);

        mCameraSound = (CheckBoxPreference) findPreference(CameraSound);
        mPowerSound = (CheckBoxPreference) findPreference(PowerSound);
        mScreenshotSound = (CheckBoxPreference) findPreference(ScreenshotSound);

    }

    public void onStart() {
        super.onStart();
        setCheckBoxPreferenceSummary(mCameraSound);
        setCheckBoxPreferenceSummary(mPowerSound);
        setCheckBoxPreferenceSummary(mScreenshotSound);
    }

    private void setCheckBoxPreferenceSummary(CheckBoxPreference mCheckBoxPreference) {
        if (mCheckBoxPreference == mCameraSound) {
            if (mCameraSound.isChecked()) {
                mCameraSound.setSummary(getResources().getString(R.string.camera_sound_summary_on));
            } else {
                mCameraSound.setSummary(getResources().getString(R.string.camera_sound_summary_off));
            }
        }
        if (mCheckBoxPreference == mPowerSound) {
            if (mPowerSound.isChecked()) {
                mPowerSound.setSummary(getResources().getString(R.string.power_sound_summary_on));
            } else {
                mPowerSound.setSummary(getResources().getString(R.string.power_sound_summary_off));
            }
        }
        if (mCheckBoxPreference == mScreenshotSound) {
            if (mScreenshotSound.isChecked()) {
                mScreenshotSound.setSummary(getResources().getString(R.string.screenshot_sound_summary_on));
            } else {
                mScreenshotSound.setSummary(getResources().getString(R.string.screenshot_sound_summary_off));
            }
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen , Preference preference) {
        if (preference == mScreenshotSound) {
            if (mScreenshotSound.isChecked()) {
                Tools.Shell("setprop persist.screenshot.sound 1");
                mScreenshotSound.setSummary(getResources().getString(R.string.screenshot_sound_summary_on));
            } else {
                Tools.Shell("setprop persist.screenshot.sound 0");
                mScreenshotSound.setSummary(getResources().getString(R.string.screenshot_sound_summary_off));
            }
        }
        if (preference == mPowerSound) {
            if (mPowerSound.isChecked()) {
                Tools.Shell("setprop persist.power.sound 1");
                mPowerSound.setSummary(getResources().getString(R.string.power_sound_summary_on));
            } else {
                Tools.Shell("setprop persist.power.sound 0");
                mPowerSound.setSummary(getResources().getString(R.string.power_sound_summary_off));
            }
        }
        if (preference == mCameraSound) {
            Tools.Shell("mount -o remount,rw /system");
            if  (mCameraSound.isChecked()) {
                new Thread() {
                    public void run() {
                        Tools.Shell("sed -i 's/bak/ogg/g' /system/lib/libcameraservice.so");
                    }
                }.start();
                mCameraSound.setSummary(getResources().getString(R.string.camera_sound_summary_on));
                DialogReboot();
            } else {
                new Thread() {
                    public void run() {
                        Tools.Shell("sed -i 's/ogg/bak/g' /system/lib/libcameraservice.so");
                    }
                }.start();
                mCameraSound.setSummary(getResources().getString(R.string.camera_sound_summary_off));
                if (Tools.IsInstall(this, "cn.nubia.camera")) {
                    Toast.makeText(this, getResources().getString(R.string.find_nubia_camera), Toast.LENGTH_LONG).show();
                }
                DialogReboot();
            }
        }
        return false;
    }

    public void DialogReboot() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.dialog_ok)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tools.Shell("reboot");
                    }
                })
                .setNeutralButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),R.string.dialog_reboot,Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
