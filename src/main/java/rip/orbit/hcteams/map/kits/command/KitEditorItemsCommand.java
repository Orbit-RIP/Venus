package rip.orbit.hcteams.map.kits.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.map.kits.DefaultKit;
import rip.orbit.hcteams.map.kits.editor.setup.KitEditorItemsMenu;

public class KitEditorItemsCommand {

    @Command(names = { "kitadmin editoritems" }, description = "Edit a kit's editor items", permission = "op")
    public static void execute(Player player, @cc.fyre.proton.command.param.Parameter(name = "kit") DefaultKit kit) {
        new KitEditorItemsMenu(kit).openMenu(player);
    }

}
