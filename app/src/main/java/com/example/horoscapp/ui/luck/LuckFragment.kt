package com.example.horoscapp.ui.luck

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.horoscapp.ui.core.listeners.OnSwipeTouchListener
import com.example.horoscapp.ui.model.LuckyModel
import com.example.horoscapp.ui.providers.RandomCardProvider
import com.google.android.material.animation.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class LuckFragment : Fragment() {

    private var _binding:FragmentLuckBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var randomCardProvider: RandomCardProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        preparePrediction()
        initListeners()
    }

    private fun preparePrediction() {
        val lucky:LuckyModel? = randomCardProvider.getLucky()

        lucky?.let {luck:LuckyModel ->
            val currentPrediction:String = getString(luck.text)
            binding.tvLucky.text = currentPrediction
            binding.ivLuckyCard.setImageResource(luck.image)
            binding.tvShare.setOnClickListener { shareResult(currentPrediction) }
        }
    }

    private fun shareResult(prediction:String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,prediction)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent,null)
        startActivity(shareIntent)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        //binding.ivRoulette.setOnClickListener { spinRoulette() }
        binding.ivRoulette.setOnTouchListener (object : OnSwipeTouchListener(requireContext()){
            override fun onSwipeRight() {
                spinRoulette()
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeLeft() {
                spinRoulette()
            }
        })
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