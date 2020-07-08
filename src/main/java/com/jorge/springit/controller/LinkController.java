package com.jorge.springit.controller;

import com.jorge.springit.model.Comment;
import com.jorge.springit.model.Link;
import com.jorge.springit.repository.CommentRepository;
import com.jorge.springit.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private LinkService linkService;
    private CommentRepository commentRepository;

    public LinkController(LinkService linkService, CommentRepository commentRepository){
        this.linkService = linkService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("links", linkService.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id, Model model){
        Optional<Link> optionalLink = linkService.findById(id);

        if(optionalLink.isPresent()){
            Link link = optionalLink.get();
            Comment comment = new Comment();
            comment.setLink(link);
            model.addAttribute("comment", comment);
            model.addAttribute("link",link);
            model.addAttribute("success",model.containsAttribute("success"));
            return"link/view";
        }
        else{
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            logger.info("Validation errors were found while submitting a new link.");
            model.addAttribute("link", link);
            return "/link/submit";
        } else {
            // save link
            linkService.save(link);
            logger.info("New link was saved successfully.");
            redirectAttributes
                    .addAttribute("id", link.getId())
                    .addFlashAttribute("success", true);
            return "redirect:/link/{id}";
        }
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/link/{linkID}/addComment")
    public String addComment(@Valid Comment comment, @PathVariable Long linkID, BindingResult bindingResult) {
        if( bindingResult.hasErrors() ) {
            logger.info("There was a problem adding a new comment.");
        } else {
            Optional<Link> optlink = linkService.findById(linkID);
            if(optlink.isPresent()){
                comment.setLink(optlink.get());
                commentRepository.save(comment);
                logger.info("New comment saved successfully.");
            } else {
                logger.info("Invalid link id = "+linkID);
            }
        }

        return "redirect:/link/"+linkID;
    }
}
