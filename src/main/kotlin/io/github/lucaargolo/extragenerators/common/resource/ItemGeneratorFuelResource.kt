package io.github.lucaargolo.extragenerators.common.resource

import com.google.gson.JsonParser
import io.github.lucaargolo.extragenerators.ExtraGenerators
import io.github.lucaargolo.extragenerators.utils.ItemGeneratorFuel
import io.github.lucaargolo.extragenerators.utils.ModIdentifier
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.resource.ResourceManager
import java.io.InputStreamReader

class ItemGeneratorFuelResource: SimpleSynchronousResourceReloadListener {

    private val ingredientsMap = linkedMapOf<String, LinkedHashMap<Ingredient, ItemGeneratorFuel>>()
    val clientIngredientsMap = linkedMapOf<String, LinkedHashMap<Ingredient, ItemGeneratorFuel>>()

    fun test(id: String, itemStack: ItemStack): ItemGeneratorFuel? {
        val map = if(clientIngredientsMap.isEmpty()) ingredientsMap else clientIngredientsMap
        map[id]?.forEach { (ingredient, fuel) ->
            if(ingredient.test(itemStack)) return fuel
        }
        return null
    }

    fun toBuf(buf: PacketByteBuf) {
        buf.writeInt(ingredientsMap.size)
        ingredientsMap.forEach { (id, ingredientMap) ->
            buf.writeString(id)
            buf.writeInt(ingredientMap.size)
            ingredientMap.forEach { (ingredient, fuel) ->
                ingredient.write(buf)
                fuel.toBuf(buf)
            }
        }
    }

    fun fromBuf(buf: PacketByteBuf) {
        clientIngredientsMap.clear()
        val ingredientsMapSize = buf.readInt()
        repeat(ingredientsMapSize) {
            val ingredientsMapId = buf.readString()
            val ingredientMapSize = buf.readInt()
            repeat(ingredientMapSize) {
                val ingredient = Ingredient.fromPacket(buf)
                val fuel = ItemGeneratorFuel.fromBuf(buf) ?: ItemGeneratorFuel(0, 0.0)
                clientIngredientsMap.getOrPut(ingredientsMapId) { linkedMapOf() } [ingredient] = fuel
            }
        }
    }

    override fun getFabricId() = ModIdentifier("item_generators")

    override fun reload(manager: ResourceManager) {
        ingredientsMap.clear()
        ExtraGenerators.LOGGER.info("Loading item generators resource.")
        manager.findResources("item_generators") { r -> r.path.endsWith(".json") }.forEach { itemsResource ->
            val id = itemsResource.key.path.split("/").lastOrNull()?.replace(".json", "") ?: return@forEach
            val resource = itemsResource.value
            ExtraGenerators.LOGGER.info("Loading $id item generators resource at $itemsResource.")
            try {
                val json = JsonParser.parseReader(InputStreamReader(resource.inputStream, "UTF-8"))
                val jsonArray = json.asJsonArray
                jsonArray.forEach { jsonElement ->
                    val jsonObject = jsonElement.asJsonObject
                    val generatorFuel = ItemGeneratorFuel.fromJson(jsonObject.get("fuel").asJsonObject)
                    generatorFuel?.let {
                        ingredientsMap.getOrPut(id.toString()) { linkedMapOf() }[Ingredient.fromJson(jsonObject.get("ingredient"))] = it
                    }
                }
            }catch (e: Exception) {
                ExtraGenerators.LOGGER.error("Unknown error while trying to read $id item generators resource at ${itemsResource.key}", e)
            }
        }
        ExtraGenerators.LOGGER.info("Finished loading item generators resource (${ingredientsMap.map { it.value.size }.sum()} entries loaded).")
    }

}