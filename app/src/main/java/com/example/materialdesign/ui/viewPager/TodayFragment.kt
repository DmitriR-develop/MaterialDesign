package com.example.materialdesign.ui.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.example.materialdesign.R
import com.example.materialdesign.ui.MainActivity
import com.example.materialdesign.ui.picture.PictureOfTheDayData
import com.example.materialdesign.ui.picture.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class TodayFragment : Fragment() {

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentDate = Date()
        val date = "${currentDate.year}-${currentDate.month}-${currentDate.day}"
        viewModel.getData(date)
            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })

        MainActivity().setContentView(R.layout.fragment_today)
        image_view_today.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                container, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            val params: ViewGroup.LayoutParams = image_view_today.layoutParams
            params.height =
                if (isExpanded) {
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    ViewGroup.LayoutParams.MATCH_PARENT
                }
            image_view_today.layoutParams = params
            image_view_today.scaleType =
                if (isExpanded) {
                    ImageView.ScaleType.CENTER_CROP
                } else {
                    ImageView.ScaleType.FIT_CENTER
                }
        }
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "Link is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val imageView = view?.findViewById<ImageView>(R.id.image_view_today)
                    imageView?.load(url) {
                        lifecycle(this@TodayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                //showError()
                Toast.makeText(context, data.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}