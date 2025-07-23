package com.example.jpitts_finalgirl_test_3.util

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.BatteryManager
import com.example.jpitts_finalgirl_test_3.R


//sends a panic SMS to a number
fun sendPanicSMS(context: Context, number: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$number")
        putExtra("sms_body", "Help! I'm in danger!")
    }
    context.startActivity(intent)
}

//plays a scream sound
fun playScream(context: Context) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    if (!prefs.getBoolean("sfx_enabled", true)) return  //don't play if SFX disabled

    val mediaPlayer = MediaPlayer.create(context, R.raw.scream)
    mediaPlayer?.apply {
        start()
        setOnCompletionListener {
            it.release()
        }
    }
}


