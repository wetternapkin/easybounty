package ca.marioux.easybounty.application.exception;

import ca.marioux.easybounty.domain.exception.EasyBountyException;

public class PlayerDoesntHaveRewardInInventoryException extends EasyBountyException {

    public PlayerDoesntHaveRewardInInventoryException() {
        super("You need the reward in your inventory to place a bounty");
    }
}
