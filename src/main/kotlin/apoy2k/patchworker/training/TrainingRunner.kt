package apoy2k.patchworker.training

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.WritingMode
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.summary.printSummary
import java.io.File

private val log = KotlinLogging.logger {}

fun main() {
    trainModel("data_depth_1")
//    trainModel("data_depth_2")
}

private fun trainModel(table: String) {
    val (trainSet, testSet) = generateDatasets(table)

    Sequential.of(
        Input(208),
        Dense(100),
        Dense(50),
        Dense(1)
    ).use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.MSE,
            metric = Metrics.MAE,
        )

        it.printSummary()

        val callback = ModelCallback(trainSet, testSet)

        it.fit(
            dataset = trainSet,
            epochs = 10,
            batchSize = 1_000,
            callbacks = listOf(callback)
        )

        val accuracy = it.evaluate(
            dataset = testSet,
            callbacks = listOf(callback)
        )
            .metrics[Metrics.MAE]

        log.info { "Accuracy: $accuracy" }

        val file = File("model/$table")
        it.save(file, writingMode = WritingMode.OVERRIDE)

        log.info { "Saved model in $file" }
    }
}
