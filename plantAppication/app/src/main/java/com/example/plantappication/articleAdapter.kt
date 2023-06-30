package com.example.plantappication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptest.plantAdapter
import com.example.plantappication.databinding.EachItemBinding
import com.example.plantappication.databinding.ItemArticleBinding
import com.squareup.picasso.Picasso

class articleAdapter (private val list:ArrayList<articleModule>) : RecyclerView.Adapter<articleAdapter.viewHolder>()  {
    //Listener
    private lateinit var mListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener : articleAdapter.onItemClickListener){
        mListener = clickListener
    }

    inner class viewHolder( var binding : ItemArticleBinding, clickListener : onItemClickListener) : RecyclerView.ViewHolder(binding.root){
        init{
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        with(holder.binding){
            with(list[position].pic){
                Picasso.get().load(this).into(image)
            }
            with(list[position].pic2){
                Picasso.get().load(this).into(iconuser)
            }

            val currentItem = list[position]
            holder.binding.title.setText(currentItem.title.toString())
            holder.binding.itemTitle2.setText(currentItem.nameAuthor.toString())
            holder.binding.date.setText(currentItem.postDate.toString())
            holder.binding.species.setText(currentItem.species.toString())
            holder.binding.species2.setText(currentItem.species2.toString())
            holder.binding.content.setText(currentItem.content.toString())


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}