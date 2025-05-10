package com.example.vnpay.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vnpay.R
import com.example.vnpay.databinding.FragmentImageListBinding
import com.example.vnpay.ui.adapter.ImageAdapter
import com.example.vnpay.ui.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageListFragment : Fragment() {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private val imageViewmodel: ImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews() {
        recyclerView = binding.recyclerView
        recyclerView.hasFixedSize()
        adapter = ImageAdapter(requireActivity())
        recyclerView.adapter = adapter.apply {
            onPictureSelected = { selectedImagePos ->
                imageViewmodel.updateCurrentSelectImage(selectedImagePos)
                findNavController().navigate(R.id.action_imageListFragment_to_imageDetailFragment)
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                imageViewmodel.selectedFolder.collectLatest { folder ->
                    folder?.let {
                        binding.folderName.text = folder.folderName
                        adapter.submitList(folder.images)
                    }
                }
            }
            launch {
                imageViewmodel.currentSelectedImagePos.collectLatest {
                    if (it == -1) return@collectLatest
                    binding.recyclerView.post {
                        binding.recyclerView.scrollToPosition(it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}