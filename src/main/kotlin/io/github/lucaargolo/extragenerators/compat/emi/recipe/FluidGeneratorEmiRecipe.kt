package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.TankWidget
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.compat.emi.ExtraGeneratorsCategory
import io.github.lucaargolo.extragenerators.utils.FluidGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants

class FluidGeneratorEmiRecipe(
    category: ExtraGeneratorsCategory,
    private val input: EmiIngredient,
    private val output: FluidGeneratorFuel
) : AbstractExtraGeneratorsEmiRecipe(
    category, null, output
) {

    override val backgroundTextureId = ModIdentifier("textures/gui/fluid_generator.png")

    init {
        inputs = listOf(input)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        widgets.add(TankWidget(input, 109, 0, 18, 54, 4 * FluidConstants.BUCKET))
            .drawBack(false)
            .recipeContext(this)
    }
}