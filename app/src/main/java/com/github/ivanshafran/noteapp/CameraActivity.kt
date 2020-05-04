package com.github.ivanshafran.noteapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CameraActivity : AppCompatActivity(), ImageCapture.OnImageSavedCallback {

    companion object {
        private const val CAMERA_REQUEST_CODE = 0

        fun getIntent(context: Context) = Intent(context, CameraActivity::class.java)
    }

    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        if (checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PERMISSION_GRANTED) {
                startCamera()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    Toast.makeText(this, R.string.need_permission, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.permission_in_settings, Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startCamera() {
        camera.captureMode = CameraView.CaptureMode.IMAGE
        camera.bindToLifecycle(this as LifecycleOwner)
        val photoFile = generatePictureFile()
        this.photoFile = photoFile
        takePhotoButton.setOnClickListener {
            takePhotoButton.isEnabled = false
            camera.takePicture(photoFile, AsyncTask.SERIAL_EXECUTOR, this)
        }
    }

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        lifecycleScope.launch {
            val photoFile = photoFile ?: return@launch
            creationProgressBar.isVisible = true
            try {
                NoteCreationUseCase(this@CameraActivity).createNoteFromImage(photoFile)
            } catch (e: Exception) {
                finish()
            } finally {
                if (isActive) {
                    creationProgressBar.isVisible = false
                }
            }
        }
    }

    override fun onError(exception: ImageCaptureException) {
    }

    private fun generatePictureFile(): File {
        return File(filesDir, UUID.randomUUID().toString() + ".jpg")
    }

}
