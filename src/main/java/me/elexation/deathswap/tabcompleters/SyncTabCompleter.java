package me.elexation.deathswap.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        if (!player.hasPermission("deathswap.use")) return null;
        if (args.length > 2) return Arrays.asList("");
        if (args.length == 1){
            List<String> complete = new ArrayList<>();
            for (Player online : Bukkit.getOnlinePlayers()) complete.add(online.getName());
            complete.add("accept");
            return complete;
        }
        return null;
    }
}
