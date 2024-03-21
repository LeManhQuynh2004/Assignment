package com.quynhlm.dev.assignment_and103.Model;

public class Student {
    private String _id;
    private String id_Sv;
    private String name;
    private String email;
    private String avatar;
    private float score;
    private String status;

    public Student() {

    }

    public Student(String _id, String id_Sv, String name, String email, String avatar, float score, String status) {
        this._id = _id;
        this.id_Sv = id_Sv;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.score = score;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_Sv() {
        return id_Sv;
    }

    public void setId_Sv(String id_Sv) {
        this.id_Sv = id_Sv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
