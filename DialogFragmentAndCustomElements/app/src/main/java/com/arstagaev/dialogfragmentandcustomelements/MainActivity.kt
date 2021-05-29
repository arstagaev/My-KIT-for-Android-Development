package com.arstagaev.dialogfragmentandcustomelements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        val prev: Fragment? = supportFragmentManager.findFragmentByTag("dialog")
//        if (prev != null) {
//            ft.remove(prev)
//        }
//        ft.addToBackStack(null)
//        val dialogFragment: DialogFragment = DialogFragmentX()
//        dialogFragment.show(ft, "dialog")
        DialogFragmentX().show(supportFragmentManager,"dialog")
    }
}