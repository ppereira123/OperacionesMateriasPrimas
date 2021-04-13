package com.example.operacionesmteriasprimas.Modelos;

public class UsersData {
    boolean admin;
    String id;
    String name;
    String photo;
    String email;

    public UsersData(boolean admin, String id, String name, String photo, String email) {
        this.admin = admin;
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
