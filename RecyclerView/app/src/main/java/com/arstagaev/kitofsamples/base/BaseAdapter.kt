package com.arstagaev.kitofsamples.base

import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * Created by agladkov on 25.12.17.
 * Use this adapter for any simple recycler view adapter you want
 *
 * arstagaev added a
 * removeItemForPosition(position: Int){} method , addItemToDefinePosition(newItem: P,position: Int) method
 * on 2020
 * i know fem methods no need, i will fix soon
 */
abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {
    protected var mDataList: MutableList<P> = ArrayList()
    private var mCallback: BaseAdapterCallback<P>? = null

    var hasItems = false


    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }

    fun detachCallback() {
        this.mCallback = null
    }
    fun clearList() {
        mDataList.clear()
        hasItems = false
        notifyDataSetChanged()
    }

    fun setList(dataList: List<P>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }

    fun addItem(newItem: P) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size - 1)
    }

    fun removeItemForPosition(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItemToTop(newItem: P) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun addItemToDefinePosition(newItem: P,position: Int){
        mDataList.add(position, newItem)
        notifyItemInserted(position)
    }



    fun updateItems(itemsList: List<P>) {
        mDataList.clear()
        setList(itemsList)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(mDataList[position])

        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(mDataList[position], holder.itemView)
        }
        holder.itemView.setOnLongClickListener {
            if (mCallback == null) {
                false
            } else {
                mCallback!!.onLongClick(mDataList[position], holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList.count()
    }
}