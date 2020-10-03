package com.tincho5588.reddittopsreader.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.presentation.fragment.PostClickedListener
import com.tincho5588.reddittopsreader.presentation.fragment.PostDetailsFragment
import com.tincho5588.reddittopsreader.presentation.fragment.PostDetailsFragment.Companion.POST_ARG_KEY
import com.tincho5588.reddittopsreader.presentation.fragment.PostDetailsFragment.Companion.POST_DETAILS_FRAGMENT_TAG
import com.tincho5588.reddittopsreader.presentation.fragment.TopsListFragment
import com.tincho5588.reddittopsreader.presentation.fragment.TopsListFragment.Companion.TOPS_LIST_FRAGMENT_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : FragmentActivity(R.layout.activity_main),
    PostClickedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar(toolbar)

        // Hilt does not support retaining fragment. Due to it, we save the state
        // and re-attach the fragment when the activity gets recreated on rotation
        if (savedInstanceState == null) {
            displayPostsListFragment()
        } else if (savedInstanceState.containsKey(POST_ARG_KEY)) {
            displayPostDetailsFragment(savedInstanceState.getString(POST_ARG_KEY)!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val detailsFragment = supportFragmentManager.findFragmentByTag(POST_DETAILS_FRAGMENT_TAG) as PostDetailsFragment?
        detailsFragment?.let {
            outState.putString(POST_ARG_KEY, detailsFragment.postId)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onPostItemClicked(post: Post) {
        displayPostDetailsFragment(post.id)
    }

    private fun displayPostDetailsFragment(postId: String) {
        var container = tops_list_fragment_container.id
        post_details_fragment_container?.let { landscapeContainer ->
            container = landscapeContainer.id
        }

        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.top_detail_fragment_enter,
                R.anim.top_detail_fragment_exit,
                R.anim.top_detail_fragment_enter,
                R.anim.top_detail_fragment_exit
            )
            .replace(
                container,
                PostDetailsFragment.newInstance(postId),
                POST_DETAILS_FRAGMENT_TAG
            )
            .addToBackStack(POST_DETAILS_FRAGMENT_TAG)
            .commit()
    }

    private fun displayPostsListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                tops_list_fragment_container.id,
                TopsListFragment.newInstance(),
                TOPS_LIST_FRAGMENT_TAG
            )
            .commit()
    }
}