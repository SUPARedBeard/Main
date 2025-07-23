package com.jpitts.backlogtracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jpitts.backlogtracker.BacklogViewModel
import com.jpitts.backlogtracker.R

class MovieDetailFragment : Fragment() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var viewModel: BacklogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[BacklogViewModel::class.java]

        viewModel.getItemById(args.itemId).observe(viewLifecycleOwner) { item ->
            view.findViewById<TextView>(R.id.titleText).text = item.title
            view.findViewById<TextView>(R.id.notesText).text = item.notes
            view.findViewById<TextView>(R.id.streamingServiceText).text = item.streamingService ?: "Unknown"
            view.findViewById<RatingBar>(R.id.ratingBar).rating = item.rating
            view.findViewById<TextView>(R.id.completedStatus).text =
                if (item.completed) "Completed" else "In Progress"

            view.findViewById<Button>(R.id.shareButton).setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this ${item.type}: ${item.title} on ${item.streamingService}.\n\nNotes: ${item.notes}"
                    )
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

            val coverArtImage = view.findViewById<ImageView>(R.id.coverArtImage)

            val imageUrl = if (item.type == "movie" && !item.coverImageUrl.isNullOrEmpty()) {
                if (item.coverImageUrl.startsWith("http")) {
                    item.coverImageUrl
                } else {
                    "https://image.tmdb.org/t/p/w500${item.coverImageUrl}"
                }
            } else {
                item.coverImageUrl
            }

            Log.d("MovieDetailFragment", "Loading image URL: $imageUrl")


            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.stat_notify_error)
                .into(coverArtImage)


            view.findViewById<Button>(R.id.editButton).setOnClickListener {
                val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToItemEditFragment(item.id.toLong())
                findNavController().navigate(action)
            }

            val deleteButton = view.findViewById<Button>(R.id.deleteButton)
            deleteButton.setOnClickListener {
                viewModel.delete(item)
                Toast.makeText(requireContext(), "${item.title} deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }

        }
    }
}
