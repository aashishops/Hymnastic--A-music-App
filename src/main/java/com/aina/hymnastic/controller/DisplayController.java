package com.aina.hymnastic.controller;

import com.aina.hymnastic.constant.ApiPath;
import com.aina.hymnastic.constant.Template;
import com.aina.hymnastic.service.DisplayArtistService;
import com.aina.hymnastic.service.DisplaySongService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class DisplayController {

    private final DisplaySongService displaySongService;
    private final DisplayArtistService displayArtistService;

    @GetMapping(value = ApiPath.SEARCH_TRACKS, produces = MediaType.TEXT_HTML_VALUE)
    public String displaySongsHandler(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        List<DisplaySongService.Song> songs;

        if (keyword != null && !keyword.isEmpty()) {
            songs = displaySongService.searchSongs(keyword, page, pageSize);
        } else {
            songs = displaySongService.getAllSongs(page, pageSize);
        }

        int totalSongs = displaySongService.getTotalSongCount().orElse(0);
        int totalPages = (int) Math.ceil((double) totalSongs / pageSize);

        model.addAttribute("songs", songs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);

        return Template.SEARCH_TRACKS;
    }

    @GetMapping(value = ApiPath.SEARCH_ARTISTS, produces = MediaType.TEXT_HTML_VALUE)
    public String displayArtistsHandler(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "30") int pageSize,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        List<DisplayArtistService.Artist> artists;

        if (keyword != null && !keyword.isEmpty()) {
            artists = displayArtistService.searchArtists(keyword, page, pageSize);
        } else {
            artists = displayArtistService.getAllArtists(page, pageSize);
        }

        int totalArtists = displayArtistService.getTotalArtistCount().orElse(0);
        int totalArtistPages = (int) Math.ceil((double) totalArtists / pageSize);

        model.addAttribute("artists", artists);
        model.addAttribute("currentArtistPage", page);
        model.addAttribute("totalArtistPages", totalArtistPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);

        return Template.SEARCH_ARTISTS;
    }
    
    @GetMapping(value = ApiPath.SEARCH_TRACKS_PLAYLIST, produces = MediaType.TEXT_HTML_VALUE)
    public String displaySongsforPlaylistHandler(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        List<DisplaySongService.Song> songs;

        if (keyword != null && !keyword.isEmpty()) {
            songs = displaySongService.searchSongs(keyword, page, pageSize);
        } else {
            songs = displaySongService.getAllSongs(page, pageSize);
        }

        int totalSongs = displaySongService.getTotalSongCount().orElse(0);
        int totalPages = (int) Math.ceil((double) totalSongs / pageSize);

        model.addAttribute("songs", songs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);

        return Template.SEARCH_TRACKS_PLAYLIST;
    }
    
}
