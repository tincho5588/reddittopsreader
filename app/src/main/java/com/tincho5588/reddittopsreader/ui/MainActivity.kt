package com.tincho5588.reddittopsreader.ui

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.ui.postdetails.PostDetailsFragment
import com.tincho5588.reddittopsreader.ui.postdetails.PostDetailsFragment.Companion.POST_DETAILS_FRAGMENT_TAG
import com.tincho5588.reddittopsreader.ui.topslist.PostClickedListener
import com.tincho5588.reddittopsreader.ui.topslist.TopsListFragment
import com.tincho5588.reddittopsreader.ui.topslist.TopsListFragment.Companion.TOPS_LIST_FRAGMENT_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : FragmentActivity(R.layout.activity_main), PostClickedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar(toolbar)

        supportFragmentManager.beginTransaction()
            .replace(
                tops_list_fragment_container.id,
                TopsListFragment.newInstance(),
                TOPS_LIST_FRAGMENT_TAG
            )
            .commit()
    }

    override fun onPostItemClicked(post: Post) {
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.top_detail_fragment_enter,
                R.anim.top_detail_fragment_exit,
                R.anim.top_detail_fragment_enter,
                R.anim.top_detail_fragment_exit
            )
            .replace(
                post_details_fragment_container.id,
                PostDetailsFragment.newInstance(),
                POST_DETAILS_FRAGMENT_TAG
            )
            .addToBackStack(POST_DETAILS_FRAGMENT_TAG)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}