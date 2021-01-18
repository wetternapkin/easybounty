package ca.marioux.easybounty.application.exception;

import ca.marioux.easybounty.domain.exception.EasyBountyException;

public class BenefactorAndTargetAreSameException extends EasyBountyException {
    public BenefactorAndTargetAreSameException() {
        super("You can't place a bounty on yourself");
    }
}
