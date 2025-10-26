package base.api.service;

import base.api.model.Membership;

import java.util.List;
import java.util.Optional;

public interface MembershipService {
    List<Membership> findAll();
    Optional<Membership> findById(int id);
    Membership save(Membership membership);
    void deleteById(int id);
}