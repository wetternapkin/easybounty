package ca.marioux.easybounty.domain.exception;

public class BountyDoesntExistsException extends EasyBountyException {
    public BountyDoesntExistsException() {
        super("No bounty has been found with the specified names");
    }
}
