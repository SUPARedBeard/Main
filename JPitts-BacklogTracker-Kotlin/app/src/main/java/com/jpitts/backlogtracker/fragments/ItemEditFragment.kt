package com.jpitts.backlogtracker.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jpitts.backlogtracker.R
import com.jpitts.backlogtracker.data.BacklogItem
import com.jpitts.backlogtracker.BacklogViewModel
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide




class ItemEditFragment : Fragment() {

    private val args: ItemEditFragmentArgs by navArgs()

    private lateinit var viewModel: BacklogViewModel

    private var currentItem: BacklogItem? = null
    private var posterUrlFromApi: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[BacklogViewModel::class.java]



        val titleInput = view.findViewById<EditText>(R.id.titleInput)

        val coverArtImage = view.findViewById<ImageView>(R.id.coverArtImage)


        val platformLabel = view.findViewById<TextView>(R.id.platformLabel)
        val platformInput = view.findViewById<EditText>(R.id.platformInput)
        val streamingServiceLabel = view.findViewById<TextView>(R.id.streamingServiceLabel)
        val streamingServiceInput = view.findViewById<EditText>(R.id.streamingServiceInput)
        val statusInput = view.findViewById<Spinner>(R.id.statusInput)
        val ratingInput = view.findViewById<RatingBar>(R.id.ratingInput)
        val notesInput = view.findViewById<EditText>(R.id.notesInput)
        val completedCheckbox = view.findViewById<CheckBox>(R.id.completedCheckbox)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        val itemId = args.itemId
        var currentItem: BacklogItem? = null

        viewModel.searchResults.observe(viewLifecycleOwner) { games ->
            if (games.isNotEmpty()) {
                val firstGame = games[0]
                Log.d("RAWG", "Game: ${firstGame.name}, Image URL: ${firstGame.background_image}")
                // Example: Automatically fill in the title and platform if blank
                if (titleInput.text.isBlank()) {
                    titleInput.setText(firstGame.name)
                }
                if (platformInput.text.isBlank()) {
                    platformInput.setText(firstGame.platforms?.joinToString(", ") { it.platform.name } ?: "")
                }
                else {
                    Log.d("RAWG", "No games returned")
                }

                Glide.with(requireContext())
                    .load(firstGame.background_image)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.stat_notify_error)
                    .into(coverArtImage)

            } else {
                Toast.makeText(requireContext(), "No game found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.moviePosterUrl.observe(viewLifecycleOwner) { posterPath ->
            if (posterPath != null) {
                posterUrlFromApi = "https://image.tmdb.org/t/p/w500$posterPath"
                Glide.with(requireContext())
                    .load(posterUrlFromApi)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.stat_notify_error)
                    .into(coverArtImage)
            }
        }

        titleInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val query = titleInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    if ((currentItem?.type ?: args.type) == "movie") {
                        viewModel.fetchMoviePoster(query)
                    } else {
                        viewModel.searchGames(query)
                    }
                }
            }
        }

        if (itemId != 0L) {
            viewModel.getItemById(itemId).observe(viewLifecycleOwner) { item ->
                if (item != null) {
                    currentItem = item

                    titleInput.setText(item.title)
                    platformInput.setText(item.platform)
                    streamingServiceInput.setText(item.streamingService)
                    notesInput.setText(item.notes)
                    ratingInput.rating = item.rating
                    completedCheckbox.isChecked = item.completed

                    val type = item.type
                    val statusOptions = resources.getStringArray(R.array.status_options)
                    val index = statusOptions.indexOf(item.status)
                    if (index >= 0) statusInput.setSelection(index)

                    if (item.type == "game" && item.title.isNotBlank()) {
                        viewModel.searchGames(item.title)
                    }

                    if (type == "movie") {
                        platformLabel.visibility = View.GONE
                        platformInput.visibility = View.GONE
                        streamingServiceLabel.visibility = View.VISIBLE
                        streamingServiceInput.visibility = View.VISIBLE
                    } else {
                        platformLabel.visibility = View.VISIBLE
                        platformInput.visibility = View.VISIBLE
                        streamingServiceLabel.visibility = View.GONE
                        streamingServiceInput.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        } else {
            // New item case: clear inputs or do nothing
            currentItem = null
            // Optionally clear fields explicitly here:
            titleInput.setText("")
            platformInput.setText("")
            streamingServiceInput.setText("")
            notesInput.setText("")
            ratingInput.rating = 0f
            completedCheckbox.isChecked = false
            // Set default status selection if you want
            statusInput.setSelection(0)

            val type = args.type

            if (type == "movie") {
                platformLabel.visibility = View.GONE
                platformInput.visibility = View.GONE
                streamingServiceLabel.visibility = View.VISIBLE
                streamingServiceInput.visibility = View.VISIBLE
            } else {
                platformLabel.visibility = View.VISIBLE
                platformInput.visibility = View.VISIBLE
                streamingServiceLabel.visibility = View.GONE
                streamingServiceInput.visibility = View.GONE
            }

            Log.d("ItemEditFragment", "Adding new item - no existing item loaded")
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val platform = platformInput.text.toString().trim()
            val streamingService = streamingServiceInput.text.toString().trim()
            val status = statusInput.selectedItem.toString().trim()
            val rating = ratingInput.rating
            val notes = notesInput.text.toString().trim()
            val completed = completedCheckbox.isChecked
            val type = currentItem?.type ?: arguments?.getString("type") ?: "game"


            val imageUrl = if (type == "movie") {
                // Use the movie poster URL fetched from TMDb if available, else fallback to current or null
                posterUrlFromApi ?: currentItem?.coverImageUrl
            } else {
                // For games, use RAWG image or existing cover image
                viewModel.searchResults.value?.firstOrNull()?.background_image ?: currentItem?.coverImageUrl
            }



            if (title.isBlank()) {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedItem = if (currentItem != null) {
                currentItem!!.copy(
                    title = title,
                    platform = platform,
                    streamingService = streamingService,
                    status = status,
                    rating = rating,
                    notes = notes,
                    completed = completed,
                    coverImageUrl = imageUrl
                )
            } else {
                BacklogItem(
                    title = title,
                    platform = platform,
                    streamingService = streamingService,
                    status = status,
                    rating = rating,
                    notes = notes,
                    completed = completed,
                    type = type,
                    coverImageUrl = imageUrl
                )
            }

            if (currentItem != null) {
                viewModel.update(updatedItem)
            } else {
                viewModel.insert(updatedItem)
            }

            Toast.makeText(requireContext(), "Item saved!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}
