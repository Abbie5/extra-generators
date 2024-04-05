package io.github.lucaargolo.extragenerators.client.screen

import io.github.lucaargolo.extragenerators.common.blockentity.ItemGeneratorBlockEntity
import io.github.lucaargolo.extragenerators.common.containers.ItemGeneratorScreenHandler
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper

class ItemGeneratorScreen(handler: ItemGeneratorScreenHandler, inventory: PlayerInventory, title: Text): AbstractGeneratorScreen<ItemGeneratorScreenHandler, ItemGeneratorBlockEntity>(handler, inventory, title) {

    private val texture = ModIdentifier("textures/gui/item_generator.png")

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(context)
        super.render(context, mouseX, mouseY, delta)
        drawMouseoverTooltip(context, mouseX, mouseY)
        if((x+25..x+33).contains(mouseX) && (y+17..y+69).contains(mouseY)) {
            val a = Text.translatable("screen.extragenerators.common.stored_energy").append(": ").formatted(Formatting.RED)
            val b = Text.literal("%d/%d E".format(handler.energyStored, handler.entity.energyStorage.getCapacity())).formatted(Formatting.GRAY)
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, listOf(a, b), mouseX, mouseY)
        }
    }

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        context.drawTexture(texture, x, y, 0, 0, backgroundWidth, backgroundHeight)
        val energyPercentage = handler.energyStored/handler.entity.energyStorage.getCapacity().toFloat()
        val energyOffset = MathHelper.lerp(energyPercentage, 0F, 52F).toInt()
        context.drawTexture(texture, x+25, y+17+(52-energyOffset), 176, 52-energyOffset, 8, energyOffset)
        handler.burningFuel?.let {
            val p = (it.currentBurnTime * 13f /it.burnTime).toInt()
            context.drawTexture(texture, x+81, y+37+(12-p), 184, 12-p, 14, p+1)
        }

    }

}