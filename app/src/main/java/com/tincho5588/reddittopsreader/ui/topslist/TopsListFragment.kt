package com.tincho5588.reddittopsreader.ui.topslist

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.itemanimators.SlideRightAlphaAnimator
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.util.Utils.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.tops_list_fragment.*

@AndroidEntryPoint
class TopsListFragment : Fragment(R.layout.tops_list_fragment) {
    companion object {
        const val TOPS_LIST_FRAGMENT_TAG = "POST_LIST_FRAGMENT"
        private const val LAYOUT_MANAGER_STATE_KEY = "LAYOUT_MANAGER_STATE"

        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = TopsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: TopPostsViewModel by viewModels()
    private lateinit var viewAdapter: TopsListAdapter
    private lateinit var postClickedListener: PostClickedListener
    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Toast.makeText(requireContext(), R.string.dismiss_hint, Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        postClickedListener = context as PostClickedListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        startObservingData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dismiss_all -> viewModel.dismissAll()
            R.id.about -> AboutDialogFragment().show(childFragmentManager, "ABOUT_DIALOG_FRAGMENT")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            recyclerViewLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, recyclerViewLayoutManager.onSaveInstanceState())
    }

    private fun setupAdapter() {
        // RecyclerView Adapter setup
        viewAdapter = TopsListAdapter(viewModel.posts.value ?: emptyList()) {
            viewModel.markAsSeen(it)
            postClickedListener.onPostItemClicked(it)
        }
    }

    private fun setupRecyclerView() {
        // Custom ItemAnimator. The remove animation of the default RecyclerView item animator is awful
        // Using a library because writing a custom ItemAnimator would take as much work as writing this whole app
        tops_list_recycler_view.itemAnimator = SlideRightAlphaAnimator()

        // RecyclerView setup
        recyclerViewLayoutManager = LinearLayoutManager(context)
        tops_list_recycler_view.apply {
            layoutManager = recyclerViewLayoutManager
            adapter = viewAdapter
        }

        // Swipe to dismiss gesture
        val swipeToDeleteCallback = SwipeToDeleteCallback(requireContext()) {
            viewModel.dismiss(viewAdapter.getItem(it))
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(tops_list_recycler_view)
    }

    private fun startObservingData() {
        viewModel.posts.observe(viewLifecycleOwner) {
            swipe_to_refresh_layout.isRefreshing = false
            viewAdapter.updateDataset(it)
        }

        // Swipe to refresh gesture
        swipe_to_refresh_layout.setOnRefreshListener {
            viewModel.refreshPosts()
            if (!isNetworkAvailable(requireContext())) swipe_to_refresh_layout.isRefreshing = false
        }
    }
}