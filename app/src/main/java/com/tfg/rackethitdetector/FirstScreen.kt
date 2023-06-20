package com.tfg.rackethitdetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.tfg.rackethitdetector.data.DbHelper
import com.tfg.rackethitdetector.databinding.ActivityFirstScreenBinding

class FirstScreen : AppCompatActivity() {
    private lateinit var binding: ActivityFirstScreenBinding
    private lateinit var sensorsDBHelper: DbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtViewQuestion: TextView = findViewById(R.id.textViewQuestion)
        val btnRightHand: Button = findViewById(R.id.btnViewRightHand)
        val btnLeftHand: Button = findViewById(R.id.btnViewLeftHand)

        txtViewQuestion.text = "¿En qué muñeca llevas el dispositivo?"
        btnRightHand.text = "Derecha >"
        btnLeftHand.text = "< Izquierda"

        btnRightHand.setOnClickListener {
            intiSensorsDataTable()

            val intent = Intent(this, SecondScreen::class.java)
            intent.putExtra("RightHand", true)
            startActivity(intent)
            finish()
        }

        btnLeftHand.setOnClickListener {
            intiSensorsDataTable()

            val intent = Intent(this, SecondScreen::class.java)
            intent.putExtra("RightHand", false)
            startActivity(intent)
            finish()
        }
    }


    private fun intiSensorsDataTable() {
        this.sensorsDBHelper = DbHelper(this)

        this.sensorsDBHelper.deleteAllSensorsData()
    }
}