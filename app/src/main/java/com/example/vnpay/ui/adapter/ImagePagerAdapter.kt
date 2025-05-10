package com.example.vnpay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vnpay.R
import com.example.vnpay.data.model.Image

class ImagesPagerAdapter(val context: Context, var allImages: List<Image>) : PagerAdapter() {

    override fun getCount(): Int {
        return allImages.size
    }

    override fun instantiateItem(containerCollection: ViewGroup, position: Int): Any {
        val layoutInflater =
            containerCollection.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = layoutInflater.inflate(R.layout.image_detail_pager_item, null)
        val imageView = view.findViewById<ImageView>(R.id.image)

        ViewCompat.setTransitionName(imageView, position.toString() + "picture")

        val image: Image = allImages[position]
        Glide.with(context)
            .load(image.imagePath)
            .apply(RequestOptions().fitCenter())
            .into(imageView)

        (containerCollection as ViewPager).addView(view)
        return view
    }

    override fun destroyItem(containerCollection: ViewGroup, position: Int, view: Any) {
        (containerCollection as ViewPager).removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === (`object` as View)
    }
}