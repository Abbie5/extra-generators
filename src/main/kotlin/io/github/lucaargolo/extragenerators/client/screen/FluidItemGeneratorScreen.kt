@file:Suppress("DEPRECATION", "UnstableApiUsage")

package io.github.lucaargolo.extragenerators.client.screen

import io.github.lucaargolo.extragenerators.common.blockentity.FluidItemGeneratorBlockEntity
import io.github.lucaargolo.extragenerators.common.containers.FluidItemGeneratorScreenHandler
import io.github.lucaargolo.extragenerators.network.PacketCompendium
import io.github.lucaargolo.extragenerators.utils.InventoryUtils.getTankFluidTooltip
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import io.github.lucaargolo.extragenerators.utils.renderGuiRect
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper

class FluidItemGeneratorScreen(handler: FluidItemGeneratorScreenHandler, inventory: PlayerInventory, title: Text): AbstractGeneratorScreen<FluidItemGeneratorScreenHandler, FluidItemGeneratorBlockEntity>(handler, inventory, title) {

    private val texture = ModIdentifier("textures/gui/fluid_item_generator.png")

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(button == 0 && (x+134.0..x+150.0).contains(mouseX) && (y+17.0..y+69.0).contains(mouseY)) {
            val buf = PacketByteBufs.create()
            buf.writeBlockPos(handler.entity.pos)
            ClientPlayNetworking.send(PacketCompendium.INTERACT_CURSOR_WITH_TANK, buf)
        }
        return super.mouseClicked(mouseX, mouseY, button)
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
        if((x+134..x+150).contains(mouseX) && (y+17..y+69).contains(mouseY)) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, handler.fluidVolume.getTankFluidTooltip(handler.entity.fluidInv.capacity), mouseX, mouseY)
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
        handler.fluidVolume.let {
            if(!it.resource.isBlank) {
                val a = it.amount/81
                val b = handler.entity.fluidInv.capacity/81F
                val fluidPercentage = a/b
                val fluidOffset = MathHelper.lerp(fluidPercentage, 0F, 52F)
                handler.fluidVolume.resource.renderGuiRect(context, x+134f, y+69f, fluidOffset)
            }
        }
        context.drawTexture(texture, x+134, y+17, 198, 0, 16, 52)
    }

}