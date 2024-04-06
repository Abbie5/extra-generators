package io.github.lucaargolo.extragenerators.compat.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import io.github.lucaargolo.extragenerators.common.block.AbstractGeneratorBlock
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.minecraft.block.Block
import net.minecraft.text.Text

class ExtraGeneratorsCategory(path: String, val block: AbstractGeneratorBlock) : EmiRecipeCategory(ModIdentifier(path), EmiStack.of(block)) {

//    init {
//        set.add(this)
//    }
//
//    companion object {
//        private val set = linkedSetOf<ExtraGeneratorsCategory>()
//
//        fun getMatching(id: String) = set.firstOrNull { id == it.id.namespace.replace("_generator", "") }
//
//        fun register(registry: EmiRegistry) = set.forEach {
//            registry.addCategory(it)
//            registry.addWorkstation(it, EmiStack.of(it.block))
//        }
//    }

    override fun getName(): Text = block.name
}