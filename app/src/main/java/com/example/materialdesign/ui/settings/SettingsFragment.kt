package com.example.materialdesign.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.example.materialdesign.R
import com.example.materialdesign.databinding.FragmentSettingsBinding
import com.example.materialdesign.ui.picture.PictureOfTheDayFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.main_fragment.*

const val NAME_SHARED_PREFERENCE = "SETTINGS_THEME"
const val APP_THEME = "APP_THEME"
const val DEFAULT_THEME = 0
const val CUSTOM_THEME = 1

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var themeName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.setTheme(getAppTheme(R.style.AppTheme))
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChipGroup()
    }

    private fun getAppTheme(codeStyle: Int): Int {
        return codeStyleToId(getCodeStyle(codeStyle))
    }

    private fun codeStyleToId(codeStyle: Int): Int {
        return when (codeStyle) {
            DEFAULT_THEME -> R.style.AppTheme
            CUSTOM_THEME -> R.style.Theme_MaterialComponents_Light_NoActionBar_CustomTheme
            else -> R.style.AppTheme
        }
    }

    private fun getCodeStyle(codeStyle: Int): Int {
        val sharedPref: SharedPreferences? =
            activity?.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPref?.getInt(APP_THEME, codeStyle)!!
    }

    private fun initChipGroup() {
        clickedChip(view?.findViewById(R.id.default_theme), DEFAULT_THEME)
        clickedChip(view?.findViewById(R.id.custom_theme), CUSTOM_THEME)
        val chipGroup = view?.findViewById<ChipGroup>(R.id.theme_choice)
        chipGroup?.isSelectionRequired = true
    }

    private fun clickedChip(chip: View?, codeStyle: Int) {
        chip?.setOnClickListener {
            setAppTheme(codeStyle)
            activity?.recreate()
        }
    }

    private fun setAppTheme(codeStyle: Int) {
        val sharedPref: SharedPreferences? =
            activity?.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putInt(APP_THEME, codeStyle)
        editor?.apply()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}