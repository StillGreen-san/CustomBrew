package moe.sgs.customBrew

import io.papermc.paper.potion.PotionMix
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class CustomBrew : JavaPlugin() {

    private val keyHaste = NamespacedKey(this, "potion_of_haste")
    private val keyLongHaste = NamespacedKey(this, "potion_of_long_haste")
    private val keyStrongHaste = NamespacedKey(this, "potion_of_strong_haste")

    override fun onEnable() {
        val hastePotion = ItemStack(Material.POTION)
        hastePotion.editMeta {
            it.customName(
                Component.translatable(
                    "potion.withAmplifier",
                    Style.style().decoration(TextDecoration.ITALIC, false).build(),
                    Component.translatable("effect.minecraft.haste"),
                    Component.translatable("item.minecraft.potion")
                )
            )
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, 3.minutes.inWholeTicks.toInt(), 0),
                true
            )
        }
        val thickPotion = ItemStack(Material.POTION)
        thickPotion.editMeta {
            (it as PotionMeta).basePotionType = PotionType.THICK
        }
        Bukkit.getPotionBrewer().addPotionMix(
            PotionMix(
                keyHaste,
                hastePotion,
                RecipeChoice.ExactChoice(thickPotion),
                RecipeChoice.ExactChoice(ItemStack(Material.HONEYCOMB_BLOCK)),
            )
        )
        val longHastePotion = hastePotion.clone()
        longHastePotion.editMeta {
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, 8.minutes.inWholeTicks.toInt(), 0),
                true
            )
        }
        Bukkit.getPotionBrewer().addPotionMix(
            PotionMix(
                keyLongHaste,
                longHastePotion,
                RecipeChoice.ExactChoice(hastePotion),
                RecipeChoice.ExactChoice(ItemStack(Material.REDSTONE)),
            )
        )
        val strongHastePotion = hastePotion.clone()
        strongHastePotion.editMeta {
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, 1.5.minutes.inWholeTicks.toInt(), 1),
                true
            )
        }
        Bukkit.getPotionBrewer().addPotionMix(
            PotionMix(
                keyStrongHaste,
                strongHastePotion,
                RecipeChoice.ExactChoice(hastePotion),
                RecipeChoice.ExactChoice(ItemStack(Material.GLOWSTONE_DUST)),
            )
        )
    }

    override fun onDisable() {
        Bukkit.getPotionBrewer().removePotionMix(keyHaste)
        Bukkit.getPotionBrewer().removePotionMix(keyLongHaste)
        Bukkit.getPotionBrewer().removePotionMix(keyStrongHaste)
    }
}

private val Duration.inWholeTicks: Long
    get() = this.inWholeMilliseconds / MS_PER_TICK

private const val MS_PER_TICK = 50
