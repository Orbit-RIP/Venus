package net.frozenorb.foxtrot.map.kits.command;

import net.frozenorb.foxtrot.map.kits.DefaultKit;
import net.frozenorb.foxtrot.map.kits.editor.setup.KitEditorItemsMenu;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.entity.Player;

public class KitEditorItemsCommand {

    @Command(names = { "kit editoritems" }, description = "Edit a kit's editor items", permission = "op")
    public static void execute(Player player, @Parameter(name = "kit") DefaultKit kit) {
        new KitEditorItemsMenu(kit).openMenu(player);
    }

}
