package ca.marioux.easybounty.domain.exception;

public class RefundNotFoundException extends EasyBountyException {
    public RefundNotFoundException() {
        super("Refund not found");
    }
}
