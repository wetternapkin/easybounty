package ca.marioux.easybounty.domain;

import org.bukkit.entity.Player;

import java.util.UUID;

public class BountyId {

    private final PlayerId benefactor;
    private final PlayerId target;

    public BountyId(PlayerId benefactor, PlayerId target) {
        this.benefactor = benefactor;
        this.target = target;
    }

    public PlayerId getBenefactor() {
        return benefactor;
    }

    public PlayerId getTarget() {
        return target;
    }
}
