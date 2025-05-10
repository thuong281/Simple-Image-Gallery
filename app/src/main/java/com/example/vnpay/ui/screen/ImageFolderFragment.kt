package com.example.vnpay.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vnpay.R
import com.example.vnpay.databinding.FragmentImageFolderBinding
import com.example.vnpay.ui.viewmodel.ImageViewModel
import com.example.vnpay.ui.adapter.FolderAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageFolderFragment : Fragment() {

    private var _binding: FragmentImageFolderBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FolderAdapter
    private val imageViewmodel: ImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageFolderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            imageViewmodel.imageFolder.collectLatest {
                if (it.isEmpty()) return@collectLatest
                adapter.submitList(it)
            }
        }
    }

    private fun initViews() {
        recyclerView = binding.recycler
        recyclerView.hasFixedSize()
        adapter = FolderAdapter(requireActivity())
        recyclerView.adapter = adapter.apply {
            onFolderSelected = { pos ->
                imageViewmodel.setSelectedFolderPos(pos)
                findNavController().navigate(R.id.action_imageFolderFragment_to_imageListFragment)
            }
        }
        binding.toggleDarkMode.setImageResource(
            if (imageViewmodel.getDarkMode()) R.drawable.dark_mode else R.drawable.light_mode
        )
        binding.toggleDarkMode.setOnClickListener {
            imageViewmodel.setDarkMode(!imageViewmodel.getDarkMode())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}