package com.jpitts.backlogtracker.splashscreens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jpitts.backlogtracker.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.jpitts.backlogtracker.SecondSplashActivity


class FirstSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_splash)

        val root = findViewById<View>(android.R.id.content)
        root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SecondSplashActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2000) // 2 seconds
    }
}
