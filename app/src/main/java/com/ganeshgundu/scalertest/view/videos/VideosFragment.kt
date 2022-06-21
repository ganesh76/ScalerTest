package com.ganeshgundu.scalertest.view.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ganeshgundu.scalertest.R
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.databinding.FragmentVideosBinding
import com.ganeshgundu.scalertest.repository.VideosRepository
import com.ganeshgundu.scalertest.util.NetworkManager.isNetworkAvailable
import com.ganeshgundu.scalertest.view.adapter.VideosAdapter
import com.ganeshgundu.scalertest.viewmodel.MainViewModel

class VideosFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentVideosBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val adapter = VideosAdapter()
        binding.videosRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : VideosAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, videoResult: VideoResult) {
                viewModel.sendVideoData(videoResult)
                adapter.updateList(position)
            }
        })
        if (isNetworkAvailable(context)) {
            viewModel.getMovies()
        } else {
            binding.statusProgressBar.visibility = View.GONE
            binding.statusTextView.text = context?.getString(R.string.no_internet_error_msg)
        }


        viewModel.responseData.observe(viewLifecycleOwner, {
            when (it.responseStatus) {
                VideosRepository.ResponseStatus.SUCCESS -> {
                    val sz = it.response?.videos?.size
                    if (sz != null) {
                        if (sz > 0) {
                            adapter.submitList(it.response?.videos)
                        } else {
                            binding.statusTextView.text = context?.getString(R.string.api_error_msg)
                        }
                    }
                }
                VideosRepository.ResponseStatus.API_ERROR -> {
                    binding.statusTextView.text = context?.getString(R.string.api_error_msg)
                }
            }
        })
        viewModel.showLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.statusProgressBar.visibility = View.VISIBLE
            } else {
                binding.statusProgressBar.visibility = View.GONE
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}