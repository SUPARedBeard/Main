//Josh Pitts
//CPT 188
//Final Project

package com.jpitts.backlogtracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.jpitts.backlogtracker.data.BacklogDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val db = Room.databaseBuilder(
            applicationContext,
            BacklogDatabase::class.java, "backlog-db"
        ).build()
    }
}