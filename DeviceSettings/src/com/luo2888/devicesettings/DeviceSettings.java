package com.luo2888.devicesettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.*;
import android.provider.Settings;
import android.widget.Toast;
import com.luo2888.devicesettings.utils.*;

import android.content.*;
import java.net.*;

import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import android.content.pm.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xs on 15-7-25.
 */
/**
 * Edit by GaHoKwan on 16-1-30.
 */

public class DeviceSettings extends android.preference.PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String BFSettings = "boeffla_settings_key";
    private static final String CameraSwitch = "camera_switch_key";
	
	/* GaHoKwan ADD */
    public static final String KEY_RUNNING_MODE = "running_mode";
    public static final String KEY_CABC = "cabc";
    public static final String KEY_CHARGE = "charge";
    public static final String KEY_KCAL = "kcal";
    public static final String KEY_KCAL_SAT = "kcal_sat";
    public static final String KEY_KCAL_INVERT = "kcal_invert";
    public static final String KEY_KEYLIGHT = "keylight";
    public static final String KEY_KEYLIGHT_TIMEOUT = "keylight_timeout";
    public static final String KEY_LED_FADE = "led_fade";
    public static final String KEY_LED_Intensity = "led_intensity";
    public static final String KEY_WAKEUP_MODE = "wakeup_mode";
    public static final String KEY_GLOVE_MODE = "glove_mode";
    public static final String KEY_MDNIE_MODE = "mdnie_mode";
    public static final String KEY_MDNIE_SCENARIO = "mdnie_scenario";
    public static final String KEY_MDNIE_OUTDOOR = "mdnie_outdoor";
    public static final String VIBRATOR = "vibrator";
    private static final int FLASH_OTA = 1;
    public static final String KEY_FLASHOTA = "flashota";
	/* GaHoKwan END */

    private PreferenceScreen mBFSettings;
    private ListPreference mCameraSwitch;
	
	/* GaHoKwan ADD */
    private ListPreference mRunningMode;
    private ListPreference mKcal;
    private ListPreference mKcalSat;
    private ListPreference mKeylight;
    private ListPreference mKeylightTimeout;
    private ListPreference mWakeUp;
    private ListPreference mLedFade;
    private ListPreference mLedIntensity;
    private CheckBoxPreference mCharge;
    private CheckBoxPreference mKcalInvert;
    private CheckBoxPreference mGLoveMode;
    private CheckBoxPreference mmDNIeOutdoor;
    private CABC mCABC;
    private mDNIeMode mmDNIeMode;
    private mDNIeScenario mmDNIeScenario;
    private File file;
	private PushManager pm;
	private PreferenceScreen flashota;
	
	/* GaHoKwan END */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		initGeTui();
        addPreferencesFromResource(R.xml.stocksettings);
        mBFSettings = (PreferenceScreen) findPreference(BFSettings);
        mCameraSwitch = (ListPreference) findPreference(CameraSwitch);
        mCameraSwitch.setOnPreferenceChangeListener(this);

		/* GaHoKwan ADD */
        mRunningMode = (ListPreference)findPreference(KEY_RUNNING_MODE);
        mRunningMode.setOnPreferenceChangeListener(this);
        mLedIntensity = (ListPreference)findPreference(KEY_LED_Intensity);
        mLedIntensity.setOnPreferenceChangeListener(this);
        mLedFade = (ListPreference)findPreference(KEY_LED_FADE);
        mLedFade.setOnPreferenceChangeListener(this);
        mWakeUp = (ListPreference)findPreference(KEY_WAKEUP_MODE);
        mWakeUp.setOnPreferenceChangeListener(this);
        mKcal = (ListPreference)findPreference(KEY_KCAL);
        mKcal.setOnPreferenceChangeListener(this);
        mKcalSat = (ListPreference)findPreference(KEY_KCAL_SAT);
        mKcalSat.setOnPreferenceChangeListener(this);
        mKeylight = (ListPreference)findPreference(KEY_KEYLIGHT);
        mKeylight.setOnPreferenceChangeListener(this);
        mKeylightTimeout = (ListPreference)findPreference(KEY_KEYLIGHT_TIMEOUT);
        mKeylightTimeout.setOnPreferenceChangeListener(this);
        mKcalInvert = (CheckBoxPreference)findPreference(KEY_KCAL_INVERT);
        mCharge = (CheckBoxPreference)findPreference(KEY_CHARGE);
        mGLoveMode = (CheckBoxPreference)findPreference(KEY_GLOVE_MODE);
        mmDNIeOutdoor = (CheckBoxPreference)findPreference(KEY_MDNIE_OUTDOOR);
        mCABC = (CABC)findPreference(KEY_CABC);
        mmDNIeScenario = (mDNIeScenario)findPreference(KEY_MDNIE_SCENARIO);
        mmDNIeScenario.setEnabled(mDNIeScenario.isSupported());
        mmDNIeMode = (mDNIeMode)findPreference(KEY_MDNIE_MODE);
        mmDNIeMode.setEnabled(mDNIeMode.isSupported());
		flashota = (PreferenceScreen) findPreference(KEY_FLASHOTA);
		/* GaHoKwan END */
        
        //Device
            mCameraSwitch.setEntries(R.array.camera_switch_entries_klte);
            mCameraSwitch.setEntryValues(R.array.camera_switch_values_klte);
			/* GaHoKwan ADD */
            mRunningMode.setEntries(R.array.running_mode_entries);
            mRunningMode.setEntryValues(R.array.running_mode_entries_values);
            mLedIntensity.setEntries(R.array.led_intensity_entries);
            mLedIntensity.setEntryValues(R.array.led_intensity_entries_values);
			mLedFade.setEntries(R.array.led_fade_entries);
			mLedFade.setEntryValues(R.array.led_fade_entries_values);
			mWakeUp.setEntries(R.array.wakeup_mode_entries);
			mWakeUp.setEntryValues(R.array.wakeup_mode_entries_values);
			mKcal.setEntries(R.array.kcal_entries);
			mKcal.setEntryValues(R.array.kcal_entries_values);
			mKcalSat.setEntries(R.array.kcal_sat_entries);
			mKcalSat.setEntryValues(R.array.kcal_sat_entries_values);
			mKeylight.setEntries(R.array.keylight_entries);
			mKeylight.setEntryValues(R.array.keylight_entries_values);
			mKeylightTimeout.setEntries(R.array.keylight_timeout_entries);
			mKeylightTimeout.setEntryValues(R.array.keylight_timeout_entries_values);
			/* GaHoKwan END */
    }

    private void initGeTui() {
        pm = PushManager.getInstance();
        pm.initialize(this.getApplicationContext());
        Tag tag = new Tag();
        tag.setName("others");
        Tag[] tags = new Tag[1];
        tags[0] = tag;
        pm.setTag(this, tags);
    }
	
    public void onStart() {
        super.onStart();
        setListPreferenceSummary(mCameraSwitch);
		/* GaHoKwan ADD */
        setListPreferenceSummary(mRunningMode);
        setListPreferenceSummary(mLedIntensity);
        setListPreferenceSummary(mLedFade);
        setListPreferenceSummary(mWakeUp);
        setListPreferenceSummary(mKcal);
        setListPreferenceSummary(mKcalSat);
        setListPreferenceSummary(mKeylight);
        setListPreferenceSummary(mKeylightTimeout);
		/* GaHoKwan END */
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen , Preference preference) {
		/* GaHoKwan ADD */
		if (preference == mCharge) {
			if (mCharge.isChecked()) {
				DialogAttention();
				Tools.Shell("echo '2200 mA' > /sys/kernel/charge_levels/charge_level_ac");
				Tools.Shell("echo '950 mA' > /sys/kernel/charge_levels/charge_level_usb");
			}
			else {
				Tools.Shell("echo '0 mA' > /sys/kernel/charge_levels/charge_level_ac");
				Tools.Shell("echo '0 mA' > /sys/kernel/charge_levels/charge_level_usb");
			}
		}
		if (preference == mGLoveMode) {
			if (mGLoveMode.isChecked()) {
				Tools.Shell("echo '1' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/glove_mode_enable");
			}
			else {
				Tools.Shell("echo '0' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/glove_mode_enable");
			}
		}
		if (preference == mKcalInvert) {
			if (mKcalInvert.isChecked()) {
				Tools.Shell("echo '1' >  /sys/devices/platform/kcal_ctrl.0/kcal_invert");
			}
			else {
				Tools.Shell("echo '0' > /sys/devices/platform/kcal_ctrl.0/kcal_invert");
			}
		}
		if (preference == mmDNIeOutdoor) {
			if (mmDNIeOutdoor.isChecked()) {
				Tools.Shell("echo '1' > /sys/class/mdnie/mdnie/outdoor");
			}
			else {
				Tools.Shell("echo '0' >/sys/class/mdnie/mdnie/outdoor");
			}
		}
		if (preference == flashota){
			new AlertDialog.Builder(this)
				.setTitle(R.string.updater_notice_title)
				.setMessage(R.string.updater_notice_message)
				.setPositiveButton(R.string.updater_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						ComponentName filemanager = new ComponentName("com.android.fileexplorer", "com.android.fileexplorer.FileExplorerTabActivity");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setComponent(filemanager);
						try {
							startActivityForResult(
								Intent.createChooser(intent, "ZIP"),
								FLASH_OTA);
						} catch (android.content.ActivityNotFoundException ex) {
						}
					}
				})
				.show();
		}
		/* GaHoKwan END */
        return false;
    }


    private void setListPreferenceSummary(ListPreference mListPreference) {
            if (mListPreference == mCameraSwitch) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.camera_switch_nubia_summary);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.camera_switch_miui_summary);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.camera_switch_google_summary);
                    } else if (3 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.camera_switch_cm_summary);
                    }
             
	/* GaHoKwan ADD */
            if (mListPreference == mRunningMode ) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.running_mode_summary_battery);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.running_mode_summary_standard);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.running_mode_summary_performance);
                    }
                }

            if (mListPreference == mLedIntensity ) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_0);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_1);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_2);
                    } else if (3 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_3);
                    } else if (4 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_4);
                    } else if (5 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_intensity_summary_5);
                    }
                }
            }

            if (mListPreference == mLedFade ) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_fade_off_summary_head);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.led_fade_on_summary_head);
                    }
                }

            if (mListPreference == mWakeUp ) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_0);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_1);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_2);
                    } else if (3 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_3);
                    } else if (4 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_4);
                    } else if (5 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_5);
                    } else if (6 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.wakeup_mode_summary_6);
                    }
                }

            
            if (mListPreference == mKcal ) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.kcal_summary_0);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.kcal_summary_1);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.kcal_summary_2);
                    } else if (3 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.kcal_summary_3);
                    } else if (4 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.kcal_summary_4);
                    }
                }
            
            if (mListPreference == mKeylight) {
                    if (0 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.keylight_summary_0);
                    } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.keylight_summary_1);
                    } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                        mListPreference.setSummary(R.string.keylight_summary_2);
                    }
                }
    
    if (mListPreference == mKeylightTimeout ) {
            if (0 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.keylight_timeout_summary_0);
            } else if (1 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.keylight_timeout_summary_1);
            } else if (2 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.keylight_timeout_summary_2);
            } else if (3 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.keylight_timeout_summary_3);
            } else if (4 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.keylight_timeout_summary_4);
            }
        }

if (mListPreference == mKcalSat ) {
        if (0 == Integer.parseInt(mListPreference.getValue())) {
            mListPreference.setSummary(R.string.kcal_sat_summary_0);
        } else if (1 == Integer.parseInt(mListPreference.getValue())) {
            mListPreference.setSummary(R.string.kcal_sat_summary_1);
        } else if (2 == Integer.parseInt(mListPreference.getValue())) {
            mListPreference.setSummary(R.string.kcal_sat_summary_2);
        }
    }
}
    /* GaHoKwan END */

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mCameraSwitch == preference) {
                String ValueCameraSwitch = (String) newValue;
                mCameraSwitch.setValue(ValueCameraSwitch);
                int mode = Integer.parseInt(ValueCameraSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.camera_switch_nubia_summary);
                        Tools.Shell("mount -o remount,rw /system");
                        Tools.Shell("rm -rf /system/app/Camera.apk");
                        Tools.Shell("cp /system/app/CameraDir/nubia_camera.apk /system/app/Camera.apk");
                        Tools.Shell("chmod 0644 /system/app/Camera.apk");
                        break;
                    case 1:
                        preference.setSummary(R.string.camera_switch_miui_summary);
                        Tools.Shell("mount -o remount,rw /system");
                        Tools.Shell("rm -rf /system/app/Camera.apk");
                        Tools.Shell("cp /system/app/CameraDir/miui_camera.apk /system/app/Camera.apk");
                        Tools.Shell("chmod 0644 /system/app/Camera.apk");
                        break;
                    case 2:
                        preference.setSummary(R.string.camera_switch_google_summary);
                        Tools.Shell("mount -o remount,rw /system");
                        Tools.Shell("rm -rf /system/app/Camera.apk");
                        Tools.Shell("cp /system/app/CameraDir/google_camera.apk /system/app/Camera.apk");
                        Tools.Shell("chmod 0644 /system/app/Camera.apk");
                        break;
                    case 3:
                        preference.setSummary(R.string.camera_switch_cm_summary);
                        Tools.Shell("mount -o remount,rw /system");
                        Tools.Shell("rm -rf /system/app/Camera.apk");
                        Tools.Shell("cp /system/app/CameraDir/cm_camera.apk /system/app/Camera.apk");
                        Tools.Shell("chmod 0644 /system/app/Camera.apk");
                        break;
                    default:
                        break;
                }
            }

    /* GaHoKwan ADD */

        if (mRunningMode == preference) {
                String ValueRunningModeSwitch = (String) newValue;
                mRunningMode.setValue(ValueRunningModeSwitch);
                int mode = Integer.parseInt(ValueRunningModeSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.running_mode_summary_battery);
                        Tools.Shell("/system/bin/running_mode_battery");
                        break;
                    case 1:
                        preference.setSummary(R.string.running_mode_summary_standard);
                        Tools.Shell("/system/bin/running_mode_standard");
                        break;
                    case 2:
                        preference.setSummary(R.string.running_mode_summary_performance);
                        Tools.Shell("/system/bin/running_mode_performance");
                    	DialogAttentionPerformance();
                        break;
                    default:
                        break;
                }
            }

        if (mLedIntensity == preference) {
                String ValueLedIntensitySwitch = (String) newValue;
                mLedIntensity.setValue(ValueLedIntensitySwitch);
                int mode = Integer.parseInt(ValueLedIntensitySwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.led_intensity_summary_0);
                        Tools.Shell("echo '40' > /sys/class/sec/led/led_intensity");
                        break;
                    case 1:
                        preference.setSummary(R.string.led_intensity_summary_1);
                        Tools.Shell("echo '30' > /sys/class/sec/led/led_intensity");
                        break;
                    case 2:
                        preference.setSummary(R.string.led_intensity_summary_2);
                        Tools.Shell("echo '20' > /sys/class/sec/led/led_intensity");
                        break;
                    case 3:
                        preference.setSummary(R.string.led_intensity_summary_3);
                        Tools.Shell("echo '10' > /sys/class/sec/led/led_intensity");
                        break;
                    case 4:
                        preference.setSummary(R.string.led_intensity_summary_4);
                        Tools.Shell("echo '5' > /sys/class/sec/led/led_intensity");
                        break;
                    case 5:
                        preference.setSummary(R.string.led_intensity_summary_5);
                        Tools.Shell("echo '1' > /sys/class/sec/led/led_intensity");
                        break;
                    default:
                        break;
                }
            }
        

        if (mLedFade == preference) {
                String ValueLedFadeSwitch = (String) newValue;
                mLedFade.setValue(ValueLedFadeSwitch);
                int mode = Integer.parseInt(ValueLedFadeSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.led_fade_off_summary_head);
                        Tools.Shell("echo '0' > /sys/class/sec/led/led_fade");
                        break;
                    case 1:
                        preference.setSummary(R.string.led_fade_on_summary_head);
                        Tools.Shell("echo '1' > /sys/class/sec/led/led_fade");
                        break;
                    default:
                        break;
                }
            }
        

        if (mWakeUp == preference) {
                String ValueWakeUpSwitch = (String) newValue;
                mWakeUp.setValue(ValueWakeUpSwitch);
                int mode = Integer.parseInt(ValueWakeUpSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.wakeup_mode_summary_0);
                        Tools.Shell("echo '0' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 1:
                        preference.setSummary(R.string.wakeup_mode_summary_1);
                        Tools.Shell("echo '1' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 2:
                        preference.setSummary(R.string.wakeup_mode_summary_2);
                        Tools.Shell("echo '2' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 3:
                        preference.setSummary(R.string.wakeup_mode_summary_3);
                        Tools.Shell("echo '3' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 4:
                        preference.setSummary(R.string.wakeup_mode_summary_4);
                        Tools.Shell("echo '4' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 5:
                        preference.setSummary(R.string.wakeup_mode_summary_5);
                        Tools.Shell("echo '5' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    case 6:
                        preference.setSummary(R.string.wakeup_mode_summary_6);
                        Tools.Shell("echo '6' > /sys/class/i2c-adapter/i2c-2/2-0020/input/input2/screen_wake_options");
                        break;
                    default:
                        break;
                }
            }
        

        if (mKcal == preference) {
                String ValueKcalSwitch = (String) newValue;
                mKcal.setValue(ValueKcalSwitch);
                int mode = Integer.parseInt(ValueKcalSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.kcal_summary_0);
                        Tools.Shell("echo '255 255 255' >/sys/devices/platform/kcal_ctrl.0/kcal");
                        break;
                    case 1:
                        preference.setSummary(R.string.kcal_summary_1);
                        Tools.Shell("echo '200 200 205' > /sys/devices/platform/kcal_ctrl.0/kcal");
                        break;
                    case 2:
                        preference.setSummary(R.string.kcal_summary_2);
                        Tools.Shell("echo '150 150 155' > /sys/devices/platform/kcal_ctrl.0/kcal");
                        break;
                    case 3:
                        preference.setSummary(R.string.kcal_summary_3);
                        Tools.Shell("echo '125 125 130' > /sys/devices/platform/kcal_ctrl.0/kcal");
                        break;
                    case 4:
                        preference.setSummary(R.string.kcal_summary_4);
                        Tools.Shell("echo '75 75 80' > /sys/devices/platform/kcal_ctrl.0/kcal");
                        break;
                    default:
                        break;
                }
            }
        


        if (mKcalSat == preference) {
                String ValueKcalSatSwitch = (String) newValue;
                mKcalSat.setValue(ValueKcalSatSwitch);
                int mode = Integer.parseInt(ValueKcalSatSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.kcal_sat_summary_0);
                        Tools.Shell("echo '255' >/sys/devices/platform/kcal_ctrl.0/kcal_sat");
                        break;
                    case 1:
                        preference.setSummary(R.string.kcal_sat_summary_1);
                        Tools.Shell("echo '128' > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                        break;
                    case 2:
                        preference.setSummary(R.string.kcal_sat_summary_2);
                        Tools.Shell("echo '285' > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                        break;
                    default:
                        break;
                }
            }
        

        if (mKeylight == preference) {
                String ValueKeylightSwitch = (String) newValue;
                mKeylight.setValue(ValueKeylightSwitch);
                int mode = Integer.parseInt(ValueKeylightSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.keylight_summary_0);
                        Tools.Shell("echo '2' >sys/class/misc/btk_control/btkc_mode");
                        break;
                    case 1:
                        preference.setSummary(R.string.keylight_summary_1);
                        Tools.Shell("echo '0' >sys/class/misc/btk_control/btkc_mode");
                        break;
                    case 2:
                        preference.setSummary(R.string.keylight_summary_2);
                        Tools.Shell("echo '1' > sys/class/misc/btk_control/btkc_mode");
                        break;
                    default:
                        break;
                }
            }
        

        if (mKeylightTimeout == preference) {
                String ValueKeylightTimeoutSwitch = (String) newValue;
                mKeylightTimeout.setValue(ValueKeylightTimeoutSwitch);
                int mode = Integer.parseInt(ValueKeylightTimeoutSwitch);
                switch (mode) {
                    case 0:
                        preference.setSummary(R.string.keylight_timeout_summary_0);
                        Tools.Shell("echo '1' >sys/class/misc/btk_control/btkc_timeout");
                        break;
                    case 1:
                        preference.setSummary(R.string.keylight_timeout_summary_1);
                        Tools.Shell("echo '5' >sys/class/misc/btk_control/btkc_timeout");
                        break;
                    case 2:
                        preference.setSummary(R.string.keylight_timeout_summary_2);
                        Tools.Shell("echo '10' > sys/class/misc/btk_control/btkc_timeout");
                        break;
                    case 3:
                        preference.setSummary(R.string.keylight_timeout_summary_3);
                        Tools.Shell("echo '20' > sys/class/misc/btk_control/btkc_timeout");
                        break;
                    case 4:
                        preference.setSummary(R.string.keylight_timeout_summary_4);
                        Tools.Shell("echo '7200' > sys/class/misc/btk_control/btkc_timeout");
                        break;
                    default:
                        break;
                }
            }
        

    /* GaHoKwan END */
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FLASH_OTA:
                if (resultCode == RESULT_OK) {
                    OutputStream localOutputStream = null;

                    if (data.getData() != null) {
                        file = new File(data.getData().getPath());
                        if (file.getName().endsWith(".zip")) {

                            try {
                                localOutputStream = Runtime.getRuntime().exec("su")
									.getOutputStream();

                                localOutputStream.write("mkdir -p /cache/recovery/\n".getBytes());
                                localOutputStream
									.write("echo 'boot-recovery' >/cache/recovery/command\n"
										   .getBytes());
                                localOutputStream
									.write(("echo '--update_package=" + file.getAbsolutePath() + "' >> /cache/recovery/command\n")
										   .getBytes());
                                localOutputStream
									.write(("reboot recovery\n")
										   .getBytes());
                                localOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally
                            {
                                if (localOutputStream != null)
                                    try {
                                        localOutputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                ((PowerManager) getSystemService(Context.POWER_SERVICE))
									.reboot("recovery");

                            }

                        }
                        else
                        {
                            Toast.makeText(this, R.string.updater_choose_abort,
										   Toast.LENGTH_LONG).show();
                        }
                    }
                }

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void DialogAttentionPerformance() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.warning_title)
                .setMessage(R.string.performance_warning_dialog_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void DialogAttention() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.warning_title)
                .setMessage(R.string.charge_warning_dialog_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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
