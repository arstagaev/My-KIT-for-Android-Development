package com.arstagaev.kitofsamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arstagaev.kitofsamples.adapters.ShowWorkersAdapter
import com.arstagaev.kitofsamples.base.ItemElementsDelegate
import com.arstagaev.kitofsamples.models.WorkerInfo

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var recycler_view : RecyclerView
    private lateinit var mainScreenLayout: RelativeLayout
    private val mAdapter = ShowWorkersAdapter()

    private var listOfWorkers = arrayListOf<WorkerInfo>(
        WorkerInfo(1,"Vasya","Popkin","1967","http://"),
        WorkerInfo(2,"Vasya","Pupkin","1949","http://"),
        WorkerInfo(1,"Armen","Bagdoburyan","2011","http://")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view = findViewById(R.id.list_of_workers)

        initRecyclerview()
        refreshRecyclerview()
    }

    private fun refreshRecyclerview() {
        mAdapter.updateItems(listOfWorkers)
    }

    private fun initRecyclerview() {

        mAdapter.attachDelegate(object : ItemElementsDelegate<WorkerInfo> {
            override fun onElementClick(model: WorkerInfo, view: View, clickedPosition: Int) {

                Log.d(TAG,"click" + model.fName)
            }
            //            override fun onElementClick(model: WorkerInfo, view: View, clickedPosition: Int) {
            //
            //            }
        })

        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(false)
    }
}