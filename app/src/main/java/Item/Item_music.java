package Item;

import java.io.Serializable;

public class Item_music implements Serializable {
    private String music_name;
    private String music_singer;
    private String music_album;
    private String music_url;
    private int music_count;
    private long music_id,playlist_id;

    public Item_music(String music_name,String music_singer,String music_album,int music_count,long music_id,String music_url,long playlist_id){
        this.music_name=music_name;
        this.music_singer=music_singer;
        this.music_album=music_album;
        this.music_count=music_count;
        this.music_id=music_id;
        this.music_url=music_url;
        this.playlist_id=playlist_id;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public String getMusic_name() {
        return music_name;
    }

    public String getMusic_singer() {
        return music_singer;
    }

    public String getMusic_album() {
        return music_album;
    }

    public int getMusic_count() {
        return music_count;
    }

    public long getMusic_id() {
        return music_id;
    }

    public String getMusic_url() {
        return music_url;
    }
}
