package org.pc3r.webtoontracker_server.persistence;

public class Tag {

    private String id;
    private String name;

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
