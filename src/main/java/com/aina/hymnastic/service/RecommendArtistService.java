package com.aina.hymnastic.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.aina.hymnastic.exception.InvalidSearchException;
import com.aina.hymnastic.utility.SearchQueryFormatter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendArtistService {

    private final RestTemplate restTemplate;
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search?q=SEARCH_QUERY&type=artist&limit=1";
    private static final String RELATED_ARTISTS_URL = "https://api.spotify.com/v1/artists/ARTIST_ID/related-artists";

    public Object recommendArtists(String token, String ArtistName) {

        if (!isValid(token, ArtistName)) {
            throw new InvalidSearchException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Step 1: Search for the artist
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Object> searchResponse = restTemplate.exchange(
                SEARCH_URL.replace("SEARCH_QUERY", SearchQueryFormatter.format(ArtistName)), HttpMethod.GET, entity, Object.class);
        LinkedHashMap searchResult = (LinkedHashMap) searchResponse.getBody();

        LinkedHashMap artists = (LinkedHashMap) searchResult.get("artists");
        ArrayList itemsArtists = (ArrayList) artists.get("items");

        if (itemsArtists.isEmpty()) {
            throw new InvalidSearchException();
        }

        // Step 2: Get the artist ID
        String artistId = (String) ((LinkedHashMap) itemsArtists.get(0)).get("id");

        // Step 3: Get related artists
        ResponseEntity<Object> relatedArtistsResponse = restTemplate.exchange(
                RELATED_ARTISTS_URL.replace("ARTIST_ID", artistId), HttpMethod.GET, entity, Object.class);
        LinkedHashMap relatedArtistsResult = (LinkedHashMap) relatedArtistsResponse.getBody();

        return relatedArtistsResult;
    }

    public boolean isValid(String token, String searchQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                SEARCH_URL.replace("SEARCH_QUERY", SearchQueryFormatter.format(searchQuery)), HttpMethod.GET, entity, Object.class);
        LinkedHashMap result = (LinkedHashMap) response.getBody();

        LinkedHashMap artists = (LinkedHashMap) result.get("artists");
        ArrayList itemsArtists = (ArrayList) artists.get("items");

        if (itemsArtists.isEmpty()) {
            return false;
        }

        return true;
    }
}