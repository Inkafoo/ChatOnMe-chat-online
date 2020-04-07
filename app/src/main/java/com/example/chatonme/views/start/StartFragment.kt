package com.example.chatonme.views.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.chatonme.R
import com.example.chatonme.adapters.SliderAdapter
import com.example.chatonme.databinding.FragmentStartBinding
import com.example.chatonme.di.components.ImageProcessing
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private lateinit var dotIndicators: MutableList<TextView>
    private val imageProcessing: ImageProcessing by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater)

        val sliderAdapter = SliderAdapter(context!!, imageProcessing, SliderAdapter.sliders)
        binding.sliderViewPager.adapter = sliderAdapter
        dotIndicators = mutableListOf()

        binding.sliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                addDotsIndicators(position)
            }

            override fun onPageSelected(position: Int) {
                /* This has no effect on the application hence why it has been left empty. */
            }

            override fun onPageScrollStateChanged(state: Int) {
                /* This has no effect on the application hence why it has been left empty. */
            }
        })

        addDotsIndicators(0)

        //navigate to  login or register view
        navigate(binding.loginBtn, R.id.action_mainFragment_to_loginFragment)
        navigate(binding.registerBtn, R.id.action_mainFragment_to_registerFragment)

        //get user
        val user = FirebaseAuth.getInstance().currentUser
        when{
            user != null -> navigateToHomeFragment()
        }


        return binding.root
    }

    /**
     * Responsible for adding the dot indicators to the slider.
     *
     * @param position - The current position in slider.
     */
    private fun addDotsIndicators(position: Int) {
        binding.sliderIndicators.removeAllViews()
        for (i in SliderAdapter.sliders.indices) {
            dotIndicators.add(TextView(context))

            dotIndicators[i].text = HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dotIndicators[i].textSize = 40F
            dotIndicators[i].setTextColor(context!!.resources.getColor(R.color.textColorWhite))

            binding.sliderIndicators.addView(dotIndicators[i])
        }

        if (dotIndicators.size > 0) {
            dotIndicators[position].setTextColor(context!!.resources.getColor(R.color.textColorBlack))
        }
    }

    /**
     * Go to target (login/register fragment)
     */
    private fun navigate(view: View, target: Int){
        RxView.clicks(view).map{
            when (findNavController().currentDestination!!.id) {
                R.id.mainFragment -> findNavController().navigate(target)
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Navigates to homeFragment
     */
    private fun navigateToHomeFragment(){
        when (findNavController().currentDestination!!.id) {
            R.id.mainFragment -> findNavController().navigate(R.id.action_mainFragment_to_connectBottomNavigationFragment)
        }
    }

}
