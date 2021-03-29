package com.eggy.aplikasiabsensi.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eggy.aplikasiabsensi.databinding.ActivityLoginBinding
import com.eggy.aplikasiabsensi.model.DataItem
import com.eggy.aplikasiabsensi.model.LoginResponse
import com.eggy.aplikasiabsensi.networking.NetworkModule
import com.eggy.aplikasiabsensi.ui.MainActivity
import com.eggy.aplikasiabsensi.ui.register.RegisterActivity
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = UserPreference(this)
        initAppbar()
        onClick()

    }

    private fun login(email: String, password: String) {

        ProgressDialog.showProgress(this)
        NetworkModule.getRetrofit().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()?.isSuccess == true) {
                            val data = response.body()?.data?.first()

                            ProgressDialog.hideProgress()
                            setPreference(data)
                            moveActivity(this@LoginActivity, MainActivity::class.java)
                            finishAffinity()
                        } else {
                            ProgressDialog.hideProgress()
                            Toasty.error(
                                this@LoginActivity,
                                "Email atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("err", t.message.toString())
                    ProgressDialog.hideProgress()
                    Snackbar.make(binding.mainLogin, t.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            })
    }


    private fun setPreference(data: DataItem?) {
        preference.setStatusUser(true)
        preference.setId(data?.id?.toInt()!!)
        preference.setName(data.nama.toString())
        preference.setEmail(data.email.toString())
        preference.setJekel(data.jekel.toString())
        preference.setAlamat(data.alamat.toString())
        preference.setSekolah(data.asalSekolah.toString())
        preference.setHP(data .noHp.toString())
    }

    private fun onClick() {
        binding.toolbarLogin.setNavigationOnClickListener {
            finish()
        }
        binding.tvDaftar.setOnClickListener {
            moveActivity(this, RegisterActivity::class.java)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (isValid(email, password)) {
                login(email, password)
            }
        }
    }

    private fun moveActivity(context: Context, aim: Class<*>) {
        val intent = Intent(context, aim)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun isValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email harus diisi"
            binding.edtEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Email Harus valid"
            binding.edtPassword.requestFocus()
        } else if (password.isEmpty()) {
            binding.edtPassword.error = "Password harus diisi"
            binding.edtPassword.requestFocus()
        } else {
            binding.edtEmail.error = null
            binding.edtPassword.error = null
            return true
        }
        return false
    }

    private fun initAppbar() {
        setSupportActionBar(binding.toolbarLogin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }


}