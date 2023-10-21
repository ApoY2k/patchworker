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

    override fun score(game: Game): Double = model.use {
        it.predictSoftly(game.toModelInput())[0].toDouble()
    }

    private fun Game.toModelInput(): FloatArray {
        val input = mutableListOf<Float>()
        val rowData = mutableListOf<Float>()

        rowData.addAll(this.patchesChecksum().decodeBinaryChecksum(35))
        rowData.add(this.player1.trackerPosition.toFloat())
        rowData.add(this.player1.buttonMultiplier.toFloat())
        rowData.add(this.player1.buttons.toFloat())
        rowData.add(this.player1.specialPatches.toFloat())
        rowData.add(this.player1.actionsTaken.toFloat())
        rowData.addAll(this.player1.board.checksum().decodeBinaryChecksum(81))
        rowData.add(this.player2.trackerPosition.toFloat())
        rowData.add(this.player2.buttonMultiplier.toFloat())
        rowData.add(this.player2.buttons.toFloat())
        rowData.add(this.player2.specialPatches.toFloat())
        rowData.add(this.player2.actionsTaken.toFloat())
        rowData.addAll(this.player2.board.checksum().decodeBinaryChecksum(81))
        rowData.add(
            when (this.isPlayer1Turn()) {
                true -> 1
                else -> 0
            }.toFloat()
        )

        return input.toFloatArray()
    }
}
