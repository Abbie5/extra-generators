@file:Suppress("DEPRECATION", "UnstableApiUsage")

package io.github.lucaargolo.extragenerators.common.blockentity

import io.github.lucaargolo.extragenerators.ExtraGenerators
import io.github.lucaargolo.extragenerators.utils.ItemGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.SimpleSidedInventory
import io.github.lucaargolo.extragenerators.utils.fromNbt
import io.github.lucaargolo.extragenerators.utils.toNbt
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

class ColorfulGeneratorBlockEntity(pos: BlockPos, state: BlockState): AbstractGeneratorBlockEntity<ColorfulGeneratorBlockEntity>(BlockEntityCompendium.COLORFUL_GENERATOR_TYPE, pos, state) {

    val itemInv = SimpleSidedInventory(3, { slot, stack ->
        when(slot) {
            0 -> initialized && (stack.isEmpty || stack.isIn(ExtraGenerators.RED_ITEMS))
            1 -> initialized && (stack.isEmpty || stack.isIn(ExtraGenerators.GREEN_ITEMS))
            2 -> initialized && (stack.isEmpty || stack.isIn(ExtraGenerators.BLUE_ITEMS))
            else -> true
        }
    }, { _, _ ->  false }, { intArrayOf(0, 1, 2) })

    var burningFuel: ItemGeneratorFuel? = null

    override fun isServerRunning() = burningFuel?.let { energyStorage.amount + MathHelper.floor(it.energyOutput/it.burnTime) <= energyStorage.getCapacity() } ?: false

    override fun getCogWheelRotation(): Float = burningFuel?.let { MathHelper.floor(it.energyOutput/it.burnTime)/10f } ?: 0f

    override fun tick() {
        super.tick()
        if(world?.isClient == false) {
            burningFuel?.let {
                val energyPerTick = MathHelper.floor(it.energyOutput/it.burnTime)
                if (energyStorage.amount + energyPerTick <= energyStorage.getCapacity()) {
                    energyStorage.amount += energyPerTick
                    it.currentBurnTime--
                }
                if (it.currentBurnTime <= 0) {
                    burningFuel = null
                    markDirtyAndSync()
                }
            }
            if (burningFuel == null) {
                val redStack = itemInv.getStack(0)
                val greenStack = itemInv.getStack(1)
                val blueStack = itemInv.getStack(2)
                if (!redStack.isEmpty && !greenStack.isEmpty && !blueStack.isEmpty) {
                    redStack.decrement(1)
                    greenStack.decrement(1)
                    blueStack.decrement(1)
                    burningFuel = getFuel()
                    markDirtyAndSync()
                }
            }
        }
    }

    override fun writeNbt(tag: NbtCompound) {
        tag.put("itemInv", itemInv.toNbt())
        burningFuel?.let { tag.put("burningFuel", it.toTag()) }
        super.writeNbt(tag)
    }

    override fun readNbt(tag: NbtCompound) {
        super.readNbt(tag)
        itemInv.fromNbt(tag.get("itemInv"))
        burningFuel = ItemGeneratorFuel.fromTag(tag.getCompound("burningFuel"))
    }

    companion object {
        fun getFuel() = ItemGeneratorFuel(100, 3200.0)
    }

}