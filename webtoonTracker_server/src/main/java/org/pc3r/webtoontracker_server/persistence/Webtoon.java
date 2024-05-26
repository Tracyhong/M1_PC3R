package org.pc3r.webtoontracker_server.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Webtoon {
    String id;
    String title;
    List<String> alternativeTitles;
    List<String> author;
    String artist;
    String description;
    int year;
    String status;
    List<Tag> tags;
    Date lastUpdate;
    String image;

    public Webtoon(String id, String title, List<String> alternativeTitles, List<String> author, String artist, String description, int year, String status, List<Tag> tags, Date lastUpdate, String image) {
        this.id = id;
        this.title = title;
        this.alternativeTitles = alternativeTitles;
        this.author = author;
        this.artist = artist;
        this.description = description;
        this.year = year;
        this.status = status;
        this.tags = tags;
        this.lastUpdate = lastUpdate;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Webtoon{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", alternativeTitles=" + alternativeTitles +
                ", author=" + author +
                ", artist='" + artist + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", status='" + status + '\'' +
                ", tags=" + tags +
                ", lastUpdate=" + lastUpdate +
                ", image='" + image + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
