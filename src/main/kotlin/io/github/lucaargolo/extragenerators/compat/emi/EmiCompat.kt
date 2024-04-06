package io.github.lucaargolo.extragenerators.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.FabricEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import io.github.lucaargolo.extragenerators.ExtraGenerators
import io.github.lucaargolo.extragenerators.common.blockentity.ColorfulGeneratorBlockEntity
import io.github.lucaargolo.extragenerators.common.resource.ResourceCompendium
import io.github.lucaargolo.extragenerators.compat.emi.recipe.*
import io.github.lucaargolo.extragenerators.utils.FluidGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ItemGeneratorFuel
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.item.EnchantedBookItem
import net.minecraft.registry.Registries

class EmiCompat : EmiPlugin {
    val itemGeneratorMap = mapOf(
        "blast" to ExtraGeneratorsCategories.BLAST_GENERATOR,
        "demise" to ExtraGeneratorsCategories.DEMISE_GENERATOR,
        "dragon" to ExtraGeneratorsCategories.DRAGON_GENERATOR,
        "icy" to ExtraGeneratorsCategories.ICY_GENERATOR,
        "sludgy" to ExtraGeneratorsCategories.SLUDGY_GENERATOR,
        "teleport" to ExtraGeneratorsCategories.TELEPORT_GENERATOR,
        "withered" to ExtraGeneratorsCategories.WITHERED_GENERATOR
    )
    val fluidGeneratorMap = mapOf(
        "scalding" to ExtraGeneratorsCategories.SCALDING_GENERATOR
    )

    override fun register(registry: EmiRegistry) {
        registry.addCategory(ExtraGeneratorsCategories.BURNABLE_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.GLUTTONY_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.BLAST_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.DEMISE_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.DRAGON_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.ICY_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.SLUDGY_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.TELEPORT_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.WITHERED_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.ENCHANTED_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.BREW_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.SCALDING_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.REDSTONE_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.STEAM_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.THERMOELECTRIC_GENERATOR)
        registry.addCategory(ExtraGeneratorsCategories.COLORFUL_GENERATOR)

        registry.addWorkstation(ExtraGeneratorsCategories.BURNABLE_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.BURNABLE_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.GLUTTONY_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.GLUTTONY_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.BLAST_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.BLAST_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.DEMISE_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.DEMISE_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.DRAGON_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.DRAGON_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.ICY_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.ICY_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.SLUDGY_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.SLUDGY_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.TELEPORT_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.TELEPORT_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.WITHERED_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.WITHERED_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.ENCHANTED_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.ENCHANTED_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.BREW_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.BREW_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.SCALDING_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.SCALDING_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.REDSTONE_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.REDSTONE_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.STEAM_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.STEAM_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.THERMOELECTRIC_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.THERMOELECTRIC_GENERATOR.block))
        registry.addWorkstation(ExtraGeneratorsCategories.COLORFUL_GENERATOR, EmiStack.of(ExtraGeneratorsCategories.COLORFUL_GENERATOR.block))

        ResourceCompendium.ITEM_GENERATORS.clientIngredientsMap.forEach { (id, ingredientMap) ->
            val category = itemGeneratorMap[id] ?: return@forEach
            ingredientMap.forEach { (ingredient, fuel) ->
                registry.addRecipe(
                    ItemGeneratorEmiRecipe(
                        category,
                        EmiIngredient.of(ingredient),
                        fuel
                    )
                )
            }
        }
        ResourceCompendium.FLUID_GENERATORS.clientFluidKeysMap.forEach { (id, fluidKeyMap) ->
            println(id)
            val category = fluidGeneratorMap[id] ?: return@forEach
            fluidKeyMap.forEach { (fluidKey, fuel) ->
                registry.addRecipe(
                    FluidGeneratorEmiRecipe(
                        category,
                        FabricEmiStack.of(fuel.fluidInput.resource, fuel.fluidInput.amount),
                        fuel
                    )
                )
            }
        }
        ResourceCompendium.BLOCK_TEMPERATURE.clientTemperatureMap.forEach { (block, temperature) ->
            registry.addRecipe(ThermoelectricGeneratorEmiRecipe(ExtraGeneratorsCategories.THERMOELECTRIC_GENERATOR, EmiStack.of(block), temperature))
        }

        Registries.ITEM.forEach {
//            ItemGeneratorFuel.fromBurnableGeneratorFuel(it)?.run {
//                ItemGeneratorEmiRecipe(ExtraGeneratorsCategories.BURNABLE_GENERATOR, "", EmiStack.of(it), this)
//            }?.let(registry::addRecipe)
//            ItemGeneratorFuel.fromGluttonyGeneratorFuel(it)?.run {
//                ItemGeneratorEmiRecipe(ExtraGeneratorsCategories.GLUTTONY_GENERATOR, EmiStack.of(it), this)
//            }?.let(registry::addRecipe)
//            ItemGeneratorFuel.fromEnchantedGeneratorFuel(it.defaultStack)?.run {
//                ItemGeneratorEmiRecipe(ExtraGeneratorsCategories.ENCHANTED_GENERATOR, EmiStack.of(it), this)
//            }?.let(registry::addRecipe)
            ItemGeneratorFuel.fromBrewGeneratorFuel(it.defaultStack)?.run {
                ItemGeneratorEmiRecipe(ExtraGeneratorsCategories.BREW_GENERATOR, EmiStack.of(it), this)
            }?.let(registry::addRecipe)
            FluidGeneratorFuel.fromRedstoneGeneratorFuel(it.defaultStack)?.run {
                FluidItemGeneratorEmiRecipe(ExtraGeneratorsCategories.REDSTONE_GENERATOR, EmiStack.of(it), FabricEmiStack.of(fluidInput.resource, fluidInput.amount), this)
            }?.let(registry::addRecipe)
//            FluidGeneratorFuel.fromSteamGeneratorFuel(it.defaultStack)?.run {
//                FluidItemGeneratorEmiRecipe(ExtraGeneratorsCategories.STEAM_GENERATOR, EmiStack.of(it), FabricEmiStack.of(fluidInput.resource, fluidInput.amount), this)
//            }?.let(registry::addRecipe)
        }

        val burnable = mutableMapOf<ItemGeneratorFuel, MutableList<EmiStack>>()
        Registries.ITEM.forEach {
            ItemGeneratorFuel.fromBurnableGeneratorFuel(it)?.run {
                val list = burnable[this]
                val stack = EmiStack.of(it)
                if (list == null) {
                    burnable[this] = arrayListOf(stack)
                } else {
                    list.add(stack)
                }
            }
        }
        burnable.forEach {
            registry.addRecipe(ItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.BURNABLE_GENERATOR,
                EmiIngredient.of(it.value),
                it.key
            ))
        }

        val steam = mutableMapOf<FluidGeneratorFuel, MutableList<EmiStack>>()
        Registries.ITEM.forEach {
            FluidGeneratorFuel.fromSteamGeneratorFuel(it.defaultStack)?.run {
                val list = steam[this]
                val stack = EmiStack.of(it)
                if (list == null) {
                    steam[this] = arrayListOf(stack)
                } else {
                    list.add(stack)
                }
            }
        }
        steam.forEach {
            registry.addRecipe(FluidItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.STEAM_GENERATOR,
                EmiIngredient.of(it.value),
                FabricEmiStack.of(it.key.fluidInput.resource, it.key.fluidInput.amount),
                it.key
            ))
        }

        val gluttony = mutableMapOf<ItemGeneratorFuel, MutableList<EmiStack>>()
        Registries.ITEM.forEach {
            ItemGeneratorFuel.fromGluttonyGeneratorFuel(it)?.run {
                val list = gluttony[this]
                val stack = EmiStack.of(it)
                if (list == null) {
                    gluttony[this] = arrayListOf(stack)
                } else {
                    list.add(stack)
                }
            }
        }
        gluttony.forEach {
            registry.addRecipe(ItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.GLUTTONY_GENERATOR,
                EmiIngredient.of(it.value),
                it.key
            ))
        }

        Registries.ENCHANTMENT.forEach {
            for (level: Int in it.minLevel..it.maxLevel) {
                val bookItemStack = EnchantedBookItem.forEnchantment(EnchantmentLevelEntry(it, level))
                ItemGeneratorFuel.fromEnchantedGeneratorFuel(bookItemStack)?.run {
                    ItemGeneratorEmiRecipe(
                        ExtraGeneratorsCategories.ENCHANTED_GENERATOR,
                        EmiStack.of(bookItemStack),
                        this
                    )
                }?.let(registry::addRecipe)
            }
        }

        registry.addRecipe(ColorfulGeneratorEmiRecipe(EmiIngredient.of(ExtraGenerators.RED_ITEMS), EmiIngredient.of(ExtraGenerators.GREEN_ITEMS), EmiIngredient.of(ExtraGenerators.BLUE_ITEMS), ColorfulGeneratorBlockEntity.getFuel()))
    }
}