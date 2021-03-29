package com.eggy.aplikasiabsensi.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.ui.login.LoginActivity
import com.eggy.aplikasiabsensi.utils.UserPreference

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preference = UserPreference(this)

        Handler(Looper.myLooper()!!).postDelayed({
            if (preference.getStatusUser()){
                moveActivity(this, MainActivity::class.java)

            }else{
                moveActivity(this, LoginActivity::class.java)

            }

        },2000)

    }
    private fun moveActivity(context: Context, aim:Class<*>){
        val intent = Intent(context, aim)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }


}