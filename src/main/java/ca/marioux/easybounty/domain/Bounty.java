package ca.marioux.easybounty.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Bounty {

    private final BountyId id;
    private final ItemStack reward;

    public Bounty(BountyId id, ItemStack reward) {
        this.id = id;
        this.reward = reward;
    }

    public BountyId getId() {
        return id;
    }

    public PlayerId getTarget() {
        return id.getTarget();
    }

    public ItemStack getReward() {
        return reward;
    }

    public PlayerId getBenefactor() {
        return id.getBenefactor();
    }
}
