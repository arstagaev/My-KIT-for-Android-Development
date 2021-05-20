package com.arstagaev.kitofsamples.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arstagaev.kitofsamples.R
import com.arstagaev.kitofsamples.base.BaseAdapter
import com.arstagaev.kitofsamples.base.BaseViewHolder
import com.arstagaev.kitofsamples.base.ItemElementsDelegate
import com.arstagaev.kitofsamples.models.WorkerInfo


class ShowWorkersAdapter: BaseAdapter<WorkerInfo>(){

    var pairDelegate: ItemElementsDelegate<WorkerInfo>? = null

    fun attachDelegate(callback: ItemElementsDelegate<WorkerInfo>) {
        this.pairDelegate = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<WorkerInfo> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_worker,parent,false))

    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<WorkerInfo>(itemView = itemView){
        private val txtName : TextView = itemView.findViewById(R.id.f_and_l_name)
        private val txtBdayOrAge : TextView = itemView.findViewById(R.id.bday_date_or_and_age)
        private val cardLayout : ConstraintLayout = itemView.findViewById(R.id.layout_of_card)
       // private val txtBdayOrAge : TextView = itemView.findViewById(R.id.bday_date_or_and_age)

        override fun bind(model: WorkerInfo) {
            txtName.text = model.fName+" "+model.lName
            txtBdayOrAge.text = model.birthday
            cardLayout.setOnClickListener {
                pairDelegate?.onElementClick(model,itemView,this.adapterPosition)

            }
        }
    }
}