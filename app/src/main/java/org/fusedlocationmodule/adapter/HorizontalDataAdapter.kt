package org.fusedlocationmodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.fusedlocationmodule.databinding.ItemHorizontalStudioListBinding
import org.fusedlocationmodule.model.Activity

class HorizontalDataAdapter : ListAdapter<Activity, HorizontalDataAdapter.StudioViewHolder>(STUDIO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioViewHolder {
        val binding = ItemHorizontalStudioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudioViewHolder, position: Int) {
        val activityItem = getItem(position)
        holder.bind(activityItem)
    }

    class StudioViewHolder(private val binding: ItemHorizontalStudioListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(studio: Activity) {
            binding.gymName.text = studio.workout_name
        }
    }
    companion object {
        private val STUDIO_COMPARATOR = object : DiffUtil.ItemCallback<Activity>() {
            override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem.workout_id == newItem.workout_id
            }

            override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
