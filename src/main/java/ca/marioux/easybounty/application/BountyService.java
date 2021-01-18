package ca.marioux.easybounty.application;

import ca.marioux.easybounty.application.exception.BenefactorAndTargetAreSameException;
import ca.marioux.easybounty.application.exception.PlayerDoesntHaveRewardInInventoryException;
import ca.marioux.easybounty.domain.*;
import ca.marioux.easybounty.domain.exception.BountyAlreadyExistsException;
import ca.marioux.easybounty.domain.exception.BountyDoesntExistsException;
import ca.marioux.easybounty.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BountyService {

    private final BountyRepository bountyRepository;
    private final RefundRepository refundRepository;
    private final GiveItemService giveItemService;

    public BountyService(BountyRepository bountyRepository,
                         RefundRepository refundRepository,
                         GiveItemService giveItemService) {
        this.bountyRepository = bountyRepository;
        this.refundRepository = refundRepository;
        this.giveItemService = giveItemService;
    }

    public void create(Player benefactor, PlayerId targetId, ItemStack reward) throws PlayerDoesntHaveRewardInInventoryException, BenefactorAndTargetAreSameException, BountyAlreadyExistsException {

        if (benefactor.getUniqueId().equals(targetId.getUuid())) {
            throw new BenefactorAndTargetAreSameException();
        }

        if (!playerHasRewardInInventory(benefactor, reward)) {
            throw new PlayerDoesntHaveRewardInInventoryException();
        }

        PlayerId benefactorId = new PlayerId(benefactor.getUniqueId());
        BountyId id = new BountyId(benefactorId, targetId);
        Bounty bounty = new Bounty(id, reward);

        bountyRepository.add(bounty);
        benefactor.getInventory().removeItem(reward);
    }

    public void selfRemove(Player benefactor, PlayerId targetId) throws BountyDoesntExistsException {

        PlayerId benefactorId = new PlayerId(benefactor.getUniqueId());
        BountyId bountyId = new BountyId(benefactorId, targetId);

        Bounty bounty = bountyRepository.remove(bountyId);

        benefactor.getInventory().addItem(bounty.getReward());
    }

    public void remove(PlayerId benefactorId, PlayerId targetId) throws BountyDoesntExistsException {

        BountyId bountyId = new BountyId(benefactorId, targetId);

        Bounty removedBounty = bountyRepository.remove(bountyId);

        giveItemService.give(benefactorId, removedBounty.getReward());
    }

    public List<Bounty> kill(PlayerId targetId) {
        return bountyRepository.removeByTarget(targetId);
    }

    public List<Refund> getRefundsFor(PlayerId playerId) {
        return refundRepository.removeAll(playerId);
    }

    private boolean playerHasRewardInInventory(Player player, final ItemStack reward) {
        ItemStack[] inventory = player.getInventory().getContents();

        for(ItemStack item : inventory) {
            if (item != null && item.getType() == reward.getType() && item.getAmount() >= reward.getAmount()) {
                return true;
            }
        }

        return false;
    }


}
