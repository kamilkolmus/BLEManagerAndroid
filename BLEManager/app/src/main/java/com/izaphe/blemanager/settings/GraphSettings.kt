package com.izaphe.blemanager.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.*
import android.util.Log
import android.widget.Toast
import com.i7xaphe.blemanager.R

class GraphSettings : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_graph)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount

        // Go through all of the preferences, and set up their preference summary.
        for (i in 0 until count) {
            val p = prefScreen.getPreference(i)
            p.onPreferenceChangeListener = this

            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
        Log.i(TAG,"onDestroy")
    }

    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            // For list preferences, figure out the label of the selected value
            val prefIndex = preference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                // Set the summary to that label
                preference.summary = preference.entries[prefIndex]
            }
        } else if (preference is EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value)
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if (preference!!.getKey() == getString(R.string.pref_graph_history_key)) {
            val historySize = newValue as String
            try {
                val size = java.lang.Integer.parseInt(historySize)
                if (size > 1000 || size < 10) {
                    Toast.makeText(context, "Please select a number between 10 and 1000", Toast.LENGTH_SHORT).show()
                    return false
                }
            } catch (nfe: NumberFormatException) {
                 Toast.makeText(context, "Please select a number between 10 and 1000", Toast.LENGTH_SHORT).show()
                return false
            }
        }else if(preference!!.getKey() == getString(R.string.pref_graph_boundary_high_key)){
            val upperBoundary = newValue as String
            val sharedPreferences = preferenceScreen.sharedPreferences
            try {
                val size = java.lang.Integer.parseInt(upperBoundary)
                if (size <= Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_graph_boundary_low_key),""))) {
                    Toast.makeText(context, "Please select a number higher than for lower boundary", Toast.LENGTH_SHORT).show()
                    return false
                }
            } catch (nfe: NumberFormatException) {
                    Toast.makeText(context, "Please select a number higher than for lower boundary ", Toast.LENGTH_SHORT).show()
                     return false
            }

        }else if(preference!!.getKey() == getString(R.string.pref_graph_boundary_low_key)){
            val lowerBoundary = newValue as String
            val sharedPreferences = preferenceScreen.sharedPreferences
            try {
                val size = java.lang.Integer.parseInt(lowerBoundary)
                if (size >= Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_graph_boundary_high_key),""))) {
                    Toast.makeText(context, "Please select a number lower than for upper boundary", Toast.LENGTH_SHORT).show()
                    return false
                }
            } catch (nfe: NumberFormatException) {
                Toast.makeText(context, "Please select a number lower than for upper boundary ", Toast.LENGTH_SHORT).show()
                return false
            }

        }


        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        if (null != preference) {
            // Updates the summary for the preference
            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences!!.getString(preference.key, "")
                setPreferenceSummary(preference, value)
            }
        }
    }

    companion object {
        val TAG:String=GraphSettings::class.java.simpleName
    }
}