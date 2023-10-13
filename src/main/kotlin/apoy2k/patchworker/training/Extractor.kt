package apoy2k.patchworker.training

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.math.BigInteger

private val reader = csvReader { skipEmptyLine = true }

fun extractFeaturesFromCsv(path: String): Array<FloatArray> {
    val result = mutableListOf<FloatArray>()
    reader.open(path) {
        readAllAsSequence().forEachIndexed { idx, row ->
            if (idx == 0) return@forEachIndexed

            val patchesInput = row[0].decodeBinaryChecksum(33)
            val features = mutableListOf<Float>()
            features.addAll(patchesInput)
            for (col in 1..5) {
                features.add(row[col].toFloat())
            }
            val board1 = row[6].decodeBinaryChecksum(81)
            features.addAll(board1)
            for (col in 7..11) {
                features.add(row[col].toFloat())
            }
            val board2 = row[12].decodeBinaryChecksum(81)
            features.addAll(board2)
            when (row[13]) {
                "true" -> features.add(1f)
                else -> features.add(0f)
            }
            result.add(features.toFloatArray())
        }
    }
    return result.toTypedArray()
}

fun extractLabelsFromCsv(path: String, y: Int): FloatArray {
    val result = mutableListOf<Float>()
    reader.open(path) {
        readAllAsSequence().forEachIndexed { idx, row ->
            if (idx == 0) return@forEachIndexed

            result.add(row[14].toFloat())
        }
    }
    return result.toFloatArray()
}

private fun String.decodeBinaryChecksum(length: Int) =
    BigInteger(this)
        .toString(2)
        .padStart(length, '0')
        .map {
            when (it) {
                '1' -> 1
                else -> 0
            }.toFloat()
        }
