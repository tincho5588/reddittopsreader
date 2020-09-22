package com.tincho5588.reddittopsreader.ui.topslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tincho5588.reddittopsreader.BR
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post

class TopsListAdapter(var posts: List<Post>, val itemClickedCallback: (post: Post) -> Unit) :
    RecyclerView.Adapter<TopsListAdapter.PostViewHolder>() {
    class PostViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, onClickListener: View.OnClickListener) {
            binding.setVariable(BR.postItem, post)
            binding.setVariable(BR.postItemClickListener, onClickListener)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.tops_list_item,
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun getItem(position: Int): Post {
        return posts[position]
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, View.OnClickListener { itemClickedCallback(item) })
    }

    fun updateDataset(newData: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return posts.size
            }

            override fun getNewListSize(): Int {
                return newData.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return posts[oldItemPosition].id == newData[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return posts[oldItemPosition] == newData[newItemPosition]
            }
        })

        posts = newData
        diffResult.dispatchUpdatesTo(this)
    }
}