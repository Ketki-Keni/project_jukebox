package com.niit.jdp;

import com.niit.jdp.exception.ArtistNameNotFoundException;
import com.niit.jdp.exception.GenreNotFoundException;
import com.niit.jdp.exception.PlaylistNotFoundException;
import com.niit.jdp.exception.SongNotFoundException;
import com.niit.jdp.model.Playlist;
import com.niit.jdp.model.Song;
import com.niit.jdp.repository.PlaylistRepository;
import com.niit.jdp.repository.SongRepository;
import com.niit.jdp.service.MusicPlayerService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            PlaylistRepository playlistRepository = new PlaylistRepository();
            SongRepository songRepository = new SongRepository();
            MusicPlayerService musicPlayerService = new MusicPlayerService();
            List<Song> allSongs = songRepository.displayAllSongs();
            System.out.println("-------------------------------\nCatalog of Songs in the Jukebox\n-------------------------------");
            System.out.println();
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%s\t%-25s\t%s\t%-20s\t%-20s\t%-10s%n", "ID", "Name", "Duration",
                    "Genre", "Artist Name", "Album");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            allSongs.forEach(System.out::println);
            String songName;
            int option;
            List<Playlist> playlists;
            do {
                System.out.println("\n----------------------------- \n\t\t\tMenu \n-----------------------------" +
                        "\n1 - Search for a song by genre \n2 - Search for a song by Artist's name " +
                        "\n3 - Search for a song by name and play " +
                        "\n4 - Create playlist \n5 - Select a playlist and add songs " +
                        "\n6 - View playlist and select a song to play  \n7 - Catalog Sorted alphabetically based on Song name\n8 - Exit");
                System.out.print("Select an option: ");
                System.out.println();
                option = scanner.nextInt();
                scanner.nextLine();
                try {
                    switch (option) {
                        case 1:
                            //Search for a song by genre
                            System.out.print("Enter the genre: ");
                            String genre = scanner.nextLine();
                            List<Song> songListByGenre = songRepository.displaySongsByGenre(genre);
                            songListByGenre.forEach(System.out::println);
                            System.out.print("Enter the song name to play the song: ");
                            songName = scanner.nextLine();
                            System.out.println(songRepository.getSongByName(songName));
                            System.out.print("Enter 1 - pause, 2 - resume, 3 - stop: ");
                            musicPlayerService.play(songRepository.getSongByName(songName).getSongPath());
                            break;
                        case 2:
                            //Search for a song by Artist's name
                            System.out.print("Enter the name of the artist: ");
                            String artistName = scanner.nextLine();
                            List<Song> songListByArtistName = songRepository.displaySongsByArtistName(artistName);
                            songListByArtistName.forEach(System.out::println);
                            System.out.print("Enter the song name to play the song: ");
                            songName = scanner.nextLine();
                            System.out.println(songRepository.getSongByName(songName));
                            System.out.print("Enter 1 - pause, 2 - resume, 3 - stop: ");
                            musicPlayerService.play(songRepository.getSongByName(songName).getSongPath());
                            break;
                        case 3:
                            //Search for a song by name and play
                            System.out.println("Enter the song name to play the song:");
                            songName = scanner.nextLine();
                            System.out.println(songRepository.getSongByName(songName));
                            System.out.println(songRepository.getSongByName(songName).getSongPath());
                            System.out.println("Enter 1 - pause, 2 - resume, 3 - stop: ");
                            musicPlayerService.play(songRepository.getSongByName(songName).getSongPath());
                            break;
                        case 4:
                            //Create playlist
                            System.out.println("Enter the name of the playlist to be created: ");
                            String playlistName = scanner.nextLine();
                            Playlist playlist = playlistRepository.createPlaylist(playlistName);
                            System.out.println("Your playlist has been created with id: " + playlist.getPlaylistNumber());
                            break;
                        case 5:
                            //Select a playlist and add songs
                            System.out.println("Playlists: \n--------------");
                            System.out.printf("%s\t%-10s%n", "Id", "Name\n--------------");
                            playlists = playlistRepository.displayAllPlaylists();
                            playlists.forEach(System.out::println);
                            System.out.println("Enter the playlist id to add songs to the Playlist: ");
                            int playlistId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Enter the song ids to add to the playlist separated by comma: ");
                            String songIds = scanner.nextLine();
                            boolean songsAdded = playlistRepository.addSong(playlistId, songIds);
                            if (songsAdded) {
                                System.out.println("Songs added to the playlist");
                            } else {
                                System.err.println("Something went wrong! Couldn't add songs to the playlist");
                            }
                            break;
                        case 6:
                            //View playlist and select a song from the playlist to play
                            System.out.println("Playlists: \n--------------");
                            System.out.printf("%s\t%-10s%n", "Id", "Name\n--------------");
                            playlists = playlistRepository.displayAllPlaylists();
                            playlists.forEach(System.out::println);
                            System.out.print("Enter the playlist id to view songs from the playlist: ");
                            int playlistIdToGetSongsFrom = scanner.nextInt();
                            scanner.nextLine();
                            List<Song> songsFromPlaylist = playlistRepository.displayPlaylistSongs(playlistIdToGetSongsFrom);
                            songsFromPlaylist.forEach(System.out::println);
                            System.out.print("Enter the song id to play the song: ");
                            int songId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println(songRepository.getSongBySerialNumber(songId));
                            System.out.println("Enter 1 - pause, 2 - resume, 3 - stop: ");
                            musicPlayerService.play(songRepository.getSongBySerialNumber(songId).getSongPath());
                            break;
                        case 7:
                            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
                            System.out.printf("%s\t%-25s\t%s\t%-20s\t%-20s\t%-10s%n", "ID", "Name", "Duration",
                                    "Genre", "Artist Name", "Album");
                            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
                            List<Song> allSongsSorted = songRepository.displayAllSongs();
                            allSongsSorted.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                            allSongsSorted.forEach(System.out::println);
                            System.out.print("Enter the song id to play the song: ");
                            int songIdToPlay = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println(songRepository.getSongBySerialNumber(songIdToPlay));
                            System.out.println("Enter 1 - pause, 2 - resume, 3 - stop: ");
                            musicPlayerService.play(songRepository.getSongBySerialNumber(songIdToPlay).getSongPath());
                        case 8:
                            break;
                        default:
                            System.out.println("Invalid option selected");
                    }
                } catch (SongNotFoundException | GenreNotFoundException | ArtistNameNotFoundException |
                         PlaylistNotFoundException exception) {
                    System.err.println(exception.getMessage());

                }
            } while (option != 8);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}