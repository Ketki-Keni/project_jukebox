/*
 * Author : Ketki Keni
 * Date : 03-12-2022
 * Created with : IntelliJ IDEA Community Edition
 */

import com.niit.jdp.exception.GenreNotFoundException;
import com.niit.jdp.exception.PlaylistNotFoundException;
import com.niit.jdp.exception.SongNotFoundException;
import com.niit.jdp.model.Playlist;
import com.niit.jdp.model.Song;
import com.niit.jdp.repository.PlaylistRepository;
import com.niit.jdp.service.DatabaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistRepositoryTest {
    PlaylistRepository playlistRepository;
    DatabaseService databaseService;
    Connection connection;

    @Before
    public void setUp() throws SQLException {
        databaseService = new DatabaseService();
        connection = databaseService.getConnection();
        playlistRepository = new PlaylistRepository();
    }

    @After
    public void tearDown() {
        databaseService = null;
        playlistRepository = null;
        connection = null;
    }

    @Test
    public void givenPlaylistDatabaseThenPlaylistRecords() throws SQLException {
        List<Playlist> playlists = playlistRepository.displayAllPlaylists();
        assertEquals(1, playlists.get(0).getPlaylistNumber());
    }

    @Test
    public void givenPlaylistDatabaseThenPlaylistRecordsNotEquals() throws SQLException {
        List<Playlist> playlists = playlistRepository.displayAllPlaylists();
        assertNotEquals(2, playlists.get(0).getPlaylistNumber());
    }

    @Test
    public void givenPlaylistDatabaseThenReturnSongRecordsFromPlaylist() throws SQLException, SongNotFoundException, PlaylistNotFoundException, GenreNotFoundException {
        List<Song> playlists = playlistRepository.displayPlaylistSongs(1);
        assertEquals(1, playlists.get(0).getSerialNumber());
    }

    @Test
    public void givenPlaylistDatabaseThenReturnSongRecordsFromPlaylistNotEquals() throws SQLException, SongNotFoundException, PlaylistNotFoundException, GenreNotFoundException {
        List<Song> playlists = playlistRepository.displayPlaylistSongs(1);
        assertNotEquals(5, playlists.get(0).getSerialNumber());
    }

    @Test
    public void givenPlaylistNameThenCreatePlaylist() {
        Playlist playlist = playlistRepository.createPlaylist("Rock");
        assertEquals("Rock", playlist.getName());
    }

    @Test
    public void givenPlaylistNameThenCreatePlaylistNotEquals() {
        Playlist playlist = playlistRepository.createPlaylist("Rock");
        assertNotEquals("Pop", playlist.getName());
    }

    @Test
    public void givenSongIdsThenAddSongsToPlaylist() throws SQLException, PlaylistNotFoundException {
        boolean addSong = playlistRepository.addSong(1, "1,5,8,9");
        assertTrue(addSong);
    }

    @Test
    public void givenSongIdsThenAddSongsToPlaylistNotEquals() throws SQLException, PlaylistNotFoundException {
        boolean addSong = playlistRepository.addSong(1, "1,5,8,9");
        assertNotEquals(false, addSong);
    }
}
