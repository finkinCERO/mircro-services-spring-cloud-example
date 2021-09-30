package htw.kbe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;

//import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
//import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
//@JsonIgnoreProperties(value="addedSongs")
@Table(name="song")
public class Song {

    @Override
    public String toString() {
        return "Song [songId=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", released="
                + released + "]";
    }
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "artist", length = 100)
    private String artist;
    @Column(name = "album", length = 100)
    private String album;
    @Column(name = "released")
    private Integer released;

    @Column(name = "picture")
    private String picture;
    @JsonIgnore
    @JsonIgnoreProperties({"songList"})
    @ManyToMany(
            cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
    @JoinTable(name = "songlists_songs",
            joinColumns =
                    {@JoinColumn(name = "songId", referencedColumnName = "id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "songListId", nullable = false, referencedColumnName = "id", updatable=false)})
    private List<SongList> songList = new ArrayList<>();

    private Song(Builder builder) {
        this.id = builder.songId;
        this.title = builder.title;
        this.artist = builder.artist;
        this.album = builder.album;
        this.released = builder.released;
    }
    public Song(Integer songId, String title, String artist, String album, Integer released) {
        this.id = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.released = released;
//        this.songs = songs;
    }

    public Song() {
    }


    public Integer getSongId() {
        return id;
    }

    public void setSongId(Integer songId) {
        this.id = songId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum (String album) {
        this.album = album;
    }

    public Integer getReleased() {
        return released;
    }

    public void setReleased(Integer released) {
        this.released=released;
    }
    /**
     * Creates builder to build {@link Song}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Builder to build {@link Song}.
     */
    public static final class Builder {
        private Integer songId;
        private String title;
        private String artist;
        private String album;
        private Integer released;

        private Builder() {
        }

        public Song.Builder withId(Integer songId) {
            this.songId = songId;
            return this;
        }

        public Song.Builder withTitle(String title) {
            this.title =title;
            return this;
        }

        public Song.Builder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Song.Builder withAlbum(String album) {
            this.album = album;
            return this;
        }

        public Song.Builder withReleased(Integer released){
            this.released = released;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }

    //	public void setSongList(SongList songList) {
//		this.songList = songList;
//	}
//
//	public SongList getSongList() {
//		return this.songList;
//	}
    public void setSongList(List<SongList> songList) {
        this.songList = songList;
    }
    public List<SongList> getSongList() {
        return songList;
    }
}
