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
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class CustomBrew : JavaPlugin() {

    private val durationBase = 3.minutes.inWholeTicks.toInt()
    private val durationLong = 8.minutes.inWholeTicks.toInt()
    private val durationStrong = 1.5.minutes.inWholeTicks.toInt()

    private val mixKeys = ArrayList<NamespacedKey>(9)

    override fun onEnable() {
        val thickPotion = ItemStack(Material.POTION)
        thickPotion.editMeta {
            (it as PotionMeta).basePotionType = PotionType.THICK
        }
        val hastePotion = addMix(thickPotion, durationBase, 0, Material.HONEYCOMB_BLOCK, "potion")
        /*val longHastePotion =*/ addMix(hastePotion, durationLong, 0, Material.REDSTONE)
        /*val strongHastePotion =*/ addMix(hastePotion, durationStrong, 1, Material.GLOWSTONE_DUST)
    }

    override fun onDisable() {
        mixKeys.forEach {
            Bukkit.getPotionBrewer().removePotionMix(it)
        }
    }

    private fun addMix(
        basePotion: ItemStack, duration: Int, amplifier: Int, ingredient: Material, nameKey: String? = null
    ): ItemStack {
        val resultPotion = basePotion.clone()
        resultPotion.editMeta {
            if (nameKey != null) {
                it.customName(
                    Component.translatable(
                        "potion.withAmplifier",
                        Style.style().decoration(TextDecoration.ITALIC, false).build(),
                        Component.translatable("effect.minecraft.haste"),
                        Component.translatable("item.minecraft.$nameKey")
                    )
                )
            }
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, duration, amplifier),
                true
            )
        }
        val mixKey = NamespacedKey(this, Random.nextLong().toHexString())
        Bukkit.getPotionBrewer().addPotionMix(
            PotionMix(
                mixKey,
                resultPotion,
                RecipeChoice.ExactChoice(basePotion),
                RecipeChoice.ExactChoice(ItemStack(ingredient)),
            )
        )
        mixKeys.add(mixKey)
        return resultPotion
    }
}

private val Duration.inWholeTicks: Long
    get() = this.inWholeMilliseconds / MS_PER_TICK

private const val MS_PER_TICK = 50
