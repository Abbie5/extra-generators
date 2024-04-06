package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.math.MathHelper
import java.awt.Color

class ThermoelectricGeneratorEmiRecipe(
    category: EmiRecipeCategory,
    private val input: EmiIngredient,
    private val temperature: Int
) : BasicEmiRecipe(
    category, null, 80, 18
) {

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

        val textColor = TextColor.fromRgb(Color(red, green, blue).rgb)

        widgets.addText(Text.literal("$temperature °C").styled { it.withColor(textColor) }, 20, 4, -1,true)
    }
}