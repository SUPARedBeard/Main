package com.example.jpitts_finalgirl_test_3.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jpitts_finalgirl_test_3.R
import com.example.jpitts_finalgirl_test_3.util.MusicManager

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val musicToggle = view.findViewById<Switch>(R.id.music_toggle)
        val sfxToggle = view.findViewById<Switch>(R.id.sfx_toggle)
        val backButton = view.findViewById<Button>(R.id.back_button)

        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        musicToggle.isChecked = prefs.getBoolean("music_enabled", true)
        sfxToggle.isChecked = prefs.getBoolean("sfx_enabled", true)


        //settings for background music
        musicToggle.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("music_enabled", isChecked).apply()
            if (isChecked) {
                MusicManager.startBackgroundMusic(requireContext())
            } else {
                MusicManager.stopBackgroundMusic()
            }
        }

        sfxToggle.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sfx_enabled", isChecked).apply()
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}