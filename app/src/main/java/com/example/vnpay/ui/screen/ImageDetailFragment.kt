package com.example.vnpay.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.vnpay.databinding.FragmentImageDetailBinding
import com.example.vnpay.ui.adapter.ImagesPagerAdapter
import com.example.vnpay.ui.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageDetailFragment : Fragment() {

    private var _binding: FragmentImageDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var imagePagerAdapter: ImagesPagerAdapter
    private val imageViewmodel: ImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews() {
        binding.imagePager.offscreenPageLimit = 3
        binding.imagePager.addOnPageChangeListener(
            object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) = Unit

                override fun onPageSelected(position: Int) {
                    imageViewmodel.updateCurrentSelectImage(position)
                }

                override fun onPageScrollStateChanged(state: Int) = Unit
            })
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            imageViewmodel.selectedFolder.collectLatest { folder ->
                folder?.let {
                    imagePagerAdapter = ImagesPagerAdapter(context = requireContext(), allImages = folder.images)
                    imagePagerAdapter.notifyDataSetChanged()
                    binding.imagePager.adapter = imagePagerAdapter
                    binding.imagePager.currentItem = imageViewmodel.currentSelectedImagePos.value
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}