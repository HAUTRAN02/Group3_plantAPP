package com.example.apptest

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantappication.databinding.EachItemBinding
import com.squareup.picasso.Picasso


class plantAdapter (private val list:ArrayList<plantModule>) : RecyclerView.Adapter<plantAdapter.viewHolder>() {
    //Listener
    private lateinit var mListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener : onItemClickListener){
        mListener = clickListener
    }

    inner class viewHolder(var binding : EachItemBinding, clickListener : onItemClickListener) : RecyclerView.ViewHolder(binding.root){
        init{
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        with(holder.binding){
            with(list[position].pic){
                Picasso.get().load(this).into(titleImage)
            }

            val currentItem = list[position]
            holder.binding.tvName.setText(currentItem.name.toString())
            holder.binding.tvKingDom.setText(currentItem.kingdom.toString())
            holder.binding.tvFamily.setText(currentItem.family.toString())
            holder.binding.tvNote.setText(currentItem.note.toString())
            holder.binding.tvdesc.setText(currentItem.description.toString())


        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}