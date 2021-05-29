package com.arstagaev.nativeblescanmodule.adapter


import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import com.arstagaev.kitofsamples.base.BaseAdapter
import com.arstagaev.kitofsamples.base.BaseViewHolder
import com.arstagaev.nativeblescanmodule.base.ItemElementsDelegate
import com.arstagaev.nativeblescanmodule.R


class ShowWorkersAdapter: BaseAdapter<ScanResult>() {

    var pairDelegate: ItemElementsDelegate<ScanResult>? = null

    fun attachDelegate(callback: ItemElementsDelegate<ScanResult>) {
        this.pairDelegate = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ScanResult> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_scan_result,parent,false))

    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<ScanResult>(itemView = itemView){
        private val txtName : TextView = itemView.findViewById(R.id.device_name)
        private val macAdrress : TextView = itemView.findViewById(R.id.mac_address)
        private val rssiLevel : TextView = itemView.findViewById(R.id.signal_strength)
        val cardLayout : ConstraintLayout = itemView.findViewById(R.id.tabletXS)
       // private val txtBdayOrAge : TextView = itemView.findViewById(R.id.bday_date_or_and_age)

        override fun bind(model: ScanResult) {
            txtName.text = model.device.name
            macAdrress.text = model.device.address
            rssiLevel.text = model.rssi.toString()+" dBm"
            cardLayout.setOnClickListener {

                pairDelegate?.onElementClick(model,itemView,this.adapterPosition)

            }
        }
    }
}