package com.eggy.aplikasiabsensi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.databinding.ActivityMainBinding
import com.eggy.aplikasiabsensi.ui.account.AccountFragment
import com.eggy.aplikasiabsensi.ui.history.HistoryFragment
import com.eggy.aplikasiabsensi.ui.presence.PresenceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    private fun initView() {
        binding.bottomNavigaton.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_history ->{
                    showFragment(HistoryFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_presence->{
                    showFragment(PresenceFragment())
                    return@setOnNavigationItemSelectedListener  true
                }
                R.id.action_account->{
                    showFragment(AccountFragment())
                    return@setOnNavigationItemSelectedListener  true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        showDefaultFragment()
    }

    private fun showDefaultFragment() {
        binding.bottomNavigaton.selectedItemId = R.id.action_presence
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }
}