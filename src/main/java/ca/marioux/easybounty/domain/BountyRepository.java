package ca.marioux.easybounty.domain;

import ca.marioux.easybounty.domain.exception.BountyAlreadyExistsException;
import ca.marioux.easybounty.domain.exception.BountyDoesntExistsException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BountyRepository {
    void add(Bounty bounty) throws BountyAlreadyExistsException;
    Bounty remove(BountyId bountyId) throws BountyDoesntExistsException;
    List<Bounty> removeByTarget(PlayerId targetId);
}
