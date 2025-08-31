package com.jpitts.backlogtracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jpitts.backlogtracker.R
import com.jpitts.backlogtracker.data.BacklogItem
import com.jpitts.backlogtracker.BacklogViewModel

/**
 * Fragment responsible for editing or creating a BacklogItem.
 *
 * This screen handles both movies and games, auto-fetching metadata
 * such as cover images and platform/service details when available.
 *
 * Features:
 * - Dynamically updates UI depending on item type (game vs movie).
 * - Observes ViewModel for search results and movie poster URLs.
 * - Provides validation (e.g., prevents empty titles).
 * - Persists changes by inserting or updating items in the database.
 */
class ItemEditFragment : Fragment() {

    // SafeArgs: Navigation argument containing itemId/type
    private val args: ItemEditFragmentArgs by navArgs()

    // Shared ViewModel for managing backlog items
    private lateinit var viewModel: BacklogViewModel

    private var currentItem: BacklogItem? = null
    private var posterUrlFromApi: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout for editing backlog items
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[BacklogViewModel::class.java]

        // --- UI references ---
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
        val priorityInput = view.findViewById<Spinner>(R.id.priorityInput)

        val itemId = args.itemId

        // --- Input behavior: clear platform field whenever title changes ---
        titleInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                platformInput.text.clear()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // --- Observe game search results from RAWG API ---
        viewModel.searchResults.observe(viewLifecycleOwner) { games ->
            if (games.isNotEmpty()) {
                val firstGame = games[0]
                Log.d("RAWG", "Game: ${firstGame.name}, Image URL: ${firstGame.background_image}")

                // Auto-fill details if fields are blank
                if (titleInput.text.isBlank()) titleInput.setText(firstGame.name)
                if (platformInput.text.isBlank()) {
                    platformInput.setText(
                        firstGame.platforms?.joinToString(", ") { it.platform.name } ?: ""
                    )
                }

                // Load cover image using Glide
                Glide.with(requireContext())
                    .load(firstGame.background_image)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.stat_notify_error)
                    .into(coverArtImage)

            } else {
                Toast.makeText(requireContext(), "No game found", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Observe movie poster results from TMDb API ---
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

        // --- Trigger API search when user finishes editing the title ---
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

        // --- Edit existing item if ID is provided ---
        if (itemId != 0L) {
            viewModel.getItemById(itemId).observe(viewLifecycleOwner) { item ->
                if (item != null) {
                    currentItem = item

                    // Populate UI with saved values
                    titleInput.setText(item.title)
                    platformInput.setText(item.platform)
                    streamingServiceInput.setText(item.streamingService)
                    notesInput.setText(item.notes)
                    ratingInput.rating = item.rating
                    completedCheckbox.isChecked = item.completed

                    // Set dropdown selections
                    val priorityOptions = resources.getStringArray(R.array.priority_options)
                    val priorityIndex = priorityOptions.indexOf(item.priority)
                    priorityInput.setSelection(priorityIndex.takeIf { it >= 0 } ?: 1)

                    val statusOptions = resources.getStringArray(R.array.status_options)
                    val statusIndex = statusOptions.indexOf(item.status)
                    if (statusIndex >= 0) statusInput.setSelection(statusIndex)

                    // Adjust fields based on item type
                    if (item.type == "game" && item.title.isNotBlank()) {
                        viewModel.searchGames(item.title)
                    }
                    toggleFields(item.type)

                } else {
                    Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        } else {
            // --- New item case: initialize empty fields ---
            currentItem = null
            titleInput.setText("")
            platformInput.setText("")
            streamingServiceInput.setText("")
            notesInput.setText("")
            ratingInput.rating = 0f
            completedCheckbox.isChecked = false
            statusInput.setSelection(0)

            toggleFields(args.type)
            Log.d("ItemEditFragment", "Adding new item - no existing item loaded")
        }

        // --- Save button logic: validate and persist ---
        saveButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            if (title.isBlank()) {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = currentItem?.type ?: arguments?.getString("type") ?: "game"
            val updatedItem = (currentItem?.copy(
                title = title,
                platform = platformInput.text.toString().trim(),
                streamingService = streamingServiceInput.text.toString().trim(),
                status = statusInput.selectedItem.toString(),
                rating = ratingInput.rating,
                notes = notesInput.text.toString().trim(),
                completed = completedCheckbox.isChecked,
                priority = priorityInput.selectedItem.toString(),
                coverImageUrl = resolveCoverImage(type)
            )) ?: BacklogItem(
                title = title,
                platform = platformInput.text.toString().trim(),
                streamingService = streamingServiceInput.text.toString().trim(),
                status = statusInput.selectedItem.toString(),
                rating = ratingInput.rating,
                notes = notesInput.text.toString().trim(),
                completed = completedCheckbox.isChecked,
                priority = priorityInput.selectedItem.toString(),
                type = type,
                coverImageUrl = resolveCoverImage(type)
            )

            if (currentItem != null) viewModel.update(updatedItem)
            else viewModel.insert(updatedItem)

            Toast.makeText(requireContext(), "Item saved!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    /**
     * Helper function: toggles visibility of fields depending on type.
     */
    private fun toggleFields(type: String) {
        view?.findViewById<TextView>(R.id.platformLabel)?.visibility =
            if (type == "movie") View.GONE else View.VISIBLE
        view?.findViewById<EditText>(R.id.platformInput)?.visibility =
            if (type == "movie") View.GONE else View.VISIBLE
        view?.findViewById<TextView>(R.id.streamingServiceLabel)?.visibility =
            if (type == "movie") View.VISIBLE else View.GONE
        view?.findViewById<EditText>(R.id.streamingServiceInput)?.visibility =
            if (type == "movie") View.VISIBLE else View.GONE
    }

    /**
     * Resolves the appropriate cover image based on type and API results.
     */
    private fun resolveCoverImage(type: String): String? {
        return if (type == "movie") {
            posterUrlFromApi ?: currentItem?.coverImageUrl
        } else {
            viewModel.searchResults.value?.firstOrNull()?.background_image
                ?: currentItem?.coverImageUrl
        }
    }
}
