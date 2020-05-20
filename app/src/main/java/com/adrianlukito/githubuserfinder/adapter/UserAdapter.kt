package com.adrianlukito.githubuserfinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrianlukito.githubuserfinder.R
import com.adrianlukito.githubuserfinder.model.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_list_item.view.*
import java.util.ArrayList

class UserAdapter(val context: Context?): RecyclerView.Adapter<UserViewHolder>() {
    private var items: ArrayList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvName.text = items.get(position).login
        Glide.with(context).load(items.get(position).avatarUrl).into(holder.imgAvatar)
    }

    fun add(item: Item) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<Item>) {
        for (result in items) {
            add(result)
        }
    }

    fun remove(item: Item?) {
        val position = items.indexOf(item)
        if (position > -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun getItem(position: Int): Item? {
        return items.get(position)
    }
}

class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvName = view.tvName
    val imgAvatar = view.imgAvatar
}
