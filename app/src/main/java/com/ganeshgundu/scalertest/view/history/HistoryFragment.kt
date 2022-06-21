package com.ganeshgundu.scalertest.view.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ganeshgundu.scalertest.R
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.databinding.FragmentHistoryBinding
import com.ganeshgundu.scalertest.view.adapter.VideosAdapter
import com.ganeshgundu.scalertest.viewmodel.MainViewModel


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setHasOptionsMenu(true);
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = VideosAdapter()
        binding.videosRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : VideosAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, videoResult: VideoResult) {
                mainViewModel.sendVideoData(videoResult)
                adapter.updateList(position)
            }
        })
        mainViewModel.getMostRecentWatchHistory()
        mainViewModel.watchHistoryList.observe(viewLifecycleOwner, {
            if (it == null || it.isEmpty()) {
                binding.statusTextView.text = context?.getString(R.string.no_watch_history)
                binding.statusProgressBar.visibility = View.GONE
                binding.statusTextView.visibility = View.VISIBLE
            } else {
                binding.statusTextView.visibility = View.GONE
                binding.statusProgressBar.visibility = View.GONE
                adapter.submitList(it)
                binding.videosRecyclerView.post {
                    binding.videosRecyclerView.layoutManager?.scrollToPosition(
                        0
                    )
                }
            }
        })

        mainViewModel.showLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.statusProgressBar.visibility = View.VISIBLE
            } else {
                binding.statusProgressBar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_count -> {
                item.setChecked(true);
                mainViewModel.getMostViewedWatchHistory()
                true
            }
            R.id.sort_by_time -> {
                item.setChecked(true);
                mainViewModel.getMostRecentWatchHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}