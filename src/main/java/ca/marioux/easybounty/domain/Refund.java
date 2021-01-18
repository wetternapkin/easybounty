package ca.marioux.easybounty.domain;

import org.bukkit.inventory.ItemStack;

public class Refund {

    private PlayerId target;
    private ItemStack reward;

    public Refund(PlayerId target, ItemStack reward) {
        this.target = target;
        this.reward = reward;
    }

    public PlayerId getTarget() {
        return target;
    }

    public ItemStack getReward() {
        return reward;
    }
}
