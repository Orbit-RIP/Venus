package rip.orbit.hcteams.wanderer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class WandererButton {

    private ItemStack itemStack;
    private int price;

}
