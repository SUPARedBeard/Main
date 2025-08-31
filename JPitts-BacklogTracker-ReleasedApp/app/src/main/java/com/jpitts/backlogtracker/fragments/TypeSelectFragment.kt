package com.jpitts.backlogtracker.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jpitts.backlogtracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TypeSelectFragment : Fragment(R.layout.fragment_type_select) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.gamesButton).setOnClickListener {
            val action = TypeSelectFragmentDirections.actionTypeSelectFragmentToItemListFragment("game")
            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.moviesButton).setOnClickListener {
            val action = TypeSelectFragmentDirections.actionTypeSelectFragmentToItemListFragment("movie")
            findNavController().navigate(action)
        }

        view.findViewById<FloatingActionButton>(R.id.fab_about).setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }
    }


}
