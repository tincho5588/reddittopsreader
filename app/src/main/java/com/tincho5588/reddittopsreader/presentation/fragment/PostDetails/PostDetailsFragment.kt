package com.tincho5588.reddittopsreader.presentation.fragment.PostDetails

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.databinding.PostDetailsFragmentBinding
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.Status
import com.tincho5588.reddittopsreader.presentation.viewmodel.PostsViewModel
import com.tincho5588.reddittopsreader.util.Utils.downloadPictureToStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.post_details_fragment.*

@AndroidEntryPoint
class PostDetailsFragment : Fragment(R.layout.post_details_fragment) {
    companion object {
        const val POST_DETAILS_FRAGMENT_TAG = "POST_DETAILS_FRAGMENT"
        const val POST_ARG_KEY = "POST_ARG"

        fun newInstance(postId: String): Fragment {
            val args = Bundle(1)
            args.putString(POST_ARG_KEY, postId)

            val fragment =
                PostDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: PostsViewModel by activityViewModels()
    lateinit var postId: String
    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = arguments?.getString(POST_ARG_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PostDetailsFragmentBinding.inflate(
            inflater, container, false
        )

        viewModel.getPostDetails(postId).observe(viewLifecycleOwner) { result ->
            if (result.status != Status.LOADING) {
                post = result.data!!
                binding.postItem = result.data
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if (!isValidExtension()) {
            Toast.makeText(
                requireContext(),
                R.string.invalid_picture_format_message,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

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
    }

    private fun isValidExtension(): Boolean {
        val extension = post.url.substring(post.url.lastIndexOf("."))
        return listOf(".jpg", ".jpeg", ".png").contains(extension)
    }
}