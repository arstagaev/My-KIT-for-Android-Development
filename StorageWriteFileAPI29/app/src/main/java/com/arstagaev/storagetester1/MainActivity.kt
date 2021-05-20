package com.arstagaev.storagetester1

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.*


class MainActivity : AppCompatActivity() {

    // Storage Permissions
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyStoragePermissions(this@MainActivity)
        Log.d("ccc","start!"
        )

        replaceAllFile("well.txt","Hello World!")
        appendText("wellx.txt","second append d")
        //editFile("wellx.txt","hahaha")


    }

    var lines: MutableList<String?> = ArrayList()
    var line: String? = null

    fun findAndReplacePartOfText(f1 : File) {
        try {
            //val f1 = File("d:/new folder/t1.htm")
            val fr = FileReader(f1)
            val br = BufferedReader(fr)
            while (br.readLine().also { line = it } != null) {
                if (line!!.contains("hahaha")) line = line!!.replace("hahaha", " ")
                lines.add(line)
            }
            fr.close()
            br.close()
            val fw = FileWriter(f1)
            val out = BufferedWriter(fw)
            for (s in lines) out.write(s)
            out.flush()
            out.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun appendText(sFileName: String, sBody: String){
        try {
            val root = File(Environment.getExternalStorageDirectory(), "RithmBrass")
            if (!root.exists()) {
                root.mkdirs()
            }
            val file = File(root, sFileName)

            val fileOutputStream = FileOutputStream(file,true)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.append("\n "+sBody)

            outputStreamWriter.close()
            fileOutputStream.close()
            //findAndReplacePartOfText(file)

        } catch (e: IOException) {
            Log.e("ccc","ERROR "+ e.message)
            e.printStackTrace()
        }

    }
    fun replaceAllFile(sFileName: String, sBody: String) {
        try {
            val root = File(Environment.getExternalStorageDirectory(), "RithmBrass")
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, sFileName)

            val writer = FileWriter(gpxfile)
            writer.append(sBody)
            writer.append("sad")

            writer.flush()
            writer.close()

        } catch (e: IOException) {
            Log.e("ccc","ERROR "+ e.message)
            e.printStackTrace()
        }

    }



    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}