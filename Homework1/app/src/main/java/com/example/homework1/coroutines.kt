package com.example.homework1

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(){
    val time = measureTimeMillis{
        runBlocking {
            printWeatherReport()
        }
    }
    println("Method took ${time/1000.0} seconds")
}

suspend fun printWeatherReport(){
    delay(1000);
    coroutineScope {
        //async{} starts a new coroutine but with return value from coroutine
        val temperature = async { getTemperature()}

        //launch{} starts a new coroutine but no return value expected from coroutine
        val weather = async {getWeather()}
        val temperatureResult = temperature.await()
        val weatherResult = weather.await()
        println("$weatherResult, $temperatureResult")
    }
}

suspend fun getTemperature() : String{
    delay(3000)
    return "20 degrees"
}

suspend fun getWeather() : String{
    delay(3000)
    return "Sunny"
}