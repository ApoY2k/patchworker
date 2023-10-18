package apoy2k.patchworker.training

import org.jetbrains.kotlinx.dl.dataset.DataBatch
import org.jetbrains.kotlinx.dl.dataset.Dataset
import java.math.BigInteger
import javax.sql.DataSource

class GameStateDataset(
    private val db: DataSource,
    private val filterQuery: String
) : Dataset() {

    override fun createDataBatch(batchStart: Int, batchLength: Int): DataBatch {
        val query = "select * from data where $filterQuery order by id limit $batchLength offset $batchStart"
        val features = mutableListOf<FloatArray>()
        val labels = mutableListOf<Float>()
        db.connection.use { connection ->
            connection.prepareStatement(query).use { statement ->
                statement.executeQuery().use {
                    while (it.next()) {
                        val rowData = mutableListOf<Float>()
                        rowData.addAll(it.getString(2).decodeBinaryChecksum(35))
                        rowData.add(it.getFloat(3))
                        rowData.add(it.getFloat(4))
                        rowData.add(it.getFloat(5))
                        rowData.add(it.getFloat(6))
                        rowData.add(it.getFloat(7))
                        rowData.addAll(it.getString(8).decodeBinaryChecksum(81))
                        rowData.add(it.getFloat(9))
                        rowData.add(it.getFloat(10))
                        rowData.add(it.getFloat(11))
                        rowData.add(it.getFloat(12))
                        rowData.add(it.getFloat(13))
                        rowData.addAll(it.getString(14).decodeBinaryChecksum(81))
                        rowData.add(
                            when (it.getBoolean(15)) {
                                true -> 1
                                else -> 0
                            }.toFloat()
                        )

                        features.add(rowData.toFloatArray())
                        labels.add(it.getInt(16).toFloat())
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
        val query = "select count(1) from data where $filterQuery"
        return db.connection.use { connection ->
            connection.prepareStatement(query).use { statement ->
                statement.executeQuery().use {
                    it.next()
                    it.getInt(1)
                }
            }
        }
    }
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

fun generateDatasets(db: DataSource) =
    Pair(GameStateDataset(db, " mod(id, 2) = 0 "), GameStateDataset(db, " mod(id, 2) <> 0 "))
