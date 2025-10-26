package base.api.service.impl;

import base.api.model.Membership;
import base.api.repository.MembershipRepository;
import base.api.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipServiceImpl implements MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    @Override
    public Optional<Membership> findById(int id) {
        return membershipRepository.findById(id);
    }

    @Override
    public Membership save(Membership membership) {
        return membershipRepository.save(membership);
    }

    @Override
    public void deleteById(int id) {
        membershipRepository.deleteById(id);
    }
}