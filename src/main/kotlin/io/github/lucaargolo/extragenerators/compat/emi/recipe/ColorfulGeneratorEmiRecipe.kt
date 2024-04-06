package io.github.lucaargolo.extragenerators.compat.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import io.github.lucaargolo.extragenerators.ExtraGenerators
import io.github.lucaargolo.extragenerators.compat.emi.ExtraGeneratorsCategories
import io.github.lucaargolo.extragenerators.utils.ItemGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.minecraft.util.Identifier

class ColorfulGeneratorEmiRecipe(
    private val redInput: EmiIngredient,
    private val greenInput: EmiIngredient,
    private val blueInput: EmiIngredient,
    private val output: ItemGeneratorFuel
) : AbstractExtraGeneratorsEmiRecipe(
    ExtraGeneratorsCategories.COLORFUL_GENERATOR,
    null,
    output
) {

    init {
        inputs = listOf(redInput, greenInput, blueInput)
    }

    override val backgroundTextureId = ModIdentifier("textures/gui/colorful_generator.png")

    private val redFlame = EmiTexture(backgroundTextureId, 184, 0, 14, 14)
    private val greenFlame = EmiTexture(backgroundTextureId, 198, 0, 14, 14)
    private val blueFlame = EmiTexture(backgroundTextureId, 212, 0, 14, 14)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        widgets.addAnimatedTexture(redFlame, 20, 20, fuel.burnTime * 50, false, true, true)
        widgets.addAnimatedTexture(greenFlame, 57, 20, fuel.burnTime * 50, false, true, true)
        widgets.addAnimatedTexture(blueFlame, 92, 20, fuel.burnTime * 50, false, true, true)

        widgets.addSlot(redInput, 19, 36)
            .drawBack(false)
            .recipeContext(this)
        widgets.addSlot(greenInput, 55, 36)
            .drawBack(false)
            .recipeContext(this)
        widgets.addSlot(blueInput, 91, 36)
            .drawBack(false)
            .recipeContext(this)
    }
}