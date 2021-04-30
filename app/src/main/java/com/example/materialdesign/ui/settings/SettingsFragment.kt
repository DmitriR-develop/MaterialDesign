package com.example.materialdesign.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.example.materialdesign.R
import com.example.materialdesign.databinding.FragmentSettingsBinding
import com.example.materialdesign.ui.MainActivity
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
        setBottomAppBar(view)
        initChipGroup()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_navigation_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> activity?.supportFragmentManager?.apply {
                beginTransaction()
                    ?.replace(R.id.container, PictureOfTheDayFragment())?.addToBackStack(null)
                    ?.commit()
            }
            R.id.settings -> activity?.supportFragmentManager?.apply {
                beginTransaction()
                    ?.replace(R.id.container, SettingsFragment())?.addToBackStack(null)
                    ?.commit()
            }
        }
        return super.onOptionsItemSelected(item)
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
        val chipGroup = view?.findViewById<ChipGroup>(R.id.chip_choice_theme_app)
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

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}