package com.example.homework1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //1. Create an Android app with an empty activity and set the title to “Homework 1”.
        title = "Homework 1"
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //2. Add a static TextView containing your name.
        val myTextView : TextView = findViewById(R.id.myTextView)
        myTextView.text = "Matthias Fichtinger"

        val editTextOne : EditText = findViewById(R.id.editTextOne)
        val editTextTwo : EditText = findViewById(R.id.editTextTwo)

        val button : Button = findViewById(R.id.sumButton)
        val output : TextView = findViewById(R.id.outputTextView)

        button.setOnClickListener {
            val first = getIntegerOfEditText(editTextOne)
            val second = getIntegerOfEditText(editTextTwo)
            output.text = addNumbersTogether(first, second).toString()

            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO){
                    println("I am executing Thread ${Thread.currentThread().name}")
                }
                withContext(Dispatchers.Default){
                    println("I am executing Thread ${Thread.currentThread().name}")
                }
            }
        }

        val navigate : Button = findViewById(R.id.navigateButton)
        navigate.setOnClickListener{
            val first = getIntegerOfEditText(editTextOne)
            val second = getIntegerOfEditText(editTextTwo)
            val intent = Intent(this,SecondActivity::class.java).apply{
                putExtra("sum",addNumbersTogether(first, second).toString())
            }
            startActivity(intent)
        }

        val seekbar : SeekBar = findViewById(R.id.seekBar)
        val seekBarValue : TextView = findViewById(R.id.seekBarValue)

        seekBarValue.text = seekbar.progress.toString()

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update TextView with current progress
                seekBarValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this implementation
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this implementation
            }
        })

    }
    fun getIntegerOfEditText(editText: EditText) : Int{
        val input = editText.text.toString()
        try{
            return input.toInt()
        }catch (e: NumberFormatException){
            return 0
        }
    }

    fun addNumbersTogether(first : Int, second : Int) : Int{
        return first + second
    }
}