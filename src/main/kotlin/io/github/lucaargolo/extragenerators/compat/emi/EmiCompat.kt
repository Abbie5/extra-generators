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
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
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
            registry.addRecipe(ThermoelectricGeneratorEmiRecipe(ExtraGeneratorsCategories.THERMOELECTRIC_GENERATOR, block, temperature))
        }

        Registries.ITEM.forEach {
            FluidGeneratorFuel.fromRedstoneGeneratorFuel(it.defaultStack)?.run {
                FluidItemGeneratorEmiRecipe(ExtraGeneratorsCategories.REDSTONE_GENERATOR, EmiStack.of(it), FabricEmiStack.of(fluidInput.resource, fluidInput.amount), this)
            }?.let(registry::addRecipe)
        }

        Registries.POTION
            .flatMap {
                listOf(Items.POTION, Items.SPLASH_POTION, Items.TIPPED_ARROW, Items.LINGERING_POTION)
                    .map { it.defaultStack }
                    .map { stack -> PotionUtil.setPotion(stack, it) }
            }.associateWith { ItemGeneratorFuel.fromBrewGeneratorFuel(it) }
            .mapNotNull { it.value?.let { value -> it.key to value } }
            .toMap()
            .mapKeys { EmiStack.of(it.key) }
            .map { ItemGeneratorEmiRecipe(ExtraGeneratorsCategories.BREW_GENERATOR, it.key, it.value) }
            .forEach { registry.addRecipe(it) }

        Registries.ITEM
            .filter { ItemGeneratorFuel.fromBurnableGeneratorFuel(it) != null }
            .groupBy(ItemGeneratorFuel::fromBurnableGeneratorFuel)
            .mapValues { EmiIngredient.of(it.value.map(EmiStack::of)) }
            .map { it.key?.let { fuel -> ItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.BURNABLE_GENERATOR,
                it.value,
                fuel
            ) } }.forEach(registry::addRecipe)

        Registries.ITEM
            .map(Item::getDefaultStack)
            .filter { FluidGeneratorFuel.fromSteamGeneratorFuel(it) != null }
            .groupBy(FluidGeneratorFuel::fromSteamGeneratorFuel)
            .mapValues { EmiIngredient.of(it.value.map(EmiStack::of)) }
            .map { it.key?.let { fuel -> FluidItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.STEAM_GENERATOR,
                it.value,
                FabricEmiStack.of(fuel.fluidInput.resource, fuel.fluidInput.amount),
                fuel
            ) } }.forEach(registry::addRecipe)

        Registries.ITEM
            .filter { ItemGeneratorFuel.fromGluttonyGeneratorFuel(it) != null }
            .groupBy(ItemGeneratorFuel::fromGluttonyGeneratorFuel)
            .mapValues { EmiIngredient.of(it.value.map(EmiStack::of)) }
            .map { it.key?.let { fuel -> ItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.GLUTTONY_GENERATOR,
                it.value,
                fuel
            ) } }.forEach(registry::addRecipe)

        Registries.ENCHANTMENT
            .flatMap { (it.minLevel..it.maxLevel).map {
                level -> EnchantedBookItem.forEnchantment(EnchantmentLevelEntry(it, level))
            } }.associateWith(ItemGeneratorFuel::fromEnchantedGeneratorFuel)
            .mapNotNull { it.value?.let { value -> it.key to value } }
            .toMap()
            .mapKeys { EmiStack.of(it.key) }
            .map { ItemGeneratorEmiRecipe(
                ExtraGeneratorsCategories.ENCHANTED_GENERATOR,
                it.key,
                it.value
            ) }.forEach(registry::addRecipe)

        registry.addRecipe(ColorfulGeneratorEmiRecipe(
            EmiIngredient.of(ExtraGenerators.RED_ITEMS),
            EmiIngredient.of(ExtraGenerators.GREEN_ITEMS),
            EmiIngredient.of(ExtraGenerators.BLUE_ITEMS),
            ColorfulGeneratorBlockEntity.getFuel()
        ))
    }
}