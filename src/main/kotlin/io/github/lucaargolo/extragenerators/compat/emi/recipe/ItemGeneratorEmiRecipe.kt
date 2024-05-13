package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.compat.emi.ExtraGeneratorsCategory
import io.github.lucaargolo.extragenerators.utils.GeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ModIdentifier

class ItemGeneratorEmiRecipe(
    category: ExtraGeneratorsCategory,
    private val input: EmiIngredient,
    output: GeneratorFuel
) : AbstractExtraGeneratorsEmiRecipe(
    category,
    null,
    output
) {
    override val backgroundTextureId = ModIdentifier("textures/gui/item_generator.png")

    init {
        inputs = listOf(input)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        widgets.addSlot(input, 55, 36)
            .drawBack(false)
            .recipeContext(this)

    }
}