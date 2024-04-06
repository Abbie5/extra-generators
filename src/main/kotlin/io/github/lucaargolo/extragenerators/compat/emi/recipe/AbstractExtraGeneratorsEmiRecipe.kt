package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.render.EmiTooltipComponents
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.compat.emi.ExtraGeneratorsCategory
import io.github.lucaargolo.extragenerators.utils.GeneratorFuel
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

abstract class AbstractExtraGeneratorsEmiRecipe(
    val generatorCategory: ExtraGeneratorsCategory,
    id: Identifier?,
    val fuel: GeneratorFuel
) : BasicEmiRecipe(
    generatorCategory,
    id,
    127,
    54
) {
    abstract val backgroundTextureId: Identifier

    private fun backgroundTexture() = EmiTexture(backgroundTextureId, 24, 16, 127, 54)
    private fun energyBarFilled() = EmiTexture(backgroundTextureId, 176, 0, 8, 52)

    override fun supportsRecipeTree() = false

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(backgroundTexture(), 0, 0)
        widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 57, 20, fuel.burnTime * 50, false, true, true)
            .tooltip(listOf(
                EmiTooltipComponents.of(Text.translatable("screen.extragenerators.rei.burn_time", fuel.burnTime)),
                EmiTooltipComponents.of(Text.translatable("screen.extragenerators.rei.burn_rate", MathHelper.floor(fuel.energyOutput/fuel.burnTime)))
            ))

        val energyHeight = (fuel.energyOutput * 52 / generatorCategory.block.generatorConfig.storage).toInt().coerceIn(1, 52)
        val energyV = 52 - energyHeight
        val energyY = 1 + energyV
        widgets.addTexture(backgroundTextureId, 1, energyY, 8, energyHeight, 176, energyV)
        widgets.addTooltip(listOf(EmiTooltipComponents.of(Text.literal("${fuel.energyOutput.toLong()} E"))), 1, 1, 8, 52)
    }
}