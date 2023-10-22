package apoy2k.patchworker.training

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.kotlinx.dl.api.core.callback.Callback
import org.jetbrains.kotlinx.dl.api.core.history.BatchEvent
import org.jetbrains.kotlinx.dl.api.core.history.BatchTrainingEvent
import org.jetbrains.kotlinx.dl.api.core.history.History
import org.jetbrains.kotlinx.dl.api.core.history.TrainingHistory
import org.jetbrains.kotlinx.dl.dataset.Dataset

class ModelCallback(
    private val trainSet: Dataset,
    private val testSet: Dataset,
) : Callback() {
    private val log = KotlinLogging.logger {}

    override fun onTrainBegin() {
        log.info { "Starting training" }
    }

    override fun onTestBegin() {
        log.info { "Starting testing" }
    }

    override fun onTrainEnd(logs: TrainingHistory) {
        log.info { "Finished training" }
    }

    override fun onTestEnd(logs: History) {
        log.info { "Finished testing" }
    }

    override fun onTrainBatchEnd(batch: Int, batchSize: Int, event: BatchTrainingEvent, logs: TrainingHistory) {
        log.info { "Finished training batch (${batch * batchSize}/${trainSet.xSize()})" }
    }

    override fun onTestBatchEnd(batch: Int, batchSize: Int, event: BatchEvent?, logs: History) {
        log.info { "Finished testing batch (${batch * batchSize}/${testSet.xSize()})" }
    }
}
