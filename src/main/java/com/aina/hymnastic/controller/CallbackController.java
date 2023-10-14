package com.aina.hymnastic.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aina.hymnastic.constant.ApiPath;
import com.aina.hymnastic.constant.Template;
import com.aina.hymnastic.exception.NoTrackPlayingException;
import com.aina.hymnastic.service.AccessTokenService;
import com.aina.hymnastic.service.CurrentPlayingService;
import com.aina.hymnastic.service.ProfileDetailService;
import com.aina.hymnastic.service.SpotifyUrlService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CallbackController {

	private final SpotifyUrlService spotifyUrlService;
	private final AccessTokenService accessToken;
	private final ProfileDetailService userDetails;
	private final CurrentPlayingService currentPlaying;

	@GetMapping(value = ApiPath.CALLBACK, produces = MediaType.TEXT_HTML_VALUE)
	public String handleCallback(@RequestParam(value = "code", required = false) final String code,
			@RequestParam(value = "error", required = false) final String error, final Model model,
			final HttpSession session) {

		if (error != null) {
			model.addAttribute("url", spotifyUrlService.getAuthorizationURL());
			return Template.CALLBACK_FAILURE;
		}
		session.setAttribute("code", code);
		String token = accessToken.getToken(code);

		session.setAttribute("accessToken", token);
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
