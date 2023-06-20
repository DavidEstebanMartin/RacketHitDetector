package com.tfg.rackethitdetector.data

class SensorsData {
    private var id = 0
    private var accX = 0f
    private var accY = 0f
    private var accZ = 0f
    private var gyrX = 0f
    private var gyrY = 0f
    private var gyrZ = 0f


    fun getId(): Int {
        return id
    }


    fun setId(id: Int) {
        this.id = id
    }


    fun getAccX(): Float {
        return accX
    }


    fun setAccX(accX: Float) {
        this.accX = accX
    }


    fun getAccY(): Float {
        return accY
    }


    fun setAccY(accY: Float) {
        this.accY = accY
    }


    fun getAccZ(): Float {
        return accZ
    }


    fun setAccZ(accZ: Float) {
        this.accZ = accZ
    }


    fun getGyrX(): Float {
        return gyrX
    }


    fun setGyrX(gyrX: Float) {
        this.gyrX = gyrX
    }


    fun getGyrY(): Float {
        return gyrY
    }


    fun setGyrY(gyrY: Float) {
        this.gyrY = gyrY
    }


    fun getGyrZ(): Float {
        return gyrZ
    }


    fun setGyrZ(gyrZ: Float) {
        this.gyrZ = gyrZ
    }
}