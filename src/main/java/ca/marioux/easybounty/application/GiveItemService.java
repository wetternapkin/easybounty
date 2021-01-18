package ca.marioux.easybounty.application;

import ca.marioux.easybounty.domain.PlayerId;
import ca.marioux.easybounty.domain.Refund;
import ca.marioux.easybounty.domain.RefundRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemService {

    private final RefundRepository refundRepository;

    public GiveItemService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    public void give(PlayerId player, ItemStack item) {
        Player onlinePlayer = Bukkit.getPlayer(player.getUuid());
        if (onlinePlayer == null) {
            Refund refund = new Refund(player, item);
            refundRepository.add(refund);
        } else {
            onlinePlayer.getInventory().addItem(item);
            onlinePlayer.sendMessage(ChatColor.AQUA + "One of your bounties has been removed by an admin and your reward is now back in your inventory");
        }
    }
}
