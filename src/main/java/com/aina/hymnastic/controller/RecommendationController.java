package com.aina.hymnastic.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aina.hymnastic.constant.ApiPath;
import com.aina.hymnastic.constant.Template;
import com.aina.hymnastic.exception.InvalidSearchException;
import com.aina.hymnastic.service.RecommendArtistService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RecommendationController {

    private final RecommendArtistService recommendArtistService;

    @PostMapping(value = ApiPath.Recommend_ARTISTS, produces = MediaType.TEXT_HTML_VALUE)
    public String recommendArtists(@RequestParam("ArtistName") final String searchQuery, final HttpSession session, final Model model) {
        String token = (String) session.getAttribute("accessToken");
        try {
            // Call the service to get recommended artists based on searchQuery
            Object relatedArtists = recommendArtistService.recommendArtists(token, searchQuery);

            // Add the related artists to the model
            model.addAttribute("relatedArtists", relatedArtists);

            // Redirect to the RecommendArtists page
            return Template.Recommend_ARTISTS;
        } catch (InvalidSearchException exception) {
            return Template.SEARCH_ERROR;
        }
    }
}



