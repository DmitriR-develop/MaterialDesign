package com.example.materialdesign.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.materialdesign.R
import com.example.materialdesign.ui.picture.PictureOfTheDayFragment

const val NAME_SHARED_PREFERENCE = "SETTINGS_THEME"
const val APP_THEME = "APP_THEME"
const val DEFAULT_THEME = 0
const val CUSTOM_THEME = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    private fun getAppTheme() {
        val sharedPref =
            getSharedPreferences(com.example.materialdesign.ui.NAME_SHARED_PREFERENCE, MODE_PRIVATE)
        if (sharedPref != null) {
            val codeStyle = sharedPref.getInt(APP_THEME, 0)
            if (codeStyle == DEFAULT_THEME) {
                setTheme(R.style.AppTheme)
            } else {
                setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar_CustomTheme)
            }
        }
    }
}