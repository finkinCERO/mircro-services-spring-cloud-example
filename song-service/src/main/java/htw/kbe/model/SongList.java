package htw.kbe.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
//@JsonIgnoreProperties(value="songList")
@Table(name="songlist")
public class SongList  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable =false, name="id",unique=true)
    private Integer id;
    @Column(length=50, nullable = false)
    private String name;
    @JsonProperty(value="isPrivate")
    private Boolean isPrivate;


//	private DAOUser owner;

    @JoinColumn(name="owner", nullable=true)
    private String owner;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})	//WAS EAGER, WAS CASCADEtYPE.MERGE
    @JoinTable(name = "songlists_songs",
            joinColumns =
                    {@JoinColumn(name = "songListId", referencedColumnName = "id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "songId", nullable = false, referencedColumnName = "id", updatable=false)})
    private List<Song> songList = new ArrayList<>();

    public SongList() {}

    public SongList(String name, Boolean isPrivate, String user){//, DAOUser user, List<Song> songs) {
//			this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.owner = user;
//		this.songList = songs;
    }
    public SongList(String name, Boolean isPrivate, String user, List<Song> songList){//, DAOUser user, List<Song> songs) {
//			this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.owner = user;
        this.songList = songList;
    }

    public SongList(Integer id, String name, Boolean isPrivate, String user){
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.owner = user;
        //	this.songList = songs;
    }
    @JsonProperty(value = "isPrivate")
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    @JsonProperty(value = "isPrivate")
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    //
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String songs = "";
        for(Song s : this.songList){
            songs += s.toString()+", ";
        }
        return "SongList [id=" + id + ", name=" + name + ", isPrivate="+isPrivate+", owner="+owner+", songs="+songList.toString()+"]";
    }
//	@Transient
//	public void addSong(Song song) {
//	    song.setSongList(this);
//	    songs.add(song);
//	}


    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

}
