package me.elexation.deathswap.team;

import me.elexation.deathswap.DeathSwap;
import org.bukkit.entity.Player;

public class TeamFunctions {

    public static Team getPlayerTeam(Player player){
        for (Team team : DeathSwap.getTeams())
            if (team.getPlayer1().equals(player) || team.getPlayer2().equals(player)) return team;
        return null;
    }

    public static boolean isPlayerSynced(Player player){
        for (Team team : DeathSwap.getTeams())
            if (team.getPlayer1().equals(player) || team.getPlayer2().equals(player)) return true;
        return false;
    }
}
