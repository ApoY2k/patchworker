package apoy2k.patchworker.training

import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.summary.printSummary
import org.jetbrains.kotlinx.dl.dataset.OnHeapDataset

fun main() {

    val (trainSet, testSet) = OnHeapDataset.createTrainAndTestDatasets(
        trainFeaturesPath = "data/generated_end_states.csv",
        trainLabelsPath = "data/generated_end_states.csv",
        testFeaturesPath = "data/generated_end_states.csv",
        testLabelsPath = "data/generated_end_states.csv",
        numClasses = 0,
        featuresExtractor = { extractFeaturesFromCsv(it) },
        labelExtractor = { path, y -> extractLabelsFromCsv(path, y) }
    )

    Sequential.of(
        Input(206),
        Dense(100),
        Dense(50),
        Dense(1)
    ).use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.MSE,
            metric = Metrics.MAE
        )

        it.printSummary()

        it.fit(
            dataset = trainSet,
            epochs = 100
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
