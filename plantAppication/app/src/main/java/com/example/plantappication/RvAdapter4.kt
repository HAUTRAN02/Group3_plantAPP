package com.example.plantappication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter4(private val list: MutableList<outData1>, val onClick: rvInterface) : RecyclerView.Adapter<RvAdapter4.itemViewHolder>() {
    class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img : ImageView

        init{
            img = itemView.findViewById(R.id.img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter4.itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home4, parent, false)
        return RvAdapter4.itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter4.itemViewHolder, position: Int) {
        holder.itemView.apply {
            holder.img.setImageResource(list[position].img)

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