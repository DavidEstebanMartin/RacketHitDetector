package com.tfg.rackethitdetector.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context): SQLiteOpenHelper(context, "sensors.db", null, 1) {
    private val tableSensors: String = "t_sensors"
    private val acceleX: String = "accele_X"
    private val acceleY: String = "accele_Y"
    private val acceleZ: String = "accele_Z"
    private val gyroX: String = "gyro_X"
    private val gyroY: String = "gyro_Y"
    private val gyroZ: String = "gyro_Z"
    private val rightHand: String = "right_Hand"


    override fun onCreate(db: SQLiteDatabase?) {
        val createExecution = "CREATE TABLE " + tableSensors +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                acceleX + " FLOAT NOT NULL," +
                acceleY + " FLOAT NOT NULL," +
                acceleZ + " FLOAT NOT NULL," +
                gyroX + " FLOAT NOT NULL," +
                gyroY + " FLOAT NOT NULL," +
                gyroZ + " FLOAT NOT NULL," +
                rightHand + " BOOLEAN NOT NULL)"

        db!!.execSQL(createExecution)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropExecution = "DROP TABLE IF EXISTS $tableSensors"

        db!!.execSQL(dropExecution)

        onCreate(db)
    }


    fun insertSensorsData(accele_X: Float, accele_Y: Float, accele_Z: Float, gyro_X: Float, gyro_Y: Float, gyro_Z: Float, right_Hand: Boolean) {
        val values = ContentValues()
        values.put(acceleX, accele_X)
        values.put(acceleY, accele_Y)
        values.put(acceleZ, accele_Z)
        values.put(gyroX, gyro_X)
        values.put(gyroY, gyro_Y)
        values.put(gyroZ, gyro_Z)
        values.put(rightHand, right_Hand)

        val db = this.writableDatabase
        db.insert(tableSensors, null, values)

        db.close()
    }


    fun deleteAllSensorsData() {
        val db = this.writableDatabase

        db.execSQL("DELETE FROM $tableSensors")

        db.close()
    }


    fun readSensorsData(right_Hand: Boolean): ArrayList<SensorsData>? {
        val sensorsDataList: ArrayList<SensorsData> = ArrayList()
        var sensorsData: SensorsData? = null
        var rightHandValue: Int = 0;

        if(right_Hand) {
            rightHandValue = 1;
        }

        val db = this.readableDatabase

        var cursorSensorsData = db.rawQuery(
            "SELECT * FROM $tableSensors WHERE $rightHand = $rightHandValue ORDER BY Id ASC",
            null
        )

        if(cursorSensorsData.moveToFirst()) {
            do {
                sensorsData = SensorsData()
                sensorsData.setId(cursorSensorsData.getInt(0))
                sensorsData.setAccX(cursorSensorsData.getFloat(1))
                sensorsData.setAccY(cursorSensorsData.getFloat(2))
                sensorsData.setAccZ(cursorSensorsData.getFloat(3))
                sensorsData.setGyrX(cursorSensorsData.getFloat(4))
                sensorsData.setGyrY(cursorSensorsData.getFloat(5))
                sensorsData.setGyrZ(cursorSensorsData.getFloat(6))

                sensorsDataList.add(sensorsData)
            } while(cursorSensorsData.moveToNext())
        }

        cursorSensorsData.close()

        return sensorsDataList
    }
}