package com.eggy.aplikasiabsensi.ui.absent

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.databinding.FragmentAbsentBinding
import com.eggy.aplikasiabsensi.model.ActionResponse
import com.eggy.aplikasiabsensi.networking.NetworkModule
import com.eggy.aplikasiabsensi.ui.presence.PresenceFragment
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AbsentFragment : Fragment() {


    private var binding: FragmentAbsentBinding? = null

    private lateinit var preference: UserPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAbsentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = UserPreference(activity!!.applicationContext)
        onClick()

    }


    private fun onClick() {

        binding?.btnBack?.setOnClickListener {
            back()

        }
        binding?.btnKirim?.setOnClickListener {

            val status = binding?.listItem?.selectedItem.toString()
            val id = preference.getId()
            val ket = binding?.edtKeterangan?.text.toString()
            val device = Build.MANUFACTURER + ' ' + Build.BRAND
            if (binding?.edtKeterangan?.text!!.isEmpty()) {
                binding?.edtKeterangan?.error = "Keterangan tidak boleh Kosong"
            } else {
                ProgressDialog.showProgress(requireContext())
                NetworkModule.getRetrofit()
                    .send(id ?: 0, status, ket, device, getCurrentTime(), null)
                    .enqueue(object : Callback<ActionResponse> {
                        override fun onResponse(
                            call: Call<ActionResponse>,
                            response: Response<ActionResponse>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.status == true) {
                                    ProgressDialog.hideProgress()

                                    Toasty.success(
                                        activity!!,
                                        "Berhasil",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                    back()

                                } else {
                                    ProgressDialog.hideProgress()
                                    Toasty.error(activity!!, "Gagal", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ActionResponse>, t: Throwable) {
                            ProgressDialog.hideProgress()
                            Log.e("err", t.message.toString())
                            Toasty.error(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }


    }

    private fun back() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.main_frame, PresenceFragment())
            ?.commit()
    }

    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentTime)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}