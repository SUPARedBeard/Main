//Josh Pitts
//CPT 188
//Lab 2 Shiny Calculator

package com.example.jpitts_shinyoddscalculator_2

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.example.jpitts_shinyoddscalculator_2.databinding.ActivityMainBinding
import kotlin.math.pow
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ShinyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: ShinyViewModel by viewModels()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val oddsArray = resources.getStringArray(R.array.shiny_odds_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, oddsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOdds.adapter = adapter

        restoreUIState()



        //mapping the spinner text to odds by method
        val oddsValues = mapOf(
            "Base Odds (1 in 4096)" to 4096,
            "Shiny Charm (1 in 1365)" to 1365,
            "Masuda Method (1 in 683)" to 683,
            "Masuda + Shiny Charm (1 in 512)" to 512,
            "Community Day (1 in 25)" to 25,
            "Custom" to -1
        )


        //for the spinner listener
        binding.spinnerOdds.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.selectedOddsIndex = position
                val selected = binding.spinnerOdds.selectedItem.toString()
                binding.editCustomOdds.visibility =
                    if (selected == "Custom") View.VISIBLE else View.GONE

                viewModel.resultText = ""
                binding.resultText.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}


        }



        binding.buttonCalculate.setOnClickListener {

            viewModel.attempts = binding.editAttempts.text.toString()
            viewModel.customOdds = binding.editCustomOdds.text.toString()


            val selected = binding.spinnerOdds.selectedItem.toString()
            val attempts = binding.editAttempts.text.toString().toIntOrNull()
            val odds = if (selected =="Custom"){
                binding.editCustomOdds.text.toString().toIntOrNull()
            }
            else
            {
                oddsValues[selected]
            }

            if (odds == null || attempts == null || odds <= 0 || attempts < 0) {
                viewModel.resultText = "Please enter valid numbers!"
                binding.resultText.text = viewModel.resultText
                return@setOnClickListener
            }


            val pFail = (1 - 1.0 / odds).pow(attempts)
            val pSuccess = 1 - pFail

            val comment = when {
                pSuccess > 0.9 -> "🎉 You're golden!"
                pSuccess > 0.5 -> "🤞 You're doing okay."
                pSuccess > 0.1 -> "😬 It's dicey."
                else -> "💀 You're so screwed."
            }

            viewModel.resultText = """
            Success Chance: ${"%.2f".format(pSuccess * 100)}%
            Failure Chance: ${"%.2f".format(pFail * 100)}%
            
            $comment
            """.trimIndent()

            binding.resultText.text = viewModel.resultText
        }



    }

    private fun restoreUIState() {
        binding.spinnerOdds.setSelection(viewModel.selectedOddsIndex)
        binding.editAttempts.setText(viewModel.attempts)
        binding.editCustomOdds.setText(viewModel.customOdds)

        val selected = binding.spinnerOdds.selectedItem?.toString() ?: ""
        binding.editCustomOdds.visibility = if (selected == "Custom") View.VISIBLE else View.GONE
        binding.resultText.text = viewModel.resultText
    }
}