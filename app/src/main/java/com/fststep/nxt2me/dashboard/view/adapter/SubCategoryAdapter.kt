package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.core.util.Utility.getImageFromAssets
import com.fststep.nxt2me.dashboard.view.fragment.SubcategoryListener
import com.fststep.nxt2me.databinding.CustomviewSubcategoryBinding

class SubCategoryAdapter(val context: Context,val listener: SubcategoryListener,var data: Category):RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:CustomviewSubcategoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bindItem(position:Int){
            data.subCategoryList!![position].apply {
                binding.let {
                    it.name.text=subCategoryName
                    it.itemRoot.setOnClickListener {
                        if(data.id==999L)
                            listener.onSubCategoryClick(parentId!!,id!!,subCategoryName!!)
                        else
                            listener.onSubCategoryClick(data.id!!,id!!,subCategoryName!!)
                    }
                    it.image.setImageBitmap(getImageFromAssets(context,imageName+".png"))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewSubcategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  data.subCategoryList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }
}