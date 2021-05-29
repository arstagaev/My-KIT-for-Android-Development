package com.arstagaev.nativeblescanmodule.base

import android.view.View


interface ItemElementsDelegate<T> {
    fun onElementClick(model: T, view: View, clickedPosition: Int)

}