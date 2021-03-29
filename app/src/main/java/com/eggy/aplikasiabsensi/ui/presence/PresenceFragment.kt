package com.eggy.aplikasiabsensi.ui.presence

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.databinding.FragmentPresenceBinding
import com.eggy.aplikasiabsensi.model.ActionResponse
import com.eggy.aplikasiabsensi.networking.NetworkModule
import com.eggy.aplikasiabsensi.ui.absent.AbsentFragment
import com.eggy.aplikasiabsensi.utils.ProgressDialog
import com.eggy.aplikasiabsensi.utils.UserPreference
import es.dmoral.toasty.Toasty
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PresenceFragment : Fragment() {
    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 1001
        private const val REQUEST_CODE_IMAGE_CAPTURE = 2001
    }

    private var binding: FragmentPresenceBinding? = null
    private lateinit var preference: UserPreference
    private var photoPath = ""


    private val cameraPermissions = arrayOf(
        permission.CAMERA,
        permission.READ_EXTERNAL_STORAGE,
        permission.WRITE_EXTERNAL_STORAGE,

        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPresenceBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference = UserPreference(activity!!.applicationContext)

        onClick()
    }

    private fun onClick() {
        binding?.btnCapture?.setOnClickListener {
            if (checkPermissions()) {
                openCamera()
            } else {
                setPermissions()
            }
        }
        binding?.btnKirim?.setOnClickListener {
            uploadAbsensi()
        }

        binding?.btnAbsen?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frame, AbsentFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }


    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    private fun uploadAbsensi() {

        val file = File(photoPath)
        val body = RequestBody.create(MediaType.parse("multipart/form-file"), file)
        val image = MultipartBody.Part.createFormData("photo", file.name, body)
        val id = preference.getId()

        val device = Build.MANUFACTURER + ' ' + Build.BRAND
        val masuk = getString(R.string.hadir)
        ProgressDialog.showProgress(requireContext())
        NetworkModule.getRetrofit().send(
            id ?: 0,
            masuk,
            getString(R.string.hadir).toString(),
            device,
            getCurrentTime(),
            image
        )
            .enqueue(object : Callback<ActionResponse> {
                override fun onResponse(
                    call: Call<ActionResponse>,
                    response: Response<ActionResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            ProgressDialog.hideProgress()

                            Toasty.success(activity!!, "Absensi berhasil", Toast.LENGTH_SHORT)
                                .show()
                            binding?.imageCapture?.visibility = View.INVISIBLE
                            binding?.btnKirim?.visibility = View.INVISIBLE
                            binding?.btnCapture?.visibility = View.VISIBLE

                            binding?.btnAbsen?.visibility = View.VISIBLE

                        } else {
                            ProgressDialog.hideProgress()
                            Toasty.error(activity!!, "Absensi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ActionResponse>, t: Throwable) {
                    ProgressDialog.hideProgress()
                    Log.e("err", t.message.toString())
                    Toasty.error(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun checkPermissions(): Boolean {
        var isPermission = false
        context?.let {
            for (permission in cameraPermissions) {
                isPermission = ActivityCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        return isPermission
    }

    private fun setPermissions() {
        requestPermissions(cameraPermissions, REQUEST_CODE_CAMERA_PERMISSIONS)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA_PERMISSIONS -> {
                var isPermission = false
                val permissionNotGranted = StringBuilder()

                for (i in permissions.indices) {
                    isPermission = grantResults[i] == PackageManager.PERMISSION_GRANTED

                    if (!isPermission) {
                        permissionNotGranted.append("${permissions[i]}\n")
                    }
                }

                if (isPermission) {
                    openCamera()
                } else {
                    Log.e("cameraError", permissionNotGranted.toString())
                }
            }

        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        context?.let {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)


            if (cameraIntent.resolveActivity(it.packageManager) != null) {
                val photoFile = try {
                    createImageFile()

                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {

                    val photoUri = FileProvider.getUriForFile(
                        context!!,
                        com.eggy.aplikasiabsensi.BuildConfig.APPLICATION_ID + ".fileprovider",
                        it
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE)

                }


            }

        }


    }

    private fun createImageFile(): File {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${time}_",
            ".jpg",
            storageDirectory
        ).apply {
            photoPath = absolutePath
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (photoPath.isNotEmpty()) {


                    val uri = Uri.parse(photoPath)

                    binding?.image?.setImageURI(uri)
                    binding?.imageCapture?.visibility = View.VISIBLE
                    binding?.btnKirim?.visibility = View.VISIBLE
                    binding?.btnCapture?.visibility = View.INVISIBLE
                    binding?.btnAbsen?.visibility = View.INVISIBLE


                }
            } else {
                if (photoPath.isNotEmpty()) {
                    val file = File(photoPath)
                    file.delete()
                    photoPath = ""
                    Toasty.error(requireContext(), "Gagal ambil foto", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

}
