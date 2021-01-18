package ca.marioux.easybounty.listeners;

import ca.marioux.easybounty.domain.PlayerId;
import ca.marioux.easybounty.domain.Refund;
import ca.marioux.easybounty.domain.RefundRepository;
import ca.marioux.easybounty.domain.exception.RefundNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class PlayerConnectListener implements Listener {

    private final RefundRepository refundRepository;

    public PlayerConnectListener(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerId playerId = new PlayerId(event.getPlayer().getUniqueId());

        List<Refund> refunds = refundRepository.removeAll(playerId);

        if (refunds.size() > 0) {
            player.sendMessage(ChatColor.AQUA + "One or more of your bounties were canceled. Your reward has been refund in your inventory");
        }

        for (Refund refund : refunds) {
            player.getInventory().addItem(refund.getReward());
        }
    }
}
