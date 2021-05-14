package com.example.materialdesign.ui.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.materialdesign.R
import com.example.materialdesign.ui.MainActivity
import com.example.materialdesign.ui.list.ListFragment
import com.example.materialdesign.ui.settings.SettingsFragment
import com.example.materialdesign.ui.viewPager.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.main_fragment.*

class PictureOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }
    private lateinit var bottomSheetTittle: TextView
    private lateinit var bottomSheetText: TextView
    private lateinit var textDate: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentDate = com.example.materialdesign.ui.viewPager.Date()
        var date = "${currentDate.year}-${currentDate.month}-${currentDate.day}"
        view_pager.currentItem = 2
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> date = "${currentDate.year}-${currentDate.month}-${currentDate.day - 2}"
                    1 -> date = "${currentDate.year}-${currentDate.month}-${currentDate.day - 1}"
                    2 -> date = "${currentDate.year}-${currentDate.month}-${currentDate.day}"
                }
                viewModel.getData(date)
                    .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
                textDate.text = date
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int,
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewModel.getData(date)
            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
        textDate.text = date
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = ViewPagerAdapter(childFragmentManager)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
        setBottomAppBar(view)
        bottomSheetTittle = view.findViewById(R.id.bottom_sheet_description_header)
        bottomSheetText = view.findViewById(R.id.bottom_sheet_description)
        textDate = view.findViewById(R.id.text_view_date)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_navigation_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list -> activity?.supportFragmentManager?.apply {
                beginTransaction()
                    ?.replace(R.id.container, ListFragment())?.addToBackStack(null)
                    ?.commit()
            }
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

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //showError("Сообщение, что ссылка пустая")
                    toast("Link is empty")
                } else {
                    //showSuccess()
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = serverResponseData.explanation
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}