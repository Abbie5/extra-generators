package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.compat.emi.stack.FireEmiStack
import io.github.lucaargolo.extragenerators.mixin.FluidBlockAccessor
import net.minecraft.block.AbstractFireBlock
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class ThermoelectricGeneratorEmiRecipe(
    category: EmiRecipeCategory,
    block: Block,
    private val temperature: Int
) : BasicEmiRecipe(
    category, null, 80, 18
) {
    private val input: EmiIngredient = when (block) {
        is FluidBlock -> EmiStack.of((block as FluidBlockAccessor).fluid)
        is AbstractFireBlock -> FireEmiStack.of(block)
        else -> EmiStack.of(block)
    }

    init {
        inputs = listOf(input)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input, 0, 0)
            .recipeContext(this)

        val baseColor = Triple(1f, 1f, 1f)

        val coldColor = Triple(0f, 1f, 1f)
        val hotColor = Triple(1f, 0.4f, 0f)

        val finalColor = if(temperature < 0) coldColor else hotColor
        val delta = MathHelper.clamp(if(temperature < 0) MathHelper.abs(temperature)/50f else temperature/1200f, 0f, 1f)

        val red = MathHelper.lerp(delta, baseColor.first, finalColor.first)
        val green = MathHelper.lerp(delta, baseColor.second, finalColor.second)
        val blue = MathHelper.lerp(delta, baseColor.third, finalColor.third)

        val textColor = ((red * 255 + 0.5).toInt() shl 16) or ((green * 255 + 0.5).toInt() shl 8) or ((blue * 255 + 0.5).toInt() shl 0)

        widgets.addText(Text.literal("$temperature °C"), 20, 4, textColor,true)
    }
}