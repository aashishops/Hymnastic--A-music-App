package com.aina.hymnastic.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.aina.hymnastic.constant.Template;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/stats")
	public String showStatPage() {
		return Template.STATS;
	}

    @GetMapping("/recommend")
	public String showRecommendPage() {
		return Template.RECOMMEND;
	}

    @GetMapping("/playlist")
	public String showPlaylistPage() {
		return Template.PLAYLIST;
	}

}
