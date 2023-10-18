package apoy2k.patchworker.training

import apoy2k.patchworker.datasource
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

    val (trainSet, testSet) = generateDatasets(datasource)

    Sequential.of(
        Input(208),
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

        log.info { "Starting training" }

        it.fit(
            dataset = trainSet,
            epochs = 5,
            batchSize = 10_000
        )

        val accuracy = it.evaluate(
            dataset = testSet
        )
            .metrics[Metrics.MAE]

        log.info { "Accuracy: $accuracy" }

        val file = File("model/0.1.0")
        it.save(file, writingMode = WritingMode.OVERRIDE)

        log.info { "Saved model in $file" }
    }
}
