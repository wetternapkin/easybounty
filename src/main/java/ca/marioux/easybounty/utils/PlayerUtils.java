package ca.marioux.easybounty.utils;

import ca.marioux.easybounty.domain.PlayerId;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUtils {

    public static Map<PlayerId, String> getAllPlayersOnlineAndOffline() {
        Map<PlayerId, String> players = new HashMap<PlayerId, String>();

        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            PlayerId playerId = new PlayerId(offlinePlayer.getUniqueId());
            players.put(playerId, offlinePlayer.getName());
        }

        return players;
    }

    public static PlayerId getPlayerIdFromServer(String playername) {
        Player onlinePlayer = Bukkit.getPlayer(playername);
        if (onlinePlayer == null) {
            for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName().equals(playername)) {
                    return new PlayerId(offlinePlayer.getUniqueId());
                }
            }
        } else {
            return new PlayerId(onlinePlayer.getUniqueId());
        }

        return null;
    }

    public static String getPlayerNameFromServer(PlayerId playerId) {
        Player onlinePlayer = Bukkit.getPlayer(playerId.getUuid());
        if (onlinePlayer == null) {
            for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getUniqueId().equals(playerId.getUuid())) {
                    return offlinePlayer.getName();
                }
            }
        } else {
            return onlinePlayer.getName();
        }

        return null;
    }
}
