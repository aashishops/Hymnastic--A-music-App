package com.aina.hymnastic.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.aina.hymnastic.constant.ApiPath;
import com.aina.hymnastic.constant.Template;
import com.aina.hymnastic.exception.NoTrackPlayingException;
import com.aina.hymnastic.service.CurrentPlayingService;
import com.aina.hymnastic.service.ProfileDetailService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RedirectController {

	private final ProfileDetailService userDetails;
	private final CurrentPlayingService currentPlaying;

	@GetMapping(value = ApiPath.REDIRECT, produces = MediaType.TEXT_HTML_VALUE)
	public String redirectToCallbackSuccess(final HttpSession session, final Model model) {

		String token = (String) session.getAttribute("accessToken");
		model.addAttribute("accessToken", token);
		model.addAttribute("userName", userDetails.getUsername(token));

		try {
			model.addAttribute("currentPlaying", currentPlaying.getCurrentPlaying(token));
			model.addAttribute("display", 1);
		} catch (NoTrackPlayingException exception) {
			model.addAttribute("display", 0);
		}
		return Template.CALLBACK_SUCCESS;
	}

}
