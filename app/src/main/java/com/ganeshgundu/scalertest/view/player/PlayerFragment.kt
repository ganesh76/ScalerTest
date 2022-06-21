package com.ganeshgundu.scalertest.view.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.databinding.FragmentPlayerBinding
import com.ganeshgundu.scalertest.viewmodel.MainViewModel


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null

    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    private var exoPlayer: ExoPlayer? = null

    private var videoResult: VideoResult? = null

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setHasOptionsMenu(false)
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoView.visibility = View.GONE
        binding.videoViewHint.visibility = View.VISIBLE
        mainViewModel.videoRes.observe(viewLifecycleOwner, {
            if (this.videoResult == null || this.videoResult?.sources != it.sources) {
                this.videoResult = it
                releasePlayer()
                initializePlayer()
            }
        })
    }

    private fun initializePlayer() {
        binding.videoView.visibility = View.VISIBLE
        binding.videoViewHint.visibility = View.GONE
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = exoPlayer
        val videoSource: MediaSource = ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoResult?.sources ?: ""))
        exoPlayer?.apply {
            setMediaSource(videoSource)
            seekTo(currentItem, playbackPosition)
            prepare()
            playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let {
            playbackPosition = 0L
            currentItem = 0
            playWhenReady = false
            it.release()
        }
        binding.videoView.visibility = View.GONE
        binding.videoViewHint.visibility = View.VISIBLE
        exoPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        _binding = null
    }
}