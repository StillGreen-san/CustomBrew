package moe.sgs.customBrew

import io.papermc.paper.potion.PotionMix
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

class CustomBrew : JavaPlugin() {

    private val potionHasteKey = NamespacedKey(this, "potion_of_haste")

    override fun onEnable() {
        val potionOfHaste = ItemStack(Material.POTION)
        potionOfHaste.editMeta {
            (it as PotionMeta).addCustomEffect(
                PotionEffect(PotionEffectType.HASTE, 30 * 20, 1),
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
