package net.frozenorb.foxtrot.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

public class TeamCommand {

    @Command(names={ "team", "t", "f", "faction", "fac" }, permission="")
    public static void  team(Player sender) {

        String[] msg = {

                "§7§m-----------------------------------------------------",
                "§6§lVexor §7- §fFaction Help",
                "§7§m-----------------------------------------------------",


                "§6General Commands:",
                "§6» §e/f create <teamName> §7- §fCreate a new team",
                "§6» §e/f accept <teamName> §7- §fAccept a pending invitation",
                "§6» §e/f lives add <amount> §7- §fIrreversibly add lives to your faction",
                "§6» §e/f leave §7- §fLeave your current team",
                "§6» §e/f home §7- §fTeleport to your team home",
                "§6» §e/f stuck §7- §fTeleport out of enemy territory",
                "§6» §e/f deposit §7<amount|all> - §fDeposit money into your team balance",


                "",
                "§6Information Commands:",
                "§6» §e/f who §7[player|teamName] - §fDisplay team information",
                "§6» §e/f map §7- §fShow nearby claims (identified by pillars)",
                "§6» §e/f list §7- §fShow list of teams online (sorted by most online)",


                "",
                "§6Captain Commands:",
                "§6» §e/f invite <player> §7- §fInvite a player to your team",
                "§6» §e/f uninvite <player> §7- §fRevoke an invitation",
                "§6» §e/f invites §7- §fList all open invitations",
                "§6» §e/f kick <player> §7- §fKick a player from your team",
                "§6» §e/f claim §7- §fStart a claim for your team",
                "§6» §e/f sethome §7- §fSet your team's home at your current location",
                "§6» §e/f withdraw <amount> §7- §fWithdraw money from your team's balance",
                "§6» §e/f announcement §7[message here] - §fSet your team's announcement",

                "",
                "§6Leader Commands:",

                "§6» §e/f coleader §7<add|remove> <player> - §fAdd or remove a co-leader",
                "§6» §e/f captain §7<add|remove> <player> - §fAdd or remove a captain",
                "§6» §e/f unclaim §7[all] - §fUnclaim land",
                "§6» §e/f rename <newName> §7- §fRename your team",
                "§6» §e/f disband §7- §fDisband your team",


                "§7§m-----------------------------------------------------",



        };
        sender.sendMessage(msg);
    }

}