package com.tincho5588.reddittopsreader.ui.topslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.util.Utils.calculateCreatedTimeHours

class TopsListAdapter(
    itemCallback: DiffUtil.ItemCallback<Post> = PostItemDiffCallback(),
    val itemClickedCallback: (post: Post) -> Unit
) :
    PagedListAdapter<Post, TopsListAdapter.ViewHolder>(itemCallback) {

    class ViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        lateinit var cardView: CardView
        lateinit var title: TextView
        lateinit var subredit: TextView
        lateinit var created: TextView
        lateinit var author: TextView
        lateinit var comments: TextView
        lateinit var upvotes: TextView
        lateinit var newIcon: ImageView
        lateinit var thumbnail: ImageView
    }

    class PostItemDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
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
        holder.newIcon = cardView.findViewById(R.id.new_icon)
        holder.thumbnail = cardView.findViewById(R.id.preview_image)

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)?: return

        holder.title.text = item.title
        holder.subredit.text = item.subreddit_name_prefixed
        holder.author.text = item.author

        holder.comments.text = holder.comments.context.getString(
            R.string.comments_count,
            item.num_comments.toString()
        )
        holder.upvotes.text =
            holder.upvotes.context.getString(R.string.upvotes_count, item.ups.toString())

        holder.created.text =
            holder.created.context.getString(
                R.string.created_hours_ago,
                calculateCreatedTimeHours(item.created_utc).toString()
            )

        if (!item.seen) {
            holder.newIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.newIcon.context,
                    R.drawable.ic_new_black
                )
            )
        } else {
            holder.newIcon.setImageDrawable(null)
        }

        Glide.with(holder.thumbnail.context)
            .load(item.thumbnail)
            .error(R.drawable.ic_image_not_supported_black)
            .into(holder.thumbnail)

        holder.cardView.setOnClickListener {
            itemClickedCallback(item)
        }
    }

    public override fun getItem(position: Int): Post? {
        return super.getItem(position)
    }
}