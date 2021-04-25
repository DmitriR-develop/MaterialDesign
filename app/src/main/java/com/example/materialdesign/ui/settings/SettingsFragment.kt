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
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.main_fragment.*

const val NAME_SHARED_PREFERENCE = "SETTINGS_THEME"
const val THEME_NAME = "THEME-NAME"
const val THEME_ID = "THEME_ID"
const val DEFAULT_THEME = "DEFAULT_THEME"
const val CUSTOM_THEME = "CUSTOM_THEME"

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var themeName: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setThemeShared()
        binding.chipChoiceThemeApp.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let { chip ->
                when (chip) {
                    binding.defaultTheme -> {
                        if (themeName != DEFAULT_THEME) {
                            requireContext().apply {
                                setTheme(R.style.AppTheme)
                                Toast.makeText(this, "Default Theme", Toast.LENGTH_SHORT).show()
                                saveThemeSettings(DEFAULT_THEME, R.style.AppTheme)
                            }
                        }
                    }
                    binding.customTheme -> {
                        if (themeName != CUSTOM_THEME) {
                            requireActivity().apply {
                                setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar_CustomTheme)
                                Toast.makeText(this, "Custom Theme", Toast.LENGTH_SHORT).show()
                                saveThemeSettings(
                                    CUSTOM_THEME,
                                    R.style.Theme_MaterialComponents_Light_NoActionBar_CustomTheme
                                )
                            }
                        }
                    }
                }
                recreate(requireActivity())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val idTheme =
            requireActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
                .getInt(THEME_ID, R.style.AppTheme)
        val newTheme = LayoutInflater.from(ContextThemeWrapper(context, idTheme))
        binding = FragmentSettingsBinding.inflate(newTheme, container, false)
        return binding.root
    }

    private fun saveThemeSettings(themeName: String, id: Int) {
        this.themeName = themeName
        activity?.let {
            with(it.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit()) {
                putString(THEME_NAME, themeName).apply()
                putInt(THEME_ID, id).apply()
            }
        }
    }

    private fun setThemeShared() {
        activity?.let {
            themeName =
                it.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
                    .getString(THEME_NAME, DEFAULT_THEME).toString()
            when (themeName) {
                DEFAULT_THEME -> {
                    binding.defaultTheme.isChecked = true
                }
                CUSTOM_THEME -> {
                    binding.customTheme.isChecked = true
                }
                else -> {
                    binding.defaultTheme.isChecked = true
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}