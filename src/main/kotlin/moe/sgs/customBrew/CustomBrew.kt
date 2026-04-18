package moe.sgs.customBrew

import io.papermc.paper.potion.PotionMix
import io.papermc.paper.util.Tick
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.Translatable
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import kotlin.time.Duration
import kotlin.time.DurationUnit

class CustomBrew : JavaPlugin() {

    private val potionHasteKey = NamespacedKey(this, "potion_of_haste")

    override fun onEnable() {
        val potionOfHaste = ItemStack(Material.POTION)
        potionOfHaste.editMeta {
            it.customName(
                Component.translatable(
                    "potion.withAmplifier",
                    Style.style().decoration(TextDecoration.ITALIC, false).build(),
                    Component.translatable("effect.minecraft.haste"),
                    Component.translatable("item.minecraft.potion")
                )
            )
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, 3 * 60 * 20, 0),
                true
            )
        }
        val mundanePotion = ItemStack(Material.POTION)
        mundanePotion.editMeta {
            (it as PotionMeta).basePotionType = PotionType.MUNDANE
        }
        Bukkit.getPotionBrewer().addPotionMix(
            PotionMix(
                potionHasteKey,
                potionOfHaste,
                RecipeChoice.ExactChoice(mundanePotion),
                RecipeChoice.itemType(ItemType.HONEYCOMB_BLOCK),
            )
        )
    }

    override fun onDisable() {
        Bukkit.getPotionBrewer().removePotionMix(potionHasteKey)
    }
}
