/*
 * Author : Ketki Keni
 * Date : 30-11-2022
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.jdp.repository;

import com.niit.jdp.exception.PlaylistNotFoundException;
import com.niit.jdp.exception.SongNotFoundException;
import com.niit.jdp.model.Playlist;
import com.niit.jdp.model.Song;
import com.niit.jdp.service.DatabaseService;
import com.niit.jdp.service.MusicPlayerService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository {
    Connection connection;
    DatabaseService databaseService;
    MusicPlayerService musicPlayerService;

    public PlaylistRepository() throws SQLException {
        databaseService = new DatabaseService();
        connection = databaseService.getConnection();
        musicPlayerService = new MusicPlayerService();
    }

    /**
     * This method returns all playlist records from the database
     *
     * @return All playlist records
     */
    public List<Playlist> displayAllPlaylists() throws SQLException {
        List<Playlist> playlistDetails = new ArrayList<>();
        String selectQuery = "SELECT `Playlist_number`,`name` FROM `jukebox`.`playlist`;";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int playlistId = resultSet.getInt("Playlist_number");
                String playlistName = resultSet.getString("name");
                Playlist playlist = new Playlist(playlistId, playlistName);
                playlistDetails.add(playlist);
            }
        }
        return playlistDetails;
    }

    /**
     * This method returns all songs from the playlist from the database based on entered playlist ID
     * Throws SongNotFoundException when Song ID is not present in the database table, PlaylistNotFoundException
     *
     * @param playlistId - Playlist ID
     * @return all songs from the playlist
     * @throws SongNotFoundException     - Throws SongNotFoundException when Song ID is not present in the database table
     * @throws PlaylistNotFoundException - PlaylistNotFoundException when Playlist ID is not present in the database table
     */
    public List<Song> displayPlaylistSongs(int playlistId) throws SQLException, SongNotFoundException, PlaylistNotFoundException {
        List<Song> songList = new ArrayList<>();
        String query = "SELECT * FROM `jukebox`.`playlist` WHERE `Playlist_number` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, playlistId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                String songId = resultSet.getString("song_id");
                String[] songsFromPlaylist = songId.split(",");
                for (String songIds : songsFromPlaylist) {
                    Song song = new SongRepository().getSongBySerialNumber(Integer.parseInt(songIds));
                    songList.add(song);
                }
            }
        } else {
            throw new PlaylistNotFoundException("Playlist not found! Please enter correct Playlist Id.");
        }
        return songList;
    }

    /**
     * This method creates a playlist when playlist name is entered.
     *
     * @param playlistName - Name of the Playlist
     * @return playlist record that is created
     */
    public Playlist createPlaylist(String playlistName) {
        Playlist playlist = new Playlist();
        String insertQuery = "INSERT INTO `jukebox`.`playlist` (`name`) VALUES (?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, playlistName);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    playlist.setPlaylistNumber(generatedKeys.getInt(1));
                    playlist.setName(playlistName);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playlist;
    }

    /**
     * This method adds songs in the playlist based on entered playlist ID
     *
     * @param playlistId - Playlist ID
     * @param songIds    - Song ID of all songs from the playlist
     * @return boolean value that indicates if the song is added or not
     */
    public boolean addSong(int playlistId, String songIds) throws SQLException {
        String updateQuery = "UPDATE `jukebox`.`playlist` SET `song_id` = ? WHERE `Playlist_number` = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, songIds);
        preparedStatement.setInt(2, playlistId);
        int result = preparedStatement.executeUpdate();
        return result > 0;
    }
}
