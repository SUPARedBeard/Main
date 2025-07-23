package com.example.jpitts_shinyoddscalculator_2

import androidx.lifecycle.ViewModel

class ShinyViewModel : ViewModel() {
    var attempts: String = ""
    var customOdds: String = ""
    var selectedOddsIndex: Int = 0
    var resultText: String = ""
}