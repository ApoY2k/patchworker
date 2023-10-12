package apoy2k.patchworker.training

import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.summary.printSummary
import org.jetbrains.kotlinx.dl.dataset.OnHeapDataset

val train = listOf(
    listOf(1, 2, 3),
    listOf(0, 3, 3),
    listOf(-5, 2, -3),
    listOf(5, 2, 7),
    listOf(2, 7, 9),
    listOf(-3, -2, -5),
    listOf(-9, 2, -7),
    listOf(5, 1, 6),
    listOf(2, 2, 4),
    listOf(-3, 6, 3),
)

val test = listOf(
    listOf(2, 2, 4),
    listOf(0, 1, 1),
    listOf(-2, 5, 3),
    listOf(5, -2, 3),
    listOf(2, 0, 2),
    listOf(-6, 0, -6),
    listOf(0, 0, 0),
    listOf(8, 1, 9),
    listOf(3, 5, 8),
    listOf(3, -6, -3),
)

private fun featureExtract(path: String): Array<FloatArray> {
    val source = when (path) {
        "train" -> train
        else -> test
    }
    return source.map { row ->
        FloatArray(row.size - 1) { idx -> row[idx].toFloat() }
    }.toTypedArray()
}

private fun labelExtract(path: String): FloatArray {
    val source = when (path) {
        "train" -> train
        else -> test
    }

    return FloatArray(source.size) { idx -> source[idx][2].toFloat() }
}

fun main() {

    val (trainSet, testSet) = OnHeapDataset.createTrainAndTestDatasets(
        trainFeaturesPath = "train",
        trainLabelsPath = "train",
        testFeaturesPath = "test",
        testLabelsPath = "test",
        numClasses = 10,
        featuresExtractor = { x -> featureExtract(x) },
        labelExtractor = { x, _ -> labelExtract(x) }
    )

    Sequential.of(
        Input(2),
        Dense(1)
    ).use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.MSE,
            metric = Metrics.MAE
        )

        it.printSummary()

        it.fit(
            dataset = trainSet
        )

        val accuracy = it.evaluate(
            dataset = testSet
        )
            .metrics[Metrics.MAE]

        println("Accuracy: $accuracy")

        val pred = it.predictSoftly(listOf(1f, 2f).toFloatArray())

        println("Prediction for (1, 2): ${pred.toList()}")
    }
}
