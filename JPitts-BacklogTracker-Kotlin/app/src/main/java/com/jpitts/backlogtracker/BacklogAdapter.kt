package com.jpitts.backlogtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpitts.backlogtracker.data.BacklogItem

class BacklogAdapter(
    private val onItemClick: (BacklogItem) -> Unit
) : ListAdapter<BacklogItem, BacklogAdapter.BacklogViewHolder>(BacklogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BacklogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return BacklogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BacklogViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class BacklogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText = itemView.findViewById<TextView>(R.id.titleText)
        private val statusText = itemView.findViewById<TextView>(R.id.statusText)
        private val platformText = itemView.findViewById<TextView>(R.id.platformText)
        private val streamingServiceText = itemView.findViewById<TextView>(R.id.streamingServiceText)
        private val coverArtImage = itemView.findViewById<ImageView>(R.id.coverArtImage)

        fun bind(item: BacklogItem) {
            titleText.text = item.title
            statusText.text = item.status

            if (item.type == "movie") {
                platformText.visibility = View.GONE
                streamingServiceText.visibility = View.VISIBLE
                streamingServiceText.text = item.streamingService ?: "Unknown"
            } else {
                streamingServiceText.visibility = View.GONE
                platformText.visibility = View.VISIBLE
                platformText.text = item.platform ?: "Unknown"
            }

            val imageUrl = when {
                item.type == "movie" && !item.coverImageUrl.isNullOrEmpty() ->
                    "https://image.tmdb.org/t/p/w500${item.coverImageUrl}"
                else -> item.coverImageUrl
            }

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image) // or your own placeholder drawable
                    .error(android.R.drawable.ic_menu_report_image)       // or your own error drawable
                    .into(itemView.findViewById<ImageView>(R.id.coverArtImage))
            } else {
                // If no image URL, show placeholder
                itemView.findViewById<ImageView>(R.id.coverArtImage)
                    .setImageResource(android.R.drawable.ic_menu_report_image)
            }

            itemView.setOnClickListener { onItemClick(item) }
        }
    }


    class BacklogDiffCallback : DiffUtil.ItemCallback<BacklogItem>() {
        override fun areItemsTheSame(oldItem: BacklogItem, newItem: BacklogItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BacklogItem, newItem: BacklogItem) = oldItem == newItem
    }
}
