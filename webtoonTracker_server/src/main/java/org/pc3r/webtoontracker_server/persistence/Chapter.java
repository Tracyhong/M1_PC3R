package org.pc3r.webtoontracker_server.persistence;

public class Chapter {
    String webtoonId;
    String id;
    String chapter;
    String updatedAt;

    public Chapter(String webtoonId, String id, String chapter, String updatedAt) {
        this.webtoonId = webtoonId;
        this.id = id;
        this.chapter = chapter;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Chapter{id='" + id + "', chapter='" + chapter + "', updatedAt='" + updatedAt + "'}";
    }

    public String getWebtoonId() {
        return webtoonId;
    }

    public String getChapter() {
        return chapter;
    }
}
