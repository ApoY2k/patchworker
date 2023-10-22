package apoy2k.patchworker.scoring

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.checksum
import apoy2k.patchworker.training.decodeBinaryChecksum
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import java.io.File

class ModelScorer(
    table: String,
) : GameStateScorer {
    private val model: TensorFlowInferenceModel = TensorFlowInferenceModel.load(File("model/$table"))

    init {
        model.reshape(208)
    }

    override fun score(game: Game): Double = model.predictSoftly(game.toModelInput())[0].toDouble()

    private fun Game.toModelInput(): FloatArray {
        val input = mutableListOf<Float>()

        input.addAll(this.patchesChecksum().decodeBinaryChecksum(35))
        input.add(this.player1.trackerPosition.toFloat())
        input.add(this.player1.buttonMultiplier.toFloat())
        input.add(this.player1.buttons.toFloat())
        input.add(this.player1.specialPatches.toFloat())
        input.add(this.player1.actionsTaken.toFloat())
        input.addAll(this.player1.board.checksum().decodeBinaryChecksum(81))
        input.add(this.player2.trackerPosition.toFloat())
        input.add(this.player2.buttonMultiplier.toFloat())
        input.add(this.player2.buttons.toFloat())
        input.add(this.player2.specialPatches.toFloat())
        input.add(this.player2.actionsTaken.toFloat())
        input.addAll(this.player2.board.checksum().decodeBinaryChecksum(81))
        input.add(
            when (this.isPlayer1Turn()) {
                true -> 1
                else -> 0
            }.toFloat()
        )

        return input.toFloatArray()
    }
}
