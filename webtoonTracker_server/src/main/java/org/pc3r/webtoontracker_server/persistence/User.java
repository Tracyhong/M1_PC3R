package org.pc3r.webtoontracker_server.persistence;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private ArrayList<String> webtoonList;

    public User(String id, String name, String email, String password, ArrayList<String> webtoonList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.webtoonList = webtoonList;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public ArrayList<String> getWebtoonList() {
        return webtoonList;
    }
    public boolean exists(String name, String password) {
        return this.name.equals(name) && this.password.equals(password);
    }

    public void addWebtoon(String webtoonId) {
        this.webtoonList.add(webtoonId);
    }
    public void removeWebtoon(String webtoonId) {
        this.webtoonList.remove(webtoonId);
    }

    public String getWebtoons() {
        return this.webtoonList.toString();
    }
}
