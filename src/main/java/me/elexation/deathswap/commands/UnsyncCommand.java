package me.elexation.deathswap.commands;

import me.elexation.deathswap.DeathSwap;
import me.elexation.deathswap.team.Team;
import me.elexation.deathswap.team.TeamFunctions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnsyncCommand implements CommandExecutor {

    public UnsyncCommand() {
        DeathSwap.getPlugin().getCommand("Unsync").setUsage(ChatColor.GOLD + "Usage: /<command>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("deathswap.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        if (args.length > 0) return false;
        Team playerTeam = TeamFunctions.getPlayerTeam(player);
        if (playerTeam == null) {
            player.sendMessage(ChatColor.DARK_RED + "You are not synced with a player");
            return true;
        }
        DeathSwap.getTeams().remove(playerTeam);
        player.sendMessage(ChatColor.GREEN + "You have been unsynced");
        return true;
    }
}
