package com.jorge.springit.controller;

import com.jorge.springit.model.Link;
import com.jorge.springit.model.Vote;
import com.jorge.springit.service.LinkService;
import com.jorge.springit.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final LinkService linkService;

    @Secured({"ROLE_USER"})
    @GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long linkID, @PathVariable short direction, @PathVariable int voteCount){

        Optional<Link> optionalLink = linkService.findById(linkID);

        if(optionalLink.isPresent()){
            Link link = optionalLink.get();
            Vote vote = new Vote(direction, link);

            voteService.save(vote);

            int updatedVoteCount = voteCount + direction;
            link.setVoteCount(updatedVoteCount);
            linkService.save(link);

            return updatedVoteCount;

        }
        return voteCount;
    }
}
