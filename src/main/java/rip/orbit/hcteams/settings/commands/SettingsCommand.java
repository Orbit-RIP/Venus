package rip.orbit.hcteams.settings.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.settings.menu.SettingsMenu;

public class SettingsCommand {

    @Command(names = {"settings", "options"}, permission = "")
    public static void settings(Player sender) {
        new SettingsMenu().openMenu(sender);
    }

}
