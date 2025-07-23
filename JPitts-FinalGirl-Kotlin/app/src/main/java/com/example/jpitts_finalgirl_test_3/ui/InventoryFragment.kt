package com.example.jpitts_finalgirl_test_3.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.jpitts_finalgirl_test_3.R
import com.example.jpitts_finalgirl_test_3.viewmodel.InventoryViewModel
import com.example.jpitts_finalgirl_test_3.viewmodel.StoryViewModel
import androidx.navigation.fragment.findNavController


class InventoryFragment : Fragment(R.layout.fragment_inventory) {

    private val viewModel: StoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val inventoryView = view.findViewById<TextView>(R.id.inventory_text)
        val backButton = view.findViewById<Button>(R.id.back_button)

        viewModel.inventory.observe(viewLifecycleOwner) { inventoryItems ->
            val inventoryText = if (inventoryItems.isEmpty()) {
                "Inventory: (empty)"
            } else {
                "Inventory:\n" + inventoryItems.joinToString("\n") { "- ${it.name}: ${it.description}" }
            }
            inventoryView.text = inventoryText
        }

        backButton.setOnClickListener {
            findNavController().popBackStack() //go back to StoryFragment
        }
    }
}
