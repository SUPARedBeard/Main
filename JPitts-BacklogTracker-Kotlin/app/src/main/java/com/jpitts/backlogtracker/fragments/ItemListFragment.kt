package com.jpitts.backlogtracker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpitts.backlogtracker.BacklogAdapter
import com.jpitts.backlogtracker.BacklogViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpitts.backlogtracker.R
import com.jpitts.backlogtracker.data.BacklogItem
import androidx.appcompat.widget.SearchView

class ItemListFragment : Fragment() {

    private val args: ItemListFragmentArgs by navArgs()
    private lateinit var viewModel: BacklogViewModel
    private lateinit var adapter: BacklogAdapter
    private lateinit var selectedType: String

    private var fullItemList: List<BacklogItem> = emptyList()
    private var currentQuery: String = ""
    private var currentFilter: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        selectedType = args.type

        val recycler = view.findViewById<RecyclerView>(R.id.itemRecyclerView)
        adapter = BacklogAdapter { item ->
            val action = if (item.type == "movie") {
                ItemListFragmentDirections.actionItemListFragmentToMovieDetailFragment(item.id.toLong())
            } else {
                ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(item.id.toLong())
            }

            findNavController().navigate(action)
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())


        view.findViewById<FloatingActionButton>(R.id.addItemFab).setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToItemEditFragment(
                itemId = 0L,
                type = selectedType
            )
            findNavController().navigate(action)
        }

        viewModel = ViewModelProvider(this)[BacklogViewModel::class.java]
        observeItems()
    }

    private fun observeItems() {
        val emptyText = view?.findViewById<View>(R.id.textViewEmpty) ?: return
        val recycler = view?.findViewById<RecyclerView>(R.id.itemRecyclerView) ?: return

        viewModel.getItemsByType(selectedType).observe(viewLifecycleOwner) { items ->
            fullItemList = items
            applyFilters()

            if (items.isNullOrEmpty()) {
                recycler.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
            } else {
                recycler.visibility = View.VISIBLE
                emptyText.visibility = View.GONE
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search by title..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText ?: ""
                applyFilters()
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyFilters() {
        var filtered = fullItemList

        if (currentQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(currentQuery, ignoreCase = true)
            }
        }

        when (currentFilter) {
            "Completed" -> filtered = filtered.filter { it.status.equals("Completed", true) }
            "In Progress" -> filtered = filtered.filter { it.status.equals("In Progress", true) }
            "Not Started" -> filtered = filtered.filter { it.status.equals("Not Started", true) }
            "A-Z" -> filtered = filtered.sortedBy { it.title }
            "Z-A" -> filtered = filtered.sortedByDescending { it.title }
        }

        adapter.submitList(filtered)
    }

    private fun showFilterDialog() {
        val options = arrayOf("All", "Completed", "In Progress", "Not Started", "A-Z", "Z-A")

        AlertDialog.Builder(requireContext())
            .setTitle("Filter Options")
            .setSingleChoiceItems(options, options.indexOf(currentFilter)) { dialog, which ->
                currentFilter = options[which]
                applyFilters()
                dialog.dismiss()
            }
            .show()
    }


}
