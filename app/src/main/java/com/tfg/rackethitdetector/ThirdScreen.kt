package com.tfg.rackethitdetector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tfg.rackethitdetector.data.DbHelper
import com.tfg.rackethitdetector.data.SensorsData
import com.tfg.rackethitdetector.databinding.ActivityThirdScreenBinding

class ThirdScreen : AppCompatActivity() {
    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var sensorsDBHelper: DbHelper
    private var rightHand: Boolean = true
    private var numberOfServes: Int = 0
    private var numberOfDrives: Int = 0
    private var numberOfBackHands: Int = 0
    private var sensorsDataList: ArrayList<SensorsData>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.buttonsAction()
        this.getSensorsData()
        this.setTextViews()
    }


    private fun buttonsAction() {
        val bundle = intent.extras
        rightHand = bundle!!.getBoolean("RightHand")

        val btnViewBackToSession: Button = findViewById(R.id.btnViewBackToSession)
        val btnViewQuitApp: Button = findViewById(R.id.btnViewQuitApp)

        btnViewBackToSession.text = "Continuar con la sesión"
        btnViewQuitApp.text = "Salir de la aplicación"

        btnViewBackToSession.setOnClickListener {
            val intent = Intent(this, SecondScreen::class.java)
            intent.putExtra("RightHand", rightHand)
            startActivity(intent)
            finish()
        }

        btnViewQuitApp.setOnClickListener {
            finishAndRemoveTask()
        }
    }


    private fun setTextViews() {
        val textResults: TextView = findViewById(R.id.textViewResults)
        val textServes: TextView = findViewById(R.id.textViewServes)
        val textDrives: TextView = findViewById(R.id.textViewDrives)
        val textBackHands: TextView = findViewById(R.id.textViewBackHands)
        val txtViewServes: TextView = findViewById(R.id.textViewNumberOfServes)
        val txtViewDrives: TextView = findViewById(R.id.textViewNumberOfDrives)
        val txtViewBackHands: TextView = findViewById(R.id.textViewNumberOfBackHands)

        textResults.text = "Resultados"
        textServes.text = "Saques"
        textDrives.text = "Derechas"
        textBackHands.text = "Revés"
        txtViewServes.text = numberOfServes.toString()
        txtViewDrives.text = numberOfDrives.toString()
        txtViewBackHands.text = numberOfBackHands.toString()
    }


    private fun getSensorsData() {
        sensorsDBHelper = DbHelper(this)

        sensorsDataList = sensorsDBHelper.readSensorsData(rightHand)

        if(sensorsDataList!!.size > 10) {
            for(i in 5 until (sensorsDataList!!.size - 5)) {

                if(checkBackhand(i)) {
                    numberOfBackHands++
                } else if(checkDrive(i)) {
                    numberOfDrives++
                } else if(checkServe(i)) {
                    numberOfServes++
                }
            }
        }
    }


    private fun checkServe(position: Int): Boolean {
        var isServe = false
        val firstPosition = position - 5
        val lastPosition = position + 5

        if(sensorsDataList!![position].getGyrX() < -4
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position - 1].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position + 1].getGyrX()) {
            var i = firstPosition
            isServe = true
            var isValidGyrX = false
            var isValidGyrXMin = false
            var isValidGyrXMax = false
            var isValidGyrY = false
            var isValidGyrYMin = false
            var isValidGyrYMax = false
            var isValidGyrZ = false
            var isValidAccX = false
            var isValidAccXMin = false
            var isValidAccXMax = false
            var isValidAccY = false
            var isValidAccYMin = false
            var isValidAccYMax = false
            var isValidAccZ = false

            do {
                if(sensorsDataList!![i].getGyrX() < 10 && sensorsDataList!![i].getGyrX() > -18) {
                    if(!isValidGyrX && !isValidGyrXMin) {
                        isValidGyrXMin = checkMinGyrX(i, -4)
                    }

                    if(!isValidGyrX && !isValidGyrXMax) {
                        isValidGyrXMax = checkMaxGyrX(i, 3)
                    }

                    isValidGyrX = isValidGyrXMin && isValidGyrXMax
                } else {
                    isServe = false
                }

                if(isServe) {
                    if(sensorsDataList!![position].getGyrY() < 12 && sensorsDataList!![position].getGyrY() > -22) {
                        if(!isValidGyrY && !isValidGyrYMin) {
                            isValidGyrYMin = checkMinGyrY(i, -1)
                        }

                        if(!isValidGyrY && !isValidGyrYMax) {
                            isValidGyrYMax = checkMaxGyrY(i, 2)
                        }

                        isValidGyrY = isValidGyrYMin && isValidGyrYMax
                    } else {
                        isServe = false
                    }

                    if(isServe && sensorsDataList!![position].getGyrZ() < 14 && sensorsDataList!![position].getGyrZ() > -17) {
                        if(!isValidGyrZ) {
                            isValidGyrZ = checkMinGyrZ(i, -3)
                        }
                    } else {
                        isServe = false
                    }

                    if(isServe && sensorsDataList!![position].getAccX() < 41 && sensorsDataList!![position].getAccX() > -32) {
                        if(!isValidAccX && !isValidAccXMin) {
                            isValidAccXMin = checkMinAccX(i, -12)
                        }

                        if(!isValidAccX && !isValidAccXMax) {
                            isValidAccXMax = checkMaxAccX(i, 5)
                        }

                        isValidAccX = isValidAccXMin && isValidAccXMax
                    } else {
                        isServe = false
                    }

                    if(isServe && sensorsDataList!![position].getAccY() < 18 && sensorsDataList!![position].getAccY() > -41) {
                        if(!isValidAccY && !isValidAccYMin) {
                            isValidAccYMin = checkMinAccY(i, -35)
                        }

                        if(!isValidAccY && !isValidAccYMax) {
                            isValidAccYMax = checkMaxAccY(i, 7)
                        }

                        isValidAccY = isValidAccYMin && isValidAccYMax
                    } else {
                        isServe = false
                    }

                    if(isServe && sensorsDataList!![position].getAccZ() < 23 && sensorsDataList!![position].getAccZ() > -41) {
                        if(!isValidAccZ) {
                            isValidAccZ = checkMinAccZ(i, -3)
                        }
                    } else {
                        isServe = false
                    }
                }

                i++
            } while(isServe && i <= lastPosition)

            if(!isServe || !isValidGyrX || !isValidGyrY || !isValidGyrZ || !isValidAccX || !isValidAccY || !isValidAccZ) {
                isServe = false
            }
        }

        return isServe
    }


    private fun checkDrive(position: Int): Boolean {
        var isDrive = false
        val firstPosition = position - 5
        val lastPosition = position + 5

        if(sensorsDataList!![position].getGyrX() < -1
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position - 1].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position + 1].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position - 2].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position + 2].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position - 3].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position + 3].getGyrX()) {
            var i = firstPosition
            isDrive = true
            var isValidGyrX = false
            var isValidGyrXMin = false
            var isValidGyrXMax = false
            var isValidGyrY = false
            var isValidGyrYMin = false
            var isValidGyrYMax = false
            var isValidGyrZ = false
            var isValidGyrZMin = false
            var isValidGyrZMax = false
            var isValidAccX = false
            var isValidAccXMin = false
            var isValidAccXMax = false
            var isValidAccY = false
            var isValidAccYMin = false
            var isValidAccYMax = false
            var isValidAccZ = false
            var maxAccX = 0f
            var minAccX = 0f

            do {
                if(sensorsDataList!![i].getGyrX() < 7 && sensorsDataList!![i].getGyrX() > -13) {
                    if(!isValidGyrX && !isValidGyrXMin) {
                        isValidGyrXMin = checkMinGyrX(i, -1)
                    }

                    if(!isValidGyrX && !isValidGyrXMax) {
                        isValidGyrXMax = checkMaxGyrX(i, 2)
                    }

                    isValidGyrX = isValidGyrXMin && isValidGyrXMax
                } else {
                    isDrive = false
                }

                if(isDrive) {
                    if(sensorsDataList!![position].getGyrY() < 20 && sensorsDataList!![position].getGyrY() > -22) {
                        if(!isValidGyrY && !isValidGyrYMin) {
                            isValidGyrYMin = checkMinGyrY(i, -5)
                        }

                        if(!isValidGyrY && !isValidGyrYMax) {
                            isValidGyrYMax = checkMaxGyrY(i, 5)
                        }

                        isValidGyrY = isValidGyrYMin && isValidGyrYMax
                    } else {
                        isDrive = false
                    }

                    if(isDrive && sensorsDataList!![position].getGyrZ() < 22 && sensorsDataList!![position].getGyrZ() > -21) {
                        if(!isValidGyrZ && !isValidGyrZMin) {
                            isValidGyrZMin = checkMinGyrZ(i, -5) //-4
                        }

                        if(!isValidGyrZ && !isValidGyrZMax) {
                            isValidGyrZMax = checkMaxGyrZ(i, 2)
                        }

                        isValidGyrZ = isValidGyrZMin && isValidGyrZMax
                    } else {
                        isDrive = false
                    }

                    if(isDrive && sensorsDataList!![position].getAccX() < 16 && sensorsDataList!![position].getAccX() > -41) {
                        if(!isValidAccX && !isValidAccXMin) {
                            isValidAccXMin = checkMinAccX(i, -13) //-10
                        }

                        if(!isValidAccX && !isValidAccXMax) {
                            isValidAccXMax = checkMaxAccX(i, 4) //5
                        }

                        if(maxAccX < sensorsDataList!![i].getAccX()) {
                            maxAccX = sensorsDataList!![i].getAccX()
                        }

                        if(minAccX > sensorsDataList!![i].getAccX()) {
                            minAccX = sensorsDataList!![i].getAccX()
                        }

                        isValidAccX = isValidAccXMin && isValidAccXMax
                    } else {
                        isDrive = false
                    }

                    if(isDrive && sensorsDataList!![position].getAccY() < 12 && sensorsDataList!![position].getAccY() > -41) {
                        if(!isValidAccY && !isValidAccYMin) {
                            isValidAccYMin = checkMinAccY(i, -31) //-10
                        }

                        if (!isValidAccY && !isValidAccYMax) {
                            isValidAccYMax = checkMaxAccY(i, 4) //5
                        }

                        isValidAccY = isValidAccYMin && isValidAccYMax
                    } else {
                        isDrive = false
                    }

                    if(isDrive && sensorsDataList!![position].getAccZ() < 19 && sensorsDataList!![position].getAccZ() > -41) {
                        if(!isValidAccZ) {
                            isValidAccZ = checkMinAccZ(i, -5)
                        }
                    } else {
                        isDrive = false
                    }
                }

                i++
            } while(isDrive && i <= lastPosition)

            if((minAccX * (-1) < (1.75 * maxAccX)) ) {
                isDrive = false
            }

            if(position > 485 && position < 495) {
                println("VALOR isServe " + isDrive)
                println("VALOR isValidGyrX " + isValidGyrX)
                println("VALOR isValidGyrY " + isValidGyrY)
                println("VALOR isValidGyrZ " + isValidGyrZ)
                println("VALOR isValidAccX " + isValidAccX)
                println("VALOR isValidAccY " + isValidAccY)
                println("VALOR isValidAccZ " + isValidAccZ)
            }

            if(!isDrive || !isValidGyrX || !isValidGyrY || !isValidGyrZ || !isValidAccX || !isValidAccY || !isValidAccZ) {
                isDrive = false
            }
        }

        return isDrive
    }


    private fun checkBackhand(position: Int): Boolean {
        var isBackhand = false
        val firstPosition = position - 5
        val lastPosition = position + 5

        if(sensorsDataList!![position].getGyrX() > 9
            && sensorsDataList!![position].getGyrX() > sensorsDataList!![position - 1].getGyrX()
            && sensorsDataList!![position].getGyrX() > sensorsDataList!![position + 1].getGyrX()) {
            var i = firstPosition
            isBackhand = true
            var isValidGyrY = false
            var isValidGyrYMin = false
            var isValidGyrYMax = false
            var isValidGyrZ = false
            var isValidGyrZMin = false
            var isValidGyrZMax = false
            var isValidAccX = false
            var isValidAccY = false
            var isValidAccZ = false

            do {
                if(sensorsDataList!![i].getGyrX() > 17 || sensorsDataList!![i].getGyrX() < -9) {
                    isBackhand = false
                }

                if(isBackhand) {
                    if(sensorsDataList!![position].getGyrY() < 7 && sensorsDataList!![position].getGyrY() > -9) {
                        if(!isValidGyrY && !isValidGyrYMin) {
                            isValidGyrYMin = checkMinGyrY(i, -1)
                        }

                        if (!isValidGyrY && !isValidGyrYMax) {
                            isValidGyrYMax = checkMaxGyrY(i, 2)
                        }

                        isValidGyrY = isValidGyrYMin && isValidGyrYMax
                    } else {
                        isBackhand = false
                    }

                    if(isBackhand && sensorsDataList!![position].getGyrZ() < 10 && sensorsDataList!![position].getGyrZ() > -7) {
                        if(!isValidGyrZ && !isValidGyrZMin) {
                            isValidGyrZMin = checkMinGyrZ(i, -1)
                        }

                        if(!isValidGyrZ && !isValidGyrZMax) {
                            isValidGyrZMax = checkMaxGyrZ(i, 3)
                        }

                        isValidGyrZ = isValidGyrZMin && isValidGyrZMax
                    } else {
                        isBackhand = false
                    }

                    if(isBackhand && sensorsDataList!![position].getAccX() < 0 && sensorsDataList!![position].getAccX() > -28) {
                        if(!isValidAccX) {
                            isValidAccX = checkMinAccX(i, -11)
                        }
                    } else {
                        isBackhand = false
                    }

                    if(isBackhand && sensorsDataList!![position].getAccY() < 18 && sensorsDataList!![position].getAccY() > -41) {
                        if(!isValidAccY) {
                            isValidAccY = checkMinAccY(i, -18)
                        }
                    } else {
                        isBackhand = false
                    }

                    if(isBackhand && sensorsDataList!![position].getAccZ() < 12 && sensorsDataList!![position].getAccZ() > -39) {
                        if(!isValidAccZ) {
                            isValidAccZ = checkMinAccZ(i, -10)
                        }
                    } else {
                        isBackhand = false
                    }
                }

                i++
            } while(isBackhand && i <= lastPosition)

            if(!isBackhand || !isValidGyrY || !isValidGyrZ || !isValidAccX || !isValidAccY || !isValidAccZ) {
                isBackhand = false
            }
        }

        return isBackhand
    }


    private fun checkMaxGyrX(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrX() > limit
            && sensorsDataList!![position].getGyrX() > sensorsDataList!![position - 1].getGyrX()
            && sensorsDataList!![position].getGyrX() > sensorsDataList!![position + 1].getGyrX()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinGyrX(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrX() < limit
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position - 1].getGyrX()
            && sensorsDataList!![position].getGyrX() < sensorsDataList!![position + 1].getGyrX()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMaxGyrY(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrY() > limit
            && sensorsDataList!![position].getGyrY() > sensorsDataList!![position - 1].getGyrY()
            && sensorsDataList!![position].getGyrY() > sensorsDataList!![position + 1].getGyrY()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinGyrY(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrY() < limit
            && sensorsDataList!![position].getGyrY() < sensorsDataList!![position - 1].getGyrY()
            && sensorsDataList!![position].getGyrY() < sensorsDataList!![position + 1].getGyrY()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMaxGyrZ(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrZ() > limit
            && sensorsDataList!![position].getGyrZ() > sensorsDataList!![position - 1].getGyrZ()
            && sensorsDataList!![position].getGyrZ() > sensorsDataList!![position + 1].getGyrZ()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinGyrZ(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getGyrZ() < limit
            && sensorsDataList!![position].getGyrZ() < sensorsDataList!![position - 1].getGyrZ()
            && sensorsDataList!![position].getGyrZ() < sensorsDataList!![position + 1].getGyrZ()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMaxAccX(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccX() > limit
            && sensorsDataList!![position].getAccX() > sensorsDataList!![position - 1].getAccX()
            && sensorsDataList!![position].getAccX() > sensorsDataList!![position + 1].getAccX()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinAccX(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccX() < limit
            && sensorsDataList!![position].getAccX() < sensorsDataList!![position - 1].getAccX()
            && sensorsDataList!![position].getAccX() < sensorsDataList!![position + 1].getAccX()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMaxAccY(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccY() > limit
            && sensorsDataList!![position].getAccY() > sensorsDataList!![position - 1].getAccY()
            && sensorsDataList!![position].getAccY() > sensorsDataList!![position + 1].getAccY()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinAccY(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccY() < limit
            && sensorsDataList!![position].getAccY() < sensorsDataList!![position - 1].getAccY()
            && sensorsDataList!![position].getAccY() < sensorsDataList!![position + 1].getAccY()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMaxAccZ(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccZ() > limit
            && sensorsDataList!![position].getAccZ() > sensorsDataList!![position - 1].getAccZ()
            && sensorsDataList!![position].getAccZ() > sensorsDataList!![position + 1].getAccZ()) {
            isValid = true
        }

        return isValid
    }


    private fun checkMinAccZ(position: Int, limit: Int): Boolean {
        var isValid = false

        if(sensorsDataList!![position].getAccZ() < limit
            && sensorsDataList!![position].getAccZ() < sensorsDataList!![position - 1].getAccZ()
            && sensorsDataList!![position].getAccZ() < sensorsDataList!![position + 1].getAccZ()) {
            isValid = true
        }

        return isValid
    }
}