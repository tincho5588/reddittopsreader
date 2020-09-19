package com.tincho5588.reddittopsreader.ui.postdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.util.Utils
import kotlinx.android.synthetic.main.post_details_fragment.*
import kotlinx.android.synthetic.main.post_details_fragment.author
import kotlinx.android.synthetic.main.post_details_fragment.comments_number
import kotlinx.android.synthetic.main.post_details_fragment.created_time_hours
import kotlinx.android.synthetic.main.post_details_fragment.subreddit
import kotlinx.android.synthetic.main.post_details_fragment.title
import kotlinx.android.synthetic.main.post_details_fragment.upvotes_number

class PostDetailsFragment: Fragment(R.layout.post_details_fragment) {
    companion object {
        const val POST_DETAILS_FRAGMENT_TAG = "POST_DETAILS_FRAGMENT"
        const val POST_ARG_KEY = "POST_ARG"

        fun newInstance(post: Post): Fragment {
            val args = Bundle(1)
            args.putParcelable(POST_ARG_KEY, post)

            val fragment = PostDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post = arguments?.getParcelable(POST_ARG_KEY)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subreddit.text = post.subreddit_name_prefixed
        created_time_hours.text = getString(R.string.created_hours_ago,
            Utils.calculateCreatedTimeHours(post.created_utc).toString())
        author.text = post.author

        title.text = post.title

        comments_number?.text = getString(
            R.string.comments_count,
            post.num_comments.toString()
        )

        upvotes_number?.text = getString(
            R.string.upvotes_count,
            post.ups.toString()
        )

        Glide.with(view.context)
            .load(post.url)
            .error(R.drawable.reddit_logo)
            .into(preview_image)
    }
}