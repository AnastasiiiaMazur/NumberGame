package com.example.w1720335

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class EndGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        val score: TextView = findViewById<TextView>(R.id.score)
        val correct: TextView = findViewById<TextView>(R.id.correct)
        val incorrect: TextView = findViewById<TextView>(R.id.incorrect)

        var correct_answer = intent.getStringExtra("correct")
        var incorrect_answer = intent.getStringExtra("incorrect")
        score.setText(correct_answer)
        correct.setText("Correct answers: "+correct_answer)
        incorrect.setText("Incorrect answers: "+incorrect_answer)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}