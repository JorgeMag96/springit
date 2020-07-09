package com.jorge.springit.service;

import com.jorge.springit.model.Vote;
import com.jorge.springit.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public Vote save(Vote vote){
        return voteRepository.save(vote);
    }
}
