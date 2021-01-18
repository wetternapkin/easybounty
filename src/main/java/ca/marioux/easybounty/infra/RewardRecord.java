package ca.marioux.easybounty.infra;

import ca.marioux.easybounty.domain.Bounty;
import ca.marioux.easybounty.domain.BountyId;
import ca.marioux.easybounty.domain.PlayerId;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardRecord {
    private String reward;
    private int amount;

    public RewardRecord(String reward, int amount) {
        this.reward = reward;
        this.amount = amount;
    }

    public RewardRecord(List<Object> values) {
        this.reward = (String) values.get(0);
        this.amount = (Integer) values.get(1);
    }

    public String getReward() {
        return reward;
    }

    public int getAmount() {
        return amount;
    }

    public List<Object> asList() {
        List<Object> list = new ArrayList<Object>();
        list.add(reward);
        list.add(amount);

        return list;
    }

    public Bounty toBounty(BountyId bountyId) {
        return new Bounty(
                bountyId,
                new ItemStack(Material.getMaterial(reward), amount));
    }
}
