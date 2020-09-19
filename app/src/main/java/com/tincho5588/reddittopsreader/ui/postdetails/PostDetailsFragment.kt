package com.tincho5588.reddittopsreader.ui.postdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tincho5588.reddittopsreader.R

class PostDetailsFragment: Fragment(R.layout.post_details_fragment) {
    companion object {
        const val POST_DETAILS_FRAGMENT_TAG = "POST_DETAILS_FRAGMENT"

        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = PostDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}