package ca.marioux.easybounty.listeners;

import ca.marioux.easybounty.application.BountyService;
import ca.marioux.easybounty.domain.Bounty;
import ca.marioux.easybounty.domain.PlayerId;
import ca.marioux.easybounty.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class KillListener implements Listener {

    private final BountyService bountyService;

    public KillListener(BountyService bountyService) {
        this.bountyService = bountyService;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer != null && !deadPlayer.equals(killer)) {
            PlayerId deadPlayerId = new PlayerId(deadPlayer.getUniqueId());
            List<Bounty> bounties = bountyService.kill(deadPlayerId);

            for (Bounty bounty : bounties) {
                killer.getInventory().addItem(bounty.getReward());
                String benefactor = PlayerUtils.getPlayerNameFromServer(bounty.getBenefactor());
                killer.sendMessage(ChatColor.AQUA +
                        String.format("You received the reward (%s x%d) for killing %s by %s in your inventory",
                                bounty.getReward().getType().toString(),
                                bounty.getReward().getAmount(),
                                deadPlayer.getName(),
                                benefactor));
            }

            if (bounties.size() > 0) {
                killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.25f);
            }
        }
    }
}
