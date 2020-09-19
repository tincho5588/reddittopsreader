package com.tincho5588.reddittopsreader.ui.postdetails

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.util.Utils
import com.tincho5588.reddittopsreader.util.Utils.downloadPictureToStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.post_details_fragment.*

@AndroidEntryPoint
class PostDetailsFragment : Fragment(R.layout.post_details_fragment) {
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
        created_time_hours.text = getString(
            R.string.created_hours_ago,
            Utils.calculateCreatedTimeHours(post.created_utc).toString()
        )
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
            .error(R.drawable.ic_image_not_supported_black)
            .into(preview_image)

        registerForContextMenu(preview_image)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(0, v.id, 0, R.string.download_menu_option)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == getString(R.string.download_menu_option)) {
            when (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    requestImageDownload()
                }
                else -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        0
                    )
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestImageDownload()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.storage_permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestImageDownload() {
        if (isValidExtension()) {
            downloadPictureToStorage(
                requireContext(),
                post.id,
                preview_image.drawToBitmap()
            ) { success ->
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.download_successful_message),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.storage_failure_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                R.string.invalid_picture_format_message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValidExtension(): Boolean {
        val extension = post.url.substring(post.url.lastIndexOf("."))
        return listOf(".jpg", ".jpeg", ".png").contains(extension)
    }
}