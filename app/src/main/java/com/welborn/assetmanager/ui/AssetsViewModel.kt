package com.welborn.assetmanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssetsViewModel : ViewModel() {
    var lst = MutableLiveData<ArrayList<AssetModel>>()
    var assetlist = arrayListOf<AssetModel>()

    fun add(assetModel: AssetModel){
        assetlist.add(assetModel)
        lst.value=assetlist
    }

    fun remove(assetModel: AssetModel){
        assetlist.remove(assetModel)
        lst.value=assetlist
    }

}