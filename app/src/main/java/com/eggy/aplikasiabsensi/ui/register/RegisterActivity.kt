package com.eggy.aplikasiabsensi.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eggy.aplikasiabsensi.databinding.ActivityRegisterBinding
import com.eggy.aplikasiabsensi.model.ActionResponse
import com.eggy.aplikasiabsensi.networking.NetworkModule
import com.eggy.aplikasiabsensi.ui.SplashScreenActivity
import com.eggy.aplikasiabsensi.ui.login.LoginActivity
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var preference: UserPreference
    private lateinit var splashActivity: SplashScreenActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashActivity = SplashScreenActivity()
        preference = UserPreference(this)


        initAppbar()
        onClick()

    }

    private fun onClick() {


        binding.toolbarRegister.setNavigationOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            val nama = binding.edtNama.text.toString().trim()

            val selectedJekel = binding.rgJekel.checkedRadioButtonId
            var jekel = ""
            if (selectedJekel == binding.rbLaki.id) {
                jekel = "Laki-laki"
            } else if (selectedJekel == binding.rbPerempuan.id) {
                jekel = "Perempuan"
            }
            val alamat = binding.edtAlamat.text.toString().trim()
            val sekolah = binding.edtSekolah.text.toString().trim()
            val no_hp = binding.edtNoHp.text.toString().trim()


            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (isValid(nama, alamat, sekolah, no_hp, email, password)) {
                register(nama, jekel, alamat, sekolah, no_hp, email, password)
            }
        }

        binding.tvMasuk.setOnClickListener {
            moveActivity(this, LoginActivity::class.java)
        }
    }

    private fun isValid(
        nama: String,
        alamat: String,
        sekolah: String,
        noHp: String,
        email: String,
        password: String
    ): Boolean {
        if (nama.isEmpty()) {
            binding.edtNama.error = "Nama harus diisi"
            binding.edtNama.requestFocus()
        } else if (alamat.isEmpty()) {
            binding.edtAlamat.error = "Alamat harus diisi"
            binding.edtAlamat.requestFocus()
        } else if (sekolah.isEmpty()) {
            binding.edtSekolah.error = "Sekolah harus diisi"
            binding.edtSekolah.requestFocus()
        } else if (noHp.isEmpty()) {
            binding.edtNoHp.error = "No HP harus diisi"
            binding.edtNoHp.requestFocus()
        } else if (email.isEmpty()) {
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
        setSupportActionBar(binding.toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }


    private fun register(
        nama: String,
        jekel: String,
        alamat: String,
        sekolah: String,
        no_hp: String,
        email: String,
        password: String
    ) {

        ProgressDialog.showProgress(this)
        NetworkModule.getRetrofit().register(nama, jekel, alamat, sekolah, no_hp, email, password)
            .enqueue(object : Callback<ActionResponse> {
                override fun onResponse(
                    call: Call<ActionResponse>,
                    response: Response<ActionResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            ProgressDialog.hideProgress()
                            Toasty.success(
                                this@RegisterActivity,
                                "Pendaftaran Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            moveActivity(this@RegisterActivity, LoginActivity::class.java)
                        } else {
                            ProgressDialog.hideProgress()
                            Toasty.error(
                                this@RegisterActivity,
                                "Email sudah digunakan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ActionResponse>, t: Throwable) {
                    ProgressDialog.hideProgress()
                    Log.e("err", t.message.toString())
                    Snackbar.make(binding.mainRegister, t.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun moveActivity(context: Context, aim: Class<*>) {
        val intent = Intent(context, aim)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}