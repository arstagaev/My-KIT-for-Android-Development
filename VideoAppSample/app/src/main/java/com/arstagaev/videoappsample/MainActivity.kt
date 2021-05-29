package com.arstagaev.videoappsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Mode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var cameraView : CameraView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraView = findViewById<CameraView>(R.id.camera_one)
        cameraView.setLifecycleOwner(this);
    }

    private fun startCamera(){
        // attention

        cameraView.open()


        Log.d("ccc", " threadx " + Thread.currentThread().getName())

        cameraView.addCameraListener(object : CameraListener() {


            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                //Toast.makeText(applicationContext,"tajen rec",Toast.LENGTH_SHORT).show()
                Log.d("sss", "video catched")
                // Video was taken!
                // Use result.getFile() to access a file holding
                // the recorded video.

                //createVideo(result.file)

            }

            override fun onVideoRecordingStart() {
                super.onVideoRecordingStart()
                //Toast.makeText(applicationContext,"Strart rec",Toast.LENGTH_SHORT).show()
                Log.d("sss", "start cam")
            }

            override fun onVideoRecordingEnd() {
                super.onVideoRecordingEnd()
                //Toast.makeText(applicationContext,"STOP rec",Toast.LENGTH_LONG).show()
                Log.d("sss", "stopped cam")
            }

            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                Log.e("###","###### Error is "+exception.message)
            }
        })

        // Select output file. Make sure you have write permissions.

        cameraView.setMode(Mode.VIDEO);
        cameraView.takeVideo(getOutputVideoFile()); // !!!!!!!!!!!!!!
        Log.d("zzz"," camz engine "+cameraView.engine.name)
    }

    private fun stopCamera() {
        cameraView.stopVideo();
        cameraView.close()
        //cameraView.destroy()
    }



    private fun getOutputVideoFile(): File {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val dataPath = "$extStorageDirectory/ItelmaBLE"
        val mediaStorageDir = File(dataPath)

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir()
        }
        val sdf = SimpleDateFormat("dd.M")
        val currentDate = sdf.format(Date())

        val sdf2 = SimpleDateFormat("hh.mm.ss a")
        val currentTime = sdf2.format(Date())

        // Create a media file name
        val mediaFile: File
        val mImageName = "logs${currentDate}$currentTime)" + ".mp4"
        mediaFile = File(mediaStorageDir.path + File.separator + "NAME OF FILE")//!!!!!!!!!!!!!!!!!!!!!!!!
        Log.d("ccc", "" + mediaFile.toString())

        return mediaFile
    }

}