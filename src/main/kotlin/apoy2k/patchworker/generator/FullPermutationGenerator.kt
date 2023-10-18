package apoy2k.patchworker.generator

import apoy2k.patchworker.datasource
import apoy2k.patchworker.game.Game
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

val parallelism = Runtime.getRuntime().availableProcessors()

val gameSimDispatcher = Executors.newFixedThreadPool(parallelism).asCoroutineDispatcher()

fun main() {
    runBlocking {
        repeat(parallelism) {
            launch(gameSimDispatcher) {
                runGame(Game(), randomDepth(), 0, datasource)
            }
        }
        joinAll()
    }
}

private val depthWeights = (1..50).associateWith { (51 - it) }
private val weightSum = depthWeights.values.sum()

private fun randomDepth(): Int {
    val rnd = Math.random() * weightSum

    var iteratorWeight = 0
    depthWeights.forEach {
        iteratorWeight += it.value
        if (iteratorWeight >= rnd) {
            return it.key
        }
    }

    return 1
}
