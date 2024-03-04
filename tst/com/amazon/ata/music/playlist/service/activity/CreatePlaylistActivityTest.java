package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.AssertJUnit.assertNull;

public class CreatePlaylistActivityTest {
    @Mock
    private PlaylistDao playlistDao;

    private CreatePlaylistActivity createPlaylistActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        createPlaylistActivity = new CreatePlaylistActivity(playlistDao);
    }

    @Test
    public void handleRequest_savedPlaylist_returnsPlaylistModelInResult() {
        // GIVEN - Create Playlist Request have 3 parameters
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        List<String> expectedTags = Lists.newArrayList("tag");

        int expectedSongCount = 0;
        List<AlbumTrack> expectedSongList = new ArrayList<>();

        // playlist to save
        Playlist playlist = new Playlist();
        playlist.setName(expectedName);
        playlist.setCustomerId(expectedCustomerId);
        playlist.setTags(Sets.newHashSet(expectedTags));
        playlist.setSongCount(expectedSongCount);
        playlist.setSongList(expectedSongList);

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
            .withCustomerId(expectedCustomerId)
            .withName(expectedName)
            .withTags(expectedTags)
            .build();

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request, null);

        // THEN - with the randomly generated id
        String expectedId = result.getPlaylist().getId();
        assertTrue(StringUtils.isAlphanumeric(expectedId));
        playlist.setId(expectedId);

        verify(playlistDao).savePlaylist(playlist);

        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedSongCount, result.getPlaylist().getSongCount());
        assertEquals(expectedTags, result.getPlaylist().getTags());
    }

    @Test
    public void handleRequest_savedPlaylist_returnsTagsNull() {
        // GIVEN - empty lists are sent in as null
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        List<String> expectedTags = null;

        int expectedSongCount = 0;
        List<AlbumTrack> expectedSongList = new ArrayList<>();

        // playlist to save
        Playlist playlist = new Playlist();
        playlist.setName(expectedName);
        playlist.setCustomerId(expectedCustomerId);
        playlist.setTags(null);
        playlist.setSongCount(expectedSongCount);
        playlist.setSongList(expectedSongList);

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(expectedCustomerId)
                .withName(expectedName)
                .withTags(expectedTags)
                .build();

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request, null);

        // THEN - with the randomly generated id
        String expectedId = result.getPlaylist().getId();
        assertTrue(StringUtils.isAlphanumeric(expectedId));
        playlist.setId(expectedId);

        verify(playlistDao).savePlaylist(playlist);

        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedSongCount, result.getPlaylist().getSongCount());
        assertNull(result.getPlaylist().getTags());
    }

    @Test
    public void handleRequest_savedPlaylist_throwsInvalidAttributeValueExceptionCustomerName() {
        // GIVEN - Create Playlist Request have 3 parameters
        String expectedName = "expected'Name";
        String expectedCustomerId = "expectedCustomerId";
        List<String> expectedTags = Lists.newArrayList("tag");

        int expectedSongCount = 0;
        List<AlbumTrack> expectedSongList = new ArrayList<>();

        // playlist to save
        Playlist playlist = new Playlist();
        playlist.setName(expectedName);
        playlist.setCustomerId(expectedCustomerId);
        playlist.setTags(Sets.newHashSet(expectedTags));
        playlist.setSongCount(expectedSongCount);
        playlist.setSongList(expectedSongList);

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(expectedCustomerId)
                .withName(expectedName)
                .withTags(expectedTags)
                .build();

        // WHEN
        // THEN
        assertThrows(InvalidAttributeValueException.class,
                () -> createPlaylistActivity.handleRequest(request, null));
    }

    @Test
    public void handleRequest_savedPlaylist_throwsInvalidAttributeValueExceptionCustomerId() {
        // GIVEN - Create Playlist Request have 3 parameters
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId\\";
        List<String> expectedTags = Lists.newArrayList("tag");

        int expectedSongCount = 0;
        List<AlbumTrack> expectedSongList = new ArrayList<>();

        // playlist to save
        Playlist playlist = new Playlist();
        playlist.setName(expectedName);
        playlist.setCustomerId(expectedCustomerId);
        playlist.setTags(Sets.newHashSet(expectedTags));
        playlist.setSongCount(expectedSongCount);
        playlist.setSongList(expectedSongList);

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(expectedCustomerId)
                .withName(expectedName)
                .withTags(expectedTags)
                .build();

        // WHEN
        // THEN
        assertThrows(InvalidAttributeValueException.class,
                () -> createPlaylistActivity.handleRequest(request, null));
    }
}
