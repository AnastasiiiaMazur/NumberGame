package com.example.w1720335

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val about_button: Button = findViewById<Button>(R.id.about)
        val game_button: Button = findViewById<Button>(R.id.new_game)

        about_button.setOnClickListener {
            val i = Intent(this, AboutActivity::class.java)
            startActivity(i)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        game_button.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            startActivity(i)
        }
    }
}