package apoy2k.patchworker.training

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.jetbrains.kotlinx.dl.dataset.DataBatch
import org.jetbrains.kotlinx.dl.dataset.Dataset

class GameStateDataset(path: String) : Dataset() {

    private val reader: CsvReader = csvReader {
        skipEmptyLine = true
    }

    init {
        reader.open(path) {

        }
    }

    override fun createDataBatch(batchStart: Int, batchLength: Int): DataBatch {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}
