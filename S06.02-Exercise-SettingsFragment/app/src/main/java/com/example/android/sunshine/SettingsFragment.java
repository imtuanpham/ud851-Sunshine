package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import java.util.List;

/**
 * Created by tuanp on 3/24/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    // COMPLETED (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.pref_sunshine);

        // Do step 9 within onCreatePreference
        // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference pref = preferenceScreen.getPreference(i);
            setPreferenceSummary(pref);
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
        Preference pref = findPreference(key);

        if (pref != null) {
            setPreferenceSummary(pref);
        }
    }

    // Do steps 5 - 11 within SettingsFragment
    // COMPLETED (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

    // COMPLETED (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    private void setPreferenceSummary(Preference pref) {
        if(pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;

            int idx = listPref.findIndexOfValue(listPref.getValue());
            listPref.setSummary(listPref.getEntries()[idx]);

        } else if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText().toString());
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
