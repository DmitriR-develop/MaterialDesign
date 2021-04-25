package com.example.materialdesign.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.materialdesign.R
import com.example.materialdesign.ui.picture.PictureOfTheDayFragment
import com.example.materialdesign.ui.settings.NAME_SHARED_PREFERENCE
import com.example.materialdesign.ui.settings.THEME_ID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val idTheme = getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
            .getInt(THEME_ID, R.style.AppTheme)
        setTheme(idTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                    .commitNow()
        }
    }
}