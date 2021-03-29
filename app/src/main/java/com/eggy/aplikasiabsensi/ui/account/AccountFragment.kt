package com.eggy.aplikasiabsensi.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eggy.aplikasiabsensi.databinding.FragmentAccountBinding
import com.eggy.aplikasiabsensi.ui.login.LoginActivity
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference

class AccountFragment : Fragment() {


    private var binding: FragmentAccountBinding? = null
    private lateinit var preference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = UserPreference(activity!!.applicationContext)
        binding?.tvName?.text = preference.getName()
        binding?.btnLogout?.setOnClickListener {
            ProgressDialog.showProgress(activity)
            Handler(Looper.myLooper()!!).postDelayed({
                ProgressDialog.hideProgress()
                preference.logout()
                startActivity(Intent(activity?.applicationContext, LoginActivity::class.java))
                activity?.overridePendingTransition(0, 0)
                activity?.finish()

            }, 2000)
        }

        initView()

    }


    private fun initView() {
        binding?.bio?.tvName?.text = preference.getName()
        binding?.bio?.tvEmail?.text = preference.getEmail()
        binding?.bio?.tvJekel?.text = preference.getJekel()
        binding?.bio?.tvAlamat?.text = preference.getAlamat()
        binding?.bio?.tvCompany?.text = preference.getSekolah()
        binding?.bio?.tvHp?.text = preference.getHP()


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}