package com.example.w1720335

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import java.util.*

class GameActivity : AppCompatActivity() {
    var timer: CountDownTimer? = null
    var correct = 0
    var incorrect = 0
    var length = 1
    var counter = 50
    var sec = 50000
    var new_sec = 0
    var numbers = mutableListOf<Int>()
    var gen_symbols = mutableListOf<String>()
    var symbols = listOf<String>("+", "-", "*", "/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val greater: Button = findViewById<Button>(R.id.greater)
        val equal: Button = findViewById<Button>(R.id.equal)
        val less: Button = findViewById<Button>(R.id.less)

        val i = Intent(this, EndGameActivity::class.java)

        var message = findViewById<TextView>(R.id.message)

        var res = main()

        if (savedInstanceState != null) {
            correct = savedInstanceState.getInt("correcr", 0)
            incorrect = savedInstanceState.getInt("incorrecr", 0)
            length = savedInstanceState.getInt("length", 0)
            counter = savedInstanceState.getInt("counter", 0)
            sec = savedInstanceState.getInt("sec", 0)
            new_sec = savedInstanceState.getInt("new_sec", 0)
        }

        timer = object: CountDownTimer(sec.toLong(), 1000) {
            val time: TextView = findViewById<TextView>(R.id.timer)
            override fun onTick(millisUntilFinished: Long) {
                time.setText((millisUntilFinished / 1000).toString())
                counter--
                new_sec = millisUntilFinished.toInt() - 1000
            }

            override fun onFinish() {
                var correct_send = correct.toString()
                var incorrect_send = incorrect.toString()
                i.putExtra("correct", correct_send)
                i.putExtra("incorrect", incorrect_send)
                startActivity(i)
            }
        }
        (timer as CountDownTimer).start()

        greater.setOnClickListener {
            if (res[0] > res[1]) {
                message.setText("Correct!")
                message.setTextColor(Color.GREEN)
                correct += 1
                if (correct % 5 == 0){
                    (timer as CountDownTimer).cancel()
                    update_timer(i)
                }
                res = main()
            } else {
                message.setText("Incorrect!")
                message.setTextColor(Color.RED)
                incorrect += 1
                res = main()
            }
        }

        equal.setOnClickListener {
            if (res[0] == res[1]) {
                message.setText("Correct!")
                message.setTextColor(Color.GREEN)
                correct += 1
                if (correct % 5 == 0){
                    (timer as CountDownTimer).cancel()
                    update_timer(i)
                }
                res = main()
            } else {
                message.setText("Incorrect!")
                message.setTextColor(Color.RED)
                incorrect += 1
                res = main()
            }
        }

        less.setOnClickListener {
            if (res[0] < res[1]) {
                message.setText("Correct!")
                message.setTextColor(Color.GREEN)
                correct += 1
                if (correct % 5 == 0){
                    (timer as CountDownTimer).cancel()
                    update_timer(i)
                }
                res = main()
            } else {
                message.setText("Incorrect!")
                message.setTextColor(Color.RED)
                incorrect += 1
                res = main()
            }
        }

    }

    // function to update timer after user submits 5 correct answers
    fun update_timer (i: Intent) {
        var num = 10000
        sec = num + new_sec
        counter += 10
        timer = object: CountDownTimer(sec.toLong(), 1000) {
            val time: TextView = findViewById<TextView>(R.id.timer)
            override fun onTick(millisUntilFinished: Long) {
                time.setText((millisUntilFinished / 1000).toString())
                counter--
                new_sec = millisUntilFinished.toInt() - 1000
            }

            override fun onFinish() {
                var correct_send = correct.toString()
                var incorrect_send = incorrect.toString()
                i.putExtra("correct", correct_send)
                i.putExtra("incorrect", incorrect_send)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            }
        }
        (timer as CountDownTimer).start()
        if (counter < 0 || sec < 0){
            (timer as CountDownTimer).cancel()
        }
    }

    // main function that is responsible for filling the textfields with generated expressions, also checks these expressions
    fun main(): MutableList<Double> {
        var exp1 = findViewById<TextView>(R.id.exp1)
        var exp2 = findViewById<TextView>(R.id.exp2)
        var text_view = listOf<TextView>(exp1, exp2)

        var res = mutableListOf<Double>()

        for(i in 0..1){
            var result = 0.0
            do {
                generator()
                result = calculate()
            } while (result > 100 || result % 1 != 0.0)
            printExp(text_view[i], numbers, gen_symbols)
            res.add(result)
            length = 0
            numbers = mutableListOf<Int>()
            gen_symbols = mutableListOf<String>()
        }
        return res
    }

    // function that generates length of the expression (1,2,3 or 4 terms)
    fun generateLength (): Int {
        var gener: Random = Random()
        length = 1 + gener.nextInt(4)
        return length
    }

    // function that generates numbers depending on the generated length
    fun generateNumbers (): MutableList<Int> {
        numbers = mutableListOf<Int>()
        for(i in 1..length) {
            var gen: Random = Random()
            var number = 1 + gen.nextInt(20)
            numbers.add(number)
        }
        return numbers
    }

    // function that generates symbols for the expression
    fun generateSymbols (): MutableList<String> {
        gen_symbols = mutableListOf<String>()
        for(i in 1..length-1){
            var gen: Random = Random()
            var num = gen.nextInt(4)
            gen_symbols.add(symbols[num])
        }
        return gen_symbols
    }

    // function that is responsible for printing the expression with brackets and generated numbers and symbols
    fun printExp (exp: TextView, numbers: List<Int>, symbols: List<String>) {
        var exprecion = ""
        if(numbers.size == 4)
            exprecion = "((" + numbers[0] + symbols[0] + numbers[1] + ")" + symbols[1] + numbers[2] + ")" + symbols[2] + numbers[3]
        else if(numbers.size == 3)
            exprecion = "(" + numbers[0] + symbols[0] + numbers[1] + ")" + symbols[1] + numbers[2]
        else if(numbers.size == 2)
            exprecion = "" + numbers[0] + symbols[0] + numbers[1]
        else if(numbers.size == 1)
            exprecion = "" + numbers[0]

        exp.setText(exprecion)
    }

    // function that implements calculation depending on generated symbol
    fun calculate (): Double {
        var result = numbers[0].toDouble()
        for(i in 1..length-1) {
            if(gen_symbols[i-1] == "+")
                result = result+numbers[i]
            else if(gen_symbols[i-1] == "-")
                result = result-numbers[i]
            else if(gen_symbols[i-1] == "*")
                result = result*numbers[i]
            else if(gen_symbols[i-1] == "/")
                result = result/numbers[i]
        }
        return result
    }

    // function that calls other functions and assigns generated values to a variables
    fun generator () {
        length = generateLength()
        numbers = generateNumbers()
        gen_symbols = generateSymbols()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("correcr", correct)
        outState.putInt("incorrecr", incorrect)
        outState.putInt("length", length)
        outState.putInt("counter", counter)
        outState.putInt("sec", sec)
        outState.putInt("new_sec", new_sec)
        outState.putString("gen_symbols", gen_symbols.toString())
        outState.putString("numbers", numbers.toString())
        //outState.putString("numbers", numbers.toString())
//        var numbers = mutableListOf<Int>()
//        var gen_symbols = mutableListOf<String>()
//        var symbols = listOf<String>("+", "-", "*", "/")
    }
}