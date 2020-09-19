package com.tincho5588.reddittopsreader.ui.topslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.util.Utils.calculateCreatedTimeHours

class TopsListAdapter(var posts: List<Post>, val itemClickedCallback: (post: Post) -> Unit) :
    RecyclerView.Adapter<TopsListAdapter.ViewHolder>() {
    class ViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        lateinit var cardView: CardView
        lateinit var title: TextView
        lateinit var subredit: TextView
        lateinit var created: TextView
        lateinit var author: TextView
        lateinit var comments: TextView
        lateinit var upvotes: TextView
        lateinit var seen: ImageView
        lateinit var preview: ImageView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tops_list_item, parent, false) as CardView

        val holder = ViewHolder(cardView)
        holder.cardView = cardView
        holder.title = cardView.findViewById(R.id.title)
        holder.subredit = cardView.findViewById(R.id.subreddit)
        holder.created = cardView.findViewById(R.id.created_time_hours)
        holder.author = cardView.findViewById(R.id.author)
        holder.comments = cardView.findViewById(R.id.comments_number)
        holder.upvotes = cardView.findViewById(R.id.upvotes_number)
        holder.seen = cardView.findViewById(R.id.seen_icon)
        holder.preview = cardView.findViewById(R.id.preview_image)

        return holder
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun getItem(position: Int): Post {
        return posts[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = posts[position].title
        holder.subredit.text = posts[position].subreddit_name_prefixed
        holder.author.text = posts[position].author

        holder.comments.text = holder.comments.context.getString(
            R.string.comments_count,
            posts[position].num_comments.toString()
        )
        holder.upvotes.text =
            holder.upvotes.context.getString(R.string.upvotes_count, posts[position].ups.toString())

        holder.created.text =
            holder.created.context.getString(R.string.created_hours_ago,
                calculateCreatedTimeHours(posts[position].created_utc).toString())

        if (posts[position].seen) {
            holder.seen.setImageDrawable(null)
        } else {
            holder.seen.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.seen.context,
                    R.drawable.ic_new_black
                )
            )
        }

        Glide.with(holder.preview.context)
            .load(posts[position].thumbnail)
            .error(R.drawable.ic_no_picture_black)
            .into(holder.preview)

        holder.cardView.setOnClickListener {
            itemClickedCallback(posts[holder.bindingAdapterPosition])
        }
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