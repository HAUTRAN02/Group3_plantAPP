package com.example.plantappication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings


class RvAdapter (private val list: List <outData>, val onClick : rvInterface) : RecyclerView.Adapter<RvAdapter.itemViewHolder>() {
    class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView
        val img : ImageView

        init{
            title = itemView.findViewById(R.id.title)
            img = itemView.findViewById(R.id.img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home1, parent, false)
        return itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        holder.itemView.apply {
            holder.img.setImageResource(list[position].img)
            holder.title.text = list[position].title

        //listen
        holder.itemView.setOnClickListener {
            onClick.onClick(position)
        }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}


