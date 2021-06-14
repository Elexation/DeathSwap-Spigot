package me.elexation.deathswap.commands;

import me.elexation.deathswap.DeathSwap;
import me.elexation.deathswap.tabcompleters.SyncTabCompleter;
import me.elexation.deathswap.team.Team;
import me.elexation.deathswap.team.TeamFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SyncCommand implements CommandExecutor {

    private final Map<Player, Player> requestMap = new HashMap<>();

    public SyncCommand() {
        DeathSwap.getPlugin().getCommand("Sync").setUsage(ChatColor.GOLD + "Usage: /<command> [accept] <player>");
        DeathSwap.getPlugin().getCommand("Sync").setTabCompleter(new SyncTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("deathswap.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        if (TeamFunctions.isPlayerSynced(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You are already synced with a player");
            return true;
        }
        if (args.length > 2 || args.length < 1) return false;
        if (args[0].equals("accept")) {
            if (args.length != 2) return false;
            if (!requestMap.containsValue(player)) {
                player.sendMessage(ChatColor.DARK_RED + "No one sent you a sync request");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (!target.getName().equals(args[1])) {
                player.sendMessage(ChatColor.DARK_RED + "Player not found");
                return true;
            }
            if (!requestMap.containsKey(target)) {
                player.sendMessage(ChatColor.DARK_RED + "This player did not send you a sync request");
                return true;
            }
            if (!requestMap.get(target).equals(player)) {
                player.sendMessage(ChatColor.DARK_RED + "This player did not send you a sync request");
                return true;
            }
            DeathSwap.getTeams().add(new Team(target, player));
            player.sendMessage(ChatColor.GREEN + "Sync complete");
            target.sendMessage(ChatColor.GREEN + "Sync complete");
            requestMap.remove(player);
            requestMap.remove(target);
            return true;
        }
        if (args.length != 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (!target.getName().equals(args[0])) {
            player.sendMessage(ChatColor.DARK_RED + "Player not found");
            return true;
        }
        if (TeamFunctions.isPlayerSynced(target)) {
            player.sendMessage(ChatColor.DARK_RED + "This player is already synced");
            return true;
        }
        if (player.getName().equals(target.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "You cannot sync with yourself");
            return true;
        }
        if (requestMap.containsKey(player) && requestMap.get(player).equals(target)) {
            player.sendMessage(ChatColor.DARK_RED + "You already sent a sync request to this player");
            return true;
        }
        requestMap.put(player, target);
        player.sendMessage(ChatColor.GOLD + "Sync request sent to " + target.getName());
        target.sendMessage(ChatColor.GOLD + player.getName() + " sent you a sync request. Do '/sync accept " + player.getName() + "' to accept");
        return true;
    }
}
