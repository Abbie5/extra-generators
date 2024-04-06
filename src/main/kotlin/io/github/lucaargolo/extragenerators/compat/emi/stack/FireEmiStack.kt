package io.github.lucaargolo.extragenerators.compat.emi.stack

import dev.emi.emi.api.render.EmiTooltipComponents
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.block.AbstractFireBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.registry.Registries
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class FireEmiStack private constructor(
    private val block: AbstractFireBlock
) : EmiStack() {
    override fun render(draw: DrawContext, x: Int, y: Int, delta: Float, flags: Int) {
        if ((flags and RENDER_ICON) != 0) {
            val sprite = MinecraftClient.getInstance()
                .bakedModelManager
                .getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
                .getSprite(id.withPrefixedPath("block/").withSuffixedPath("_0"))
            draw.drawSprite(x, y, 150, 16, 16, sprite)
        }
    }

    override fun isEmpty() = false

    override fun copy() = FireEmiStack(block)

    override fun getNbt() = null

    override fun getKey() = block

    override fun getId() = Registries.BLOCK.getId(block)

    override fun getTooltipText(): List<Text> = listOf()

    override fun getTooltip(): List<TooltipComponent> {
        val list = mutableListOf<TooltipComponent>()
        list.add(EmiTooltipComponents.of(name))
        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            list.add(EmiTooltipComponents.of(Text.literal(id.toString()).formatted(Formatting.DARK_GRAY)))
        }
        EmiTooltipComponents.appendModName(list, id.namespace)
        return list
    }

    override fun getName(): Text = block.name

    companion object {
        fun of(block: AbstractFireBlock): EmiStack = FireEmiStack(block)
    }
}