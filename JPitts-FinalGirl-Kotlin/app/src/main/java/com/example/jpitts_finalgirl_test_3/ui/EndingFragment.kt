package com.example.jpitts_finalgirl_test_3.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.jpitts_finalgirl_test_3.R
import com.example.jpitts_finalgirl_test_3.viewmodel.StoryViewModel

class EndingFragment : Fragment(R.layout.fragment_ending) {
    private val viewModel: StoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val endingMessage = view.findViewById<TextView>(R.id.ending_message)
        val restartButton = view.findViewById<Button>(R.id.restart_button)
        val exitButton = view.findViewById<Button>(R.id.exit_button)


        val message = arguments?.getString("ENDING_MESSAGE") ?: "The End"
        endingMessage.text = message

        restartButton.setOnClickListener {
            viewModel.resetStory()
            findNavController().navigate(R.id.action_endingFragment_to_storyFragment)
        }

        exitButton.setOnClickListener {
            requireActivity().finish()
        }
    }
}
