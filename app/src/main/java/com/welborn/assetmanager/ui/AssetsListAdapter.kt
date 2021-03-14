package com.welborn.assetmanager.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.welborn.assetmanager.databinding.AssetListRowBinding

class AssetsListAdapter(
        private var mlist: List<AssetModel>,
        private val assetAdapterClick: AssetAdapterClick
) : RecyclerView.Adapter<AssetsListAdapter.ViewHolder>() {

    var selectedPosition = 0

    inner class ViewHolder(val itemBinding: AssetListRowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AssetListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    fun loadItems(mlistData: List<AssetModel>) {
        mlist = mlistData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: AssetModel = mlist[position]
        holder.itemBinding.title.text = item.title

        if (selectedPosition === position)
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()

            assetAdapterClick.getClickedPos(selectedPosition)
        }
    }
}