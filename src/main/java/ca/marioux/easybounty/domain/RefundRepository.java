package ca.marioux.easybounty.domain;

import ca.marioux.easybounty.domain.exception.RefundNotFoundException;

import java.util.List;

public interface RefundRepository {
    void add(Refund refund);
    List<Refund> removeAll(PlayerId target);
}
