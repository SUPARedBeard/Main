package com.example.jpitts_finalgirl_test_3.ui



import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.jpitts_finalgirl_test_3.R
import com.example.jpitts_finalgirl_test_3.repository.StoryRepository
import com.example.jpitts_finalgirl_test_3.viewmodel.StoryViewModel
import com.example.jpitts_finalgirl_test_3.viewmodel.StoryViewModelFactory
import androidx.navigation.fragment.findNavController
import com.example.jpitts_finalgirl_test_3.util.MusicManager
import com.example.jpitts_finalgirl_test_3.util.playScream




class StoryFragment : Fragment(R.layout.fragment_story) {

    //use the repository object directly
    private val repository = StoryRepository

    //pass repository to factory
    private val viewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(repository)
    }

    private val idleTimeout = 15_000L // 15 seconds
    private val handler = Handler(Looper.getMainLooper())
    private val jumpScareRunnable = Runnable {
        showJumpScare()
    }

    private fun showJumpScare() {
        MediaPlayer.create(requireContext(), R.raw.screaming)?.start()

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(300)
        }

        val jumpScareImage = ImageView(requireContext()).apply {
            setImageResource(R.drawable.jump_scare_image)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(Color.BLACK)
        }

        //flash overlay (white screen effect)
        val flashView = View(context).apply {
            setBackgroundColor(Color.WHITE)
            alpha = 0f
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        //add image to the root of the fragment
        val rootView = requireActivity().window.decorView as ViewGroup
        rootView.addView(jumpScareImage)
        rootView.addView(flashView)

        //flash animation
        flashView.animate().alpha(1f).setDuration(100).withEndAction {
            flashView.animate().alpha(0f).setDuration(100).start()
        }.start()

        //remove it after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            rootView.removeView(jumpScareImage)
        }, 2000)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val storyText = view.findViewById<TextView>(R.id.story_text)
        val choice1 = view.findViewById<Button>(R.id.choice1_button)
        val choice2 = view.findViewById<Button>(R.id.choice2_button)
        val inventoryButton = view.findViewById<Button>(R.id.inventory_button)
        val settingsButton = view.findViewById<Button>(R.id.settings_button)
        val panicButton = view.findViewById<Button>(R.id.panic_button)

        fun resetIdleTimer() {
            handler.removeCallbacks(jumpScareRunnable)
            handler.postDelayed(jumpScareRunnable, idleTimeout)
        }

        viewModel.currentPage.observe(viewLifecycleOwner) { page ->
            storyText.text = page.text
            choice1.text = page.choices.getOrNull(0)?.text ?: "End"
            choice2.text = page.choices.getOrNull(1)?.text ?: "Quit"

            val killerPages = listOf(2, 5, 7, 11, 13)
            if (page.id in killerPages) {
                playScream(requireContext())
            }

            choice1.setOnClickListener {
                viewModel.chooseOption(page.choices.getOrNull(0)?.nextPageId ?: -1)
            }
            choice2.setOnClickListener {
                viewModel.chooseOption(page.choices.getOrNull(1)?.nextPageId ?: -1)
            }
            resetIdleTimer()
        }

        viewModel.shouldNavigateToEnding.observe(viewLifecycleOwner) { shouldEnd ->
            if (shouldEnd) {
                findNavController().navigate(R.id.action_storyFragment_to_endingFragment)
            }
        }

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                resetIdleTimer()
            }
            false
        }

        inventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_inventoryFragment)
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        val musicEnabled = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getBoolean("music_enabled", true)
        if (musicEnabled) {
            MusicManager.startBackgroundMusic(requireContext())
        }

        panicButton.setOnClickListener {
            findNavController().navigate(R.id.contactsFragment)
        }

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(jumpScareRunnable) //stop when fragment not visible
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(jumpScareRunnable, idleTimeout) //restart timer
    }
}
