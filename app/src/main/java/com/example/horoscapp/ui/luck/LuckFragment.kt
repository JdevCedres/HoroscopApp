package com.example.horoscapp.ui.luck

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.example.horoscapp.R
import com.example.horoscapp.databinding.FragmentLuckBinding
import com.google.android.material.animation.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class LuckFragment : Fragment() {

    private var _binding:FragmentLuckBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.ivRoulette.setOnClickListener { spinRoulette() }
    }

    private fun spinRoulette() {
        val random = Random
        val degrees:Int = random.nextInt(1440) + 360
        val animator:ValueAnimator = ObjectAnimator.ofFloat(binding.ivRoulette, View.ROTATION, 0f,degrees.toFloat())
        animator.duration = 2000
        animator.interpolator = DecelerateInterpolator()
        animator.doOnEnd { sliderCard() }
        animator.start()
    }
    private fun sliderCard(){
        val slidUpAnimation:Animation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        slidUpAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                binding.reverse.isVisible = true

            }

            override fun onAnimationEnd(p0: Animation?) {
                growCard()
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })
        binding.reverse.startAnimation(slidUpAnimation)

    }
    private fun growCard(){
        val growAnimation = android.view.animation.AnimationUtils.loadAnimation(requireContext(),R.anim.grow)
        growAnimation.setAnimationListener(object: AnimationListener{
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                binding.reverse.isVisible = false
                showPremonitionView()
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })
        binding.reverse.startAnimation(growAnimation)
    }
    private fun showPremonitionView(){
        val disappearAnimation = AlphaAnimation(1.0f,0.0f)
        disappearAnimation.duration = 200
        val appearAnimation = AlphaAnimation(0.0f,1.0f)
        appearAnimation.duration = 1000

        disappearAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                binding.preview.isVisible = false
                binding.prediction.isVisible = true
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })
        binding.preview.startAnimation(disappearAnimation)
        binding.prediction.startAnimation(appearAnimation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLuckBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}