package com.aina.hymnastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.aina.hymnastic.constant.Template;

import org.springframework.jdbc.core.RowMapper;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisplayArtistService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static class Artist {
        private String artist;

        // Constructors, getters, and setters

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
    }

    public List<Artist> getAllArtists(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM artist LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new ArtistRowMapper(), pageSize, offset);
    }

    public List<Artist> searchArtists(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM artist WHERE artist LIKE ? LIMIT ? OFFSET ?";
        keyword = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new ArtistRowMapper(), keyword, pageSize, offset);
    }

    public Optional<Integer> getTotalArtistCount() {
        String sql = "SELECT COUNT(*) FROM artist";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class));
    }

    public int getSearchResultCount(String keyword) {
        String sql = "SELECT COUNT(*) FROM artist WHERE artist LIKE ?";
        keyword = "%" + keyword + "%";
        return jdbcTemplate.queryForObject(sql, Integer.class, keyword);
    }

    private static class ArtistRowMapper implements RowMapper<Artist> {
        @Override
        public Artist mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Artist artist = new Artist();
            artist.setArtist(resultSet.getString("artist"));
            return artist;
        }
    }
    
}
