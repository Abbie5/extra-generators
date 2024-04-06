package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.TankWidget
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.compat.emi.ExtraGeneratorsCategory
import io.github.lucaargolo.extragenerators.utils.FluidGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants

class FluidItemGeneratorEmiRecipe(
    category: ExtraGeneratorsCategory,
    private val itemInput: EmiIngredient,
    private val fluidInput: EmiIngredient,
    private val output: FluidGeneratorFuel
) : AbstractExtraGeneratorsEmiRecipe(
    category, null, output
) {
    override val backgroundTextureId = ModIdentifier("textures/gui/fluid_item_generator.png")

    init {
        inputs = listOf(itemInput, fluidInput)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        widgets.addSlot(itemInput, 55, 36)
            .drawBack(false)
            .recipeContext(this)
        widgets.add(TankWidget(fluidInput, 109, 0, 18, 54, 4 * FluidConstants.BUCKET))
            .drawBack(false)
            .recipeContext(this)
    }
}