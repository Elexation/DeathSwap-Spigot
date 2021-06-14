package me.elexation.deathswap;

import me.elexation.deathswap.commands.SyncCommand;
import me.elexation.deathswap.commands.UnsyncCommand;
import me.elexation.deathswap.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DeathSwap extends JavaPlugin implements CommandExecutor, Listener, TabCompleter {

    private static final List<Team> TEAMS = new ArrayList<>();
    private static DeathSwap plugin;
    private static boolean isDeathSwapOn = false;
    private int time = 300;
    private BukkitTask task;

    public static DeathSwap getPlugin() {
        return plugin;
    }

    public static List<Team> getTeams() {
        return TEAMS;
    }

    public static boolean isDeathSwapOn() {
        return isDeathSwapOn;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        this.getCommand("Sync").setExecutor(new SyncCommand());
        this.getCommand("Unsync").setExecutor(new UnsyncCommand());
        this.getCommand("Deathswap").setExecutor(this);
        this.getCommand("Deathswap").setTabCompleter(this);
        this.getCommand("Deathswap").setUsage(ChatColor.GOLD + "Usage: /<command> <start|stop|time> [time]");
    }

    private void start(int time) {
        isDeathSwapOn = true;
        task = (new BukkitRunnable() {
            int timer = time;

            public void run() {
                if (timer <= 10 && timer != 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Swapping in " + timer + (timer == 1 ? " second!" : " seconds!"));
                }
                if (timer == 0) {
                    for (Team team : TEAMS) {
                        Location loc = team.getPlayer1().getLocation();
                        team.getPlayer1().teleport(team.getPlayer2());
                        team.getPlayer2().teleport(loc);
                    }
                    timer = time;
                } else {
                    timer--;
                }
            }
        }).runTaskTimer(this, 0L, 20L);
    }

    private void stop() {
        isDeathSwapOn = false;
        task.cancel();
        task = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("deathswap.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equals("start")) {
                if (isDeathSwapOn) {
                    player.sendMessage(ChatColor.RED + "Deathswap is already on");
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "Deathswap has started");
                start(time);
                return true;
            }
            if (args[0].equals("stop")) {
                if (!isDeathSwapOn) {
                    player.sendMessage(ChatColor.RED + "Deathswap is already off");
                    return true;
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "Deathswap has stopped");
                stop();
                return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equals("time")) {
                try {
                    int time = Integer.parseInt(args[1]);
                    this.time = time;
                    player.sendMessage(ChatColor.GREEN + "Deathswap time has been set to " + time + " seconds");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Deathswap time can only be a whole number");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        if (!player.hasPermission("deathswap.use")) return null;
        if (args.length > 1) return Arrays.asList("");
        if (args.length == 1) {
            return Arrays.asList("start", "stop", "time");
        }
        return null;
    }
}
