/*
 * Copyright (C) 2012 The CyanogenMod project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.ViewConfiguration;

<<<<<<< HEAD
// **** BEEGEE_PATCH_START ****
import android.app.Activity;
import android.view.View;
import android.util.Slog;
import com.android.settings.Utils;
// **** BEEGEE_PATCH_END ****

import com.android.internal.util.slim.DeviceUtils;
=======
>>>>>>> upstream/android-4.4
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class SystemUiSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "SystemSettings";

    private static final String KEY_EXPANDED_DESKTOP = "expanded_desktop";
    private static final String KEY_EXPANDED_DESKTOP_NO_NAVBAR = "expanded_desktop_no_navbar";

    private ListPreference mExpandedDesktopPref;
    private CheckBoxPreference mExpandedDesktopNoNavbarPref;

<<<<<<< HEAD
    private ListPreference mNavigationBarHeight;
    private ListPreference mNavigationBarWidth;

    // **** BEEGEE_PATCH_START ****
    private static final String KEY_NAV_BAR_POS = "nav_position";
    private static final String KEY_NAV_BAR_LEFT = "navigation_bar_left";
    private ListPreference mNavPos;
    // **** BEEGEE_PATCH_END ****

=======
>>>>>>> upstream/android-4.4
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system_ui_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        // Expanded desktop
        mExpandedDesktopPref = (ListPreference) findPreference(KEY_EXPANDED_DESKTOP);
        mExpandedDesktopNoNavbarPref =
                (CheckBoxPreference) findPreference(KEY_EXPANDED_DESKTOP_NO_NAVBAR);

        int expandedDesktopValue = Settings.System.getInt(resolver,
                Settings.System.EXPANDED_DESKTOP_STYLE, 0);

<<<<<<< HEAD
        final boolean hasRealNavigationBar = getResources()
                .getBoolean(com.android.internal.R.bool.config_showNavigationBar);
        if (hasRealNavigationBar) { // only disable on devices with REAL navigation bars
            Preference pref = findPreference("hardware_keys_disable");
            if (pref != null) {
                prefScreen.removePreference(pref);
            }
            pref = findPreference("navbar_force_enable");
            if (pref != null) {
                prefScreen.removePreference(pref);
            }
        }
        
        /**** BEEGEE_PATCH_START ****/
        mNavPos = (ListPreference) prefScreen.findPreference(KEY_NAV_BAR_POS);
        CheckBoxPreference mNavbarleft = (CheckBoxPreference) prefScreen.findPreference(KEY_NAV_BAR_LEFT);
        PreferenceCategory notificationsCategory = (PreferenceCategory) findPreference("navigation_bar");
//Slog.d("NavBarPos","device_type = "+device_type);
        if (Utils.isTablet(getActivity())) {
            notificationsCategory.removePreference(mNavbarleft);
            Settings.System.putInt(getContentResolver(), Settings.System.NAV_BAR_POS, 4);
            mNavPos.setOnPreferenceChangeListener(this);
        } else {
            notificationsCategory.removePreference(mNavPos);
        }
        /**** BEEGEE_PATCH_END ****/

=======
>>>>>>> upstream/android-4.4
        // Allows us to support devices, which have the navigation bar force enabled.
        final boolean hasNavBar = !ViewConfiguration.get(getActivity()).hasPermanentMenuKey();

        if (hasNavBar) {
            mExpandedDesktopPref.setOnPreferenceChangeListener(this);
            mExpandedDesktopPref.setValue(String.valueOf(expandedDesktopValue));
            updateExpandedDesktop(expandedDesktopValue);
            prefScreen.removePreference(mExpandedDesktopNoNavbarPref);
        } else {
            // Hide no-op "Status bar visible" expanded desktop mode
            mExpandedDesktopNoNavbarPref.setOnPreferenceChangeListener(this);
            mExpandedDesktopNoNavbarPref.setChecked(expandedDesktopValue > 0);
            prefScreen.removePreference(mExpandedDesktopPref);
        }

    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mExpandedDesktopPref) {
            int expandedDesktopValue = Integer.valueOf((String) objValue);
            updateExpandedDesktop(expandedDesktopValue);
            return true;
        } else if (preference == mExpandedDesktopNoNavbarPref) {
            updateExpandedDesktop((Boolean) objValue ? 2 : 0);
            return true;
        }

        // **** BEEGEE_PATCH_START ****
        if (preference == mNavPos) {
            int mNavPosSel = 2;
            if (objValue.toString().equals("left")) {
					mNavPosSel = 0;
            } else if (objValue.toString().equals("right")) {
					mNavPosSel = 1;
            }
            Settings.System.putInt(getContentResolver(), Settings.System.NAV_BAR_POS, mNavPosSel);
//Slog.d("NavBarPos","Selected Position = "+mNavPosSel);
//Slog.d("NavBarPos","Selected Position String = "+objValue.toString());
           return true;
        }
        // **** BEEGEE_PATCH_END ****
        return false;
    }

    private void updateExpandedDesktop(int value) {
        ContentResolver cr = getContentResolver();
        Resources res = getResources();
        int summary = -1;

        Settings.System.putInt(cr, Settings.System.EXPANDED_DESKTOP_STYLE, value);

        if (value == 0) {
            // Expanded desktop deactivated
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 0);
            Settings.System.putInt(cr, Settings.System.EXPANDED_DESKTOP_STATE, 0);
            summary = R.string.expanded_desktop_disabled;
        } else if (value == 1) {
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
            summary = R.string.expanded_desktop_status_bar;
        } else if (value == 2) {
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
            summary = R.string.expanded_desktop_no_status_bar;
        }

        if (mExpandedDesktopPref != null && summary != -1) {
            mExpandedDesktopPref.setSummary(res.getString(summary));
        }
    }

}
