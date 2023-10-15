package com.aina.hymnastic.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import com.aina.hymnastic.constant.Template;



@Controller
public class SearchArtistController {

    @GetMapping("/searchArtists")
    public String showSearchArtistsPage() {
        return Template.SEARCH_ARTISTS;
    }
}
