package ca.marioux.easybounty.domain.exception;

public class BountyAlreadyExistsException extends EasyBountyException {

    public BountyAlreadyExistsException() {
        super("A bounty already exists for this player");
    }
}
