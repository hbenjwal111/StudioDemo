package org.fusedlocationmodule.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.fusedlocationmodule.R
import org.fusedlocationmodule.model.Studio
import org.fusedlocationmodule.databinding.ItemStudioListBinding

class StudioAdapter : ListAdapter<Studio, StudioAdapter.StudioViewHolder>(STUDIO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioViewHolder {
        val binding = ItemStudioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudioViewHolder, position: Int) {
        val studio = getItem(position)
        holder.bind(studio)
    }

    class StudioViewHolder(private val binding: ItemStudioListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(studio: Studio) {
            binding.gymName.text = studio.studio_app_name
            binding.gymLocation.text = studio.locality_name
            binding.gymRating.text = studio.average_rating
            binding.gymRatingGrade.text = studio.total_reviews + " " + "rating"
            loadImage(studio.studio_logo, binding.imageView)

            val horizontalAdapter = HorizontalDataAdapter()
            binding.horizontalRecyclerView.apply {
                adapter = horizontalAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                // Set your data for the horizontal RecyclerView here
                 horizontalAdapter.submitList(studio.activity_list)
            }
    }

    fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_apple)
            .error(R.drawable.img_apple)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("ImageLoadingError", "Error loading image: $e")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(imageView)
    }
}

    companion object {
        private val STUDIO_COMPARATOR = object : DiffUtil.ItemCallback<Studio>() {
            override fun areItemsTheSame(oldItem: Studio, newItem: Studio): Boolean {
                return oldItem.studio_id == newItem.studio_id
            }

            override fun areContentsTheSame(oldItem: Studio, newItem: Studio): Boolean {
                return oldItem == newItem
            }
        }
    }
}
