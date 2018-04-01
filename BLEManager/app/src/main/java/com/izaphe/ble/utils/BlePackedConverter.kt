package com.izaphe.ble.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.floor

/**
 * Created by Kamil on 2018-03-29.
 */
object BlePackedConverter {

     fun interpretAsString(data: ByteArray): DoubleArray {
        val string = String(data).replace("(\r\n|\t|\n\r|\r)".toRegex(), "\n").split("\n")
        var values = DoubleArray(string.size)
        for (i in 0 until string.size) {
            try {
                values[i] = string[i].toDouble()
            } catch (e: NumberFormatException) {
                values[i] = 0.0
            }
        }
        return values
    }

     fun interpretAsInteger(data: ByteArray): DoubleArray {
        val size = data.size
        //  println("data: ByteArray"+Arrays.toString(data))
        var values = DoubleArray((floor((size / 4.0))).toInt())
        for (i in 0 until size step 4) {
            var sample = byteArrayOf(data[i], data[i + 1], data[i + 2], data[i + 3])
            //    println("sample: ByteArray"+Arrays.toString(sample))
            values[(i + 1) / 4] = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).getInt().toDouble()
            //     println("values: ByteArray"+Arrays.toString(values))
        }
        return values
    }

     fun interpretAsFloat(data: ByteArray): DoubleArray {
        val size = data.size
        var values = DoubleArray((floor((size / 4.0))).toInt())
        for (i in 0 until size step 4) {
            var sample = byteArrayOf(data[i], data[i + 1], data[i + 2], data[i + 3])
            values[(i + 1) / 4] = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).getFloat().toDouble()

        }
        return values
    }

}