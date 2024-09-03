package com.fststep.nxt2me.dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.dashboard.view.fragment.SubcategoryListener
import com.fststep.nxt2me.databinding.CustomviewCategoryBinding

class CategoryAdapter(val listener:SubcategoryListener, var data:MutableList<Category>):RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.let {
                    it.name.text=data[position].categoryName
                    it.recyclerView.apply {
                        layoutManager=GridLayoutManager(context,5)
                        adapter=SubCategoryAdapter(context,listener, data[position])
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CustomviewCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }

}