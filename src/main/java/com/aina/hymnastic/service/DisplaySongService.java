package com.aina.hymnastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DisplaySongService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static class Song {
        private String artist;
        private String albumName;
        private String trackName;

        // Constructors, getters, and setters

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }
    }

    public List<Song> getAllSongs(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM songs LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new SongRowMapper(), pageSize, offset);
    }
    
    public List<Song> searchSongs(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM songs WHERE artists LIKE ? OR album_name LIKE ? OR track_name LIKE ? LIMIT ? OFFSET ?";
        keyword = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new SongRowMapper(), keyword, keyword, keyword, pageSize, offset);
    }
    
    
    

    public Optional<Integer> getTotalSongCount() {
        String sql = "SELECT COUNT(*) FROM songs";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class));
    }
    

    public int getSearchResultCount(String keyword) {
        String sql = "SELECT COUNT(*) FROM songs WHERE artist LIKE ? OR album_name LIKE ? OR track_name LIKE ?";
        keyword = "%" + keyword + "%";
        return jdbcTemplate.queryForObject(sql, Integer.class, keyword, keyword, keyword);
    }
    

    private static class SongRowMapper implements RowMapper<Song> {
        @Override
        public Song mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Song song = new Song();
            song.setArtist(resultSet.getString("artists"));
            song.setAlbumName(resultSet.getString("album_name"));
            song.setTrackName(resultSet.getString("track_name"));
            return song;
        }
    }
}
