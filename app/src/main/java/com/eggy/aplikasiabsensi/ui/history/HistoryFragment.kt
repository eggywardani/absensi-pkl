package com.eggy.aplikasiabsensi.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.databinding.FragmentHistoryBinding
import com.eggy.aplikasiabsensi.model.HistoryItem
import com.eggy.aplikasiabsensi.model.PresensiResponse
import com.eggy.aplikasiabsensi.networking.NetworkModule
import com.eggy.aplikasiabsensi.utils.DateUtil.stringToDate
import com.eggy.aplikasiabsensi.utils.DateUtil.toCalendar
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {


    private var binding: FragmentHistoryBinding? = null
    private lateinit var preference: UserPreference
    private var dataHistories: List<HistoryItem?>? = null
    private val events = ArrayList<EventDay>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = UserPreference(activity!!.applicationContext)
        requestHistory()
        setUpCalendar()
        onClick()

    }

    private fun onClick() {
        binding?.calenderHistory?.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val selectedDay = eventDay.calendar
                if (dataHistories != null) {
                    for (dataHistory in dataHistories!!) {
                        val status = dataHistory?.status
                        val time = dataHistory?.waktu.toString()

                        // Tanggal Bulan , Tahun
                        val firstTime = time.split(" ")[0]

                        val year = firstTime.split("-")[0]
                        val month = firstTime.split("-")[1]
                        val date = firstTime.split("-")[2]

                        // Waktu
                        val absenTime = time.split(" ")[1]
                        // Tanggal saat ini
                        val currentDate = Calendar.getInstance()
                        val absenDay = time.stringToDate()!!.toCalendar() as Calendar

                        if (status == getString(R.string.hadir)){
                            events.add(EventDay(absenDay, R.drawable.ic_baseline_check_circle_24))
                        }else if (status == getString(R.string.izin)){
                            events.add(EventDay(absenDay, R.drawable.ic_izin))
                        }else if (status == getString(R.string.sakit)){
                            events.add(EventDay(absenDay, R.drawable.ic_sakit))
                        }


                        if (selectedDay.get(Calendar.DAY_OF_MONTH) == absenDay.get(Calendar.DAY_OF_MONTH)) {

                            if (status == getString(R.string.hadir) ){
                                binding?.ivStatus?.setImageResource(R.drawable.ic_baseline_check_circle_24)
                                binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card)
                            }else if (status == getString(R.string.izin)){
                                binding?.ivStatus?.setImageResource(R.drawable.ic_izin)
                                binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card_izin)
                            }else if (status == getString(R.string.sakit)){
                                binding?.ivStatus?.setImageResource(R.drawable.ic_sakit)
                                binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card_sakit)
                            }

                            binding?.tvStatus?.text = status
                            binding?.tvDate?.text = "Pada $date/$month/$year"
                            binding?.tvTime?.text = "pukul $absenTime"

                        }

                    }

                }

            }
        })

    }


    private fun setUpCalendar() {
        binding?.calenderHistory?.setOnPreviousPageChangeListener(object :
            OnCalendarPageChangeListener {
            override fun onChange() {
                requestHistory()
            }
        })
        binding?.calenderHistory?.setOnForwardPageChangeListener(object :
            OnCalendarPageChangeListener {
            override fun onChange() {
                requestHistory()
            }
        })


    }

    private fun requestHistory() {
        val calendar = binding?.calenderHistory?.currentPageDate
        val lastDay = calendar?.getActualMaximum(Calendar.DAY_OF_MONTH)
        val month = calendar?.get(Calendar.MONTH)?.plus(1)
        val year = calendar?.get(Calendar.YEAR)

        val fromDate = "$year-$month-01"
        val toDate = "$year-$month-$lastDay"
        Log.d(
            "cal", """
            From : $fromDate, 
            To : $toDate
        """.trimIndent()
        )
        getHistory(fromDate, toDate)
    }

    private fun getHistory(fromDate: String, toDate: String) {
        val id = preference.getId() as Int
        ProgressDialog.showProgress(requireContext())
        NetworkModule.getRetrofit().getPresensi(id, fromDate, toDate)
            .enqueue(object : Callback<PresensiResponse> {
                override fun onResponse(
                    call: Call<PresensiResponse>,
                    response: Response<PresensiResponse>
                ) {
                    if (response.isSuccessful) {
                        ProgressDialog.hideProgress()
                        dataHistories = response.body()?.data
                        if (dataHistories != null) {
                            for (dataHistory in dataHistories!!) {
                                val status = dataHistory?.status
                                val time = dataHistory?.waktu.toString()

                                // Tanggal Bulan , Tahun
                                val firstTime = time.split(" ")[0]

                                val year = firstTime.split("-")[0]
                                val month = firstTime.split("-")[1]
                                val date = firstTime.split("-")[2]

                                // Waktu
                                val absenTime = time.split(" ")[1]
                                // Tanggal saat ini
                                val currentDate = Calendar.getInstance()
                                val absenDay = time.stringToDate()!!.toCalendar() as Calendar

                                if (status == getString(R.string.hadir)){
                                    events.add(EventDay(absenDay, R.drawable.ic_baseline_check_circle_24))
                                }else if (status == getString(R.string.izin)){
                                    events.add(EventDay(absenDay, R.drawable.ic_izin))
                                }else if (status == getString(R.string.sakit)){
                                    events.add(EventDay(absenDay, R.drawable.ic_sakit))
                                }


                                if (currentDate.get(Calendar.DAY_OF_MONTH) == absenDay.get(Calendar.DAY_OF_MONTH)) {

                                    if (status == getString(R.string.hadir) ){
                                        binding?.ivStatus?.setImageResource(R.drawable.ic_baseline_check_circle_24)
                                        binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card)
                                    }else if (status == getString(R.string.izin)){
                                        binding?.ivStatus?.setImageResource(R.drawable.ic_izin)
                                        binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card_izin)
                                    }else if (status == getString(R.string.sakit)){
                                        binding?.ivStatus?.setImageResource(R.drawable.ic_sakit)
                                        binding?.cardContainer?.setBackgroundResource(R.drawable.bg_card_sakit)
                                    }

                                    binding?.tvStatus?.text = status
                                    binding?.tvDate?.text = "Pada $date/$month/$year"
                                    binding?.tvTime?.text = "pukul $absenTime"

                                }

                            }

                        }
                        binding?.calenderHistory?.setEvents(events)

                    }
                }

                override fun onFailure(call: Call<PresensiResponse>, t: Throwable) {
                    Log.e("err", t.message.toString())
                    ProgressDialog.hideProgress()
                    Snackbar.make(requireView(), t.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            })

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}