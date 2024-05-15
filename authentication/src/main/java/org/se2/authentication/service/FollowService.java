package org.se2.authentication.service;

import org.se2.authentication.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public int getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    public int getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}