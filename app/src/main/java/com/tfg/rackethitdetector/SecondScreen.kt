package com.tfg.rackethitdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tfg.rackethitdetector.data.DbHelper
import com.tfg.rackethitdetector.databinding.ActivitySecondScreenBinding

class SecondScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding
    private lateinit var sensorsDBHelper: DbHelper
    private var readSensorData: Boolean = false
    private var read: Boolean = false
    private var accelerometerReady: Boolean = false
    private var gyroscopeReady: Boolean = false
    private var handler: Handler = Handler()
    private var runnable: Runnable? = null
    private var delay = 250
    private var accX = 0f
    private var accY = 0f
    private var accZ = 0f
    private var gyrX = 0f
    private var gyrY = 0f
    private var gyrZ = 0f
    private var rightHand: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorsDBHelper = DbHelper(this)

        this.buttonsAction()
        this.sensorsData()
    }


    @SuppressLint("SetTextI18n")
    private fun buttonsAction() {
        val bundle = intent.extras
        rightHand = bundle!!.getBoolean("RightHand")

        val txtViewRecording: TextView = findViewById(R.id.textViewRecording)
        val btnInitSession: Button = findViewById(R.id.btnViewInitSession)
        val btnFinishSession: Button = findViewById(R.id.btnViewFinishSession)

        txtViewRecording.text = "GRABANDO LA SESIÓN"
        txtViewRecording.setTextColor(Color.WHITE)
        btnInitSession.text = "Inciciar sesión"
        btnFinishSession.text = "Finalizar sesión"

        btnInitSession.setOnClickListener {
            readSensorData = !readSensorData
            btnFinishSession.isEnabled = !readSensorData

            if(readSensorData) {
                btnInitSession.text = "Detener sesión"
                txtViewRecording.setBackgroundColor(Color.GREEN)
            } else {
                btnInitSession.text = "Iniciar sesión"
                txtViewRecording.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        btnFinishSession.setOnClickListener {
            val intent = Intent(this, ThirdScreen::class.java)
            intent.putExtra("RightHand", rightHand)
            startActivity(intent)
            finish()
        }
    }


    private fun sensorsData() {
        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscopeSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if(accelerometerSensor == null || gyroscopeSensor == null) {
            Toast.makeText(this, "Falta acelerómetro y/o giroscopio", Toast.LENGTH_LONG).show()
            finish()
        }

        val sensorEvent = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // method to check accuracy changed in sensor.
            }

            override fun onSensorChanged(event: SensorEvent) {
                if(readSensorData && read) {
                    if(event.sensor.type == Sensor.TYPE_ACCELEROMETER && !accelerometerReady) {
                        accX = event.values[0]
                        accY = event.values[1]
                        accZ = event.values[2]

                        accelerometerReady = true
                    } else if(event.sensor.type == Sensor.TYPE_GYROSCOPE && !gyroscopeReady) {
                        gyrX = event.values[0]
                        gyrY = event.values[1]
                        gyrZ = event.values[2]

                        gyroscopeReady = true
                    }

                    if(accelerometerReady && gyroscopeReady) {
                        insertSensorsData()
                    }
                }
            }
        }

        sensorManager.registerListener(
            sensorEvent,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            sensorEvent,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }


    private fun insertSensorsData() {
        sensorsDBHelper.insertSensorsData(accX, accY, accZ, gyrX, gyrY, gyrZ, rightHand)

        read = false
        accX = 0f
        accY = 0f
        accZ = 0f
        gyrX = 0f
        gyrY = 0f
        gyrZ = 0f
    }


    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            read = true
            accelerometerReady = false
            gyroscopeReady = false
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
}