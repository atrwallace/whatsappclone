package com.example.whatsappclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.*
import com.example.whatsappclone.R
import com.example.whatsappclone.config.ConfigFirebase
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var auth = ConfigFirebase.getAuth()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewers()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this@MainActivity, MessageActivity::class.java))
            finish()
        } else {

        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        repeatAnim()
    }

    fun initViewers() {}
    fun initListeners() {
        binding.btnLogin.setOnClickListener { openLogin() }
        binding.signUpTxt.setOnClickListener { openSignUp() }
    }


    fun repeatAnim() {
        val fadein = AlphaAnimation(0f, 1f)
        val fadeout = AlphaAnimation(1f, 0f)
        fadein.apply {
            interpolator = DecelerateInterpolator()
            duration = 1500
        }
        fadeout.apply {
            interpolator = AccelerateInterpolator()
            duration = 1500
        }
        val anim = AnimationSet(false)
        anim.apply {
            addAnimation(fadein)
            addAnimation(fadeout)
            anim.animations
        }
        binding.txtTitleZap.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.txtTitleZap.startAnimation(anim)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    fun openLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    fun openSignUp() {
        startActivity(Intent(this@MainActivity, SinUpActivity::class.java))
        finish()

    }
}