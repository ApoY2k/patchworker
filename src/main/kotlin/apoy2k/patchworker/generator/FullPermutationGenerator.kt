package apoy2k.patchworker.generator

import apoy2k.patchworker.game.Game
import kotlinx.coroutines.*
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

val trainingFile = Path("data/training.csv")
val testFile = Path("data/test.csv")
val trainingWriter = trainingFile.bufferedWriter(Charsets.UTF_8, DEFAULT_BUFFER_SIZE, StandardOpenOption.CREATE)
val testWriter = testFile.bufferedWriter(Charsets.UTF_8, DEFAULT_BUFFER_SIZE, StandardOpenOption.CREATE)

fun main() {
    val trainingResults = ConcurrentHashMap<String, Int>()
    val testResults = ConcurrentHashMap<String, Int>()

    val trainingOutput = Pair(trainingWriter, trainingResults)
    val testOutput = Pair(testWriter, testResults)

    val outputPool = setOf(trainingOutput, testOutput)

    val parallelism = Runtime.getRuntime().availableProcessors()
    val gameSimDispatcher = Executors.newFixedThreadPool(parallelism).asCoroutineDispatcher()

    runBlocking {
        launch(Dispatchers.IO) {
            while (isActive) {
                delay(100)
                print("\r${trainingResults.size} training states and ${testResults.size} test states collected        ")
            }
        }

        repeat(parallelism) {
            launch(gameSimDispatcher) {
                val output = outputPool.random()
                runGame(output.second, Game(), randomDepth(), 0, output.first)
            }
        }
    }
}

val depthWeights = (1..50).associateWith { (51 - it) }
val weightSum = depthWeights.values.sum()

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
