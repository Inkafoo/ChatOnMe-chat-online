package com.example.chatonme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.chatonme.R
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.models.Slider

class SliderAdapter(
    val context: Context,
    private val imageProcessing: ImageProcessing,
    private val sliders: List<Slider>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, ob: Any): Boolean {
        return view == ob as ConstraintLayout
    }

    override fun getCount(): Int = sliders.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.welcome_slider, container, false)
        val imageView = view.findViewById<ImageView>(R.id.sliderImageView)
        val titleTextView = view.findViewById<TextView>(R.id.sliderTitleTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.sliderDescriptionTextView)
        imageProcessing.setImageCenterFit(sliders[position].imagePath, imageView)
        titleTextView.text = sliders[position].title
        descriptionTextView.text = sliders[position].description
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        container.removeView(o as ConstraintLayout)
    }

    companion object {
        val sliders = listOf(
            Slider(R.mipmap.ic_launcher, "Welcome to Chat OnMe", ""),
            Slider(R.drawable.chat, "Chat", "Text with friends any time"),
            Slider(R.drawable.post, "Timeline", "Share your photos, posts and experiences on Chat OnMe"
            )
        )
    }

}