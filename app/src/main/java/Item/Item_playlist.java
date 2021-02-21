package Item;

public class Item_playlist {
    private String name_playlist;
    private String url_playlist;
    private int count_music;
    private long playlist_id;
    public Item_playlist(String name_playlist, String url_playlist, int count_music,long playlist_id){
           this.name_playlist=name_playlist;
           this.url_playlist=url_playlist;
           this.count_music=count_music;
           this.playlist_id=playlist_id;
    }

    public String getName_playlist() {
        return name_playlist;
    }

    public String getUrl_playlist() {
        return url_playlist;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public int getCount_music() {
        return count_music;
    }
}
