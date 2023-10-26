package apoy2k.patchworker.training

import apoy2k.patchworker.datasource
import apoy2k.patchworker.game.asPatchIdOneHotEncodedFloatList
import org.jetbrains.kotlinx.dl.dataset.DataBatch
import org.jetbrains.kotlinx.dl.dataset.Dataset
import java.math.BigInteger

class GameStateDataset(
    private val table: String,
    private val filterQuery: String,
) : Dataset() {

    override fun createDataBatch(batchStart: Int, batchLength: Int): DataBatch {
        val query = "select * from $table where $filterQuery order by id limit $batchLength offset $batchStart"
        val features = mutableListOf<FloatArray>()
        val labels = mutableListOf<Float>()
        datasource.connection.use { connection ->
            connection.prepareStatement(query).use { statement ->
                statement.executeQuery().use { stmt ->
                    while (stmt.next()) {
                        val rowData = mutableListOf<Float>()
                        val patches = stmt.getString(4)
                            .split(",")
                            .flatMap { it.asPatchIdOneHotEncodedFloatList() }
                            .toMutableList()

                        // Pad out zeroed inputs for missing patches
                        while (patches.size < 1089) {
                            repeat(33) {
                                patches.add(0f)
                            }
                        }

                        rowData.addAll(patches)
                        rowData.add(stmt.getFloat(5))
                        rowData.add(stmt.getFloat(6))
                        rowData.add(stmt.getFloat(7))
                        rowData.add(stmt.getFloat(8))
                        rowData.add(stmt.getFloat(9))
                        rowData.addAll(stmt.getString(10).decodeBinaryChecksum(81))
                        rowData.add(stmt.getFloat(11))
                        rowData.add(stmt.getFloat(12))
                        rowData.add(stmt.getFloat(13))
                        rowData.add(stmt.getFloat(14))
                        rowData.add(stmt.getFloat(15))
                        rowData.addAll(stmt.getString(16).decodeBinaryChecksum(81))
                        rowData.add(
                            when (stmt.getBoolean(17)) {
                                true -> 1
                                else -> 0
                            }.toFloat()
                        )

                        features.add(rowData.toFloatArray())
                        labels.add(stmt.getFloat(18))
                    }
                }
            }
        }
        return DataBatch(features.toTypedArray(), labels.toFloatArray(), batchLength)
    }

    override fun getX(idx: Int): FloatArray {
        TODO("Not yet implemented")
    }

    override fun getY(idx: Int): Float {
        TODO("Not yet implemented")
    }

    override fun shuffle(): Dataset {
        TODO("Not yet implemented")
    }

    override fun split(splitRatio: Double): Pair<Dataset, Dataset> {
        TODO("Not yet implemented")
    }

    override fun xSize(): Int {
        val query = "select count(1) from $table where $filterQuery"
        return datasource.connection.use { connection ->
            connection.prepareStatement(query).use { statement ->
                statement.executeQuery().use {
                    it.next()
                    it.getInt(1)
                }
            }
        }
    }
}

fun String.decodeBinaryChecksum(length: Int) =
    BigInteger(this)
        .toString(2)
        .padStart(length, '0')
        .map {
            when (it) {
                '1' -> 1
                else -> 0
            }.toFloat()
        }

fun generateDatasets(table: String) =
    Pair(GameStateDataset(table, " mod(id, 2) = 0 "), GameStateDataset(table, " mod(id, 2) <> 0 "))
