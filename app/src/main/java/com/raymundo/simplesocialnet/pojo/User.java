package com.raymundo.simplesocialnet.pojo;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String name;
    private String email;
    private String nickname;
    private String birthday;
    private String city;
    private Drawable image;

    public User() {
    }

    public User(String name, Drawable image, String email, String nickname, String city, String birthday) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.nickname = nickname;
        this.city = city;
        this.birthday = birthday;
    }

    protected User(Parcel in) {
        name = in.readString();
        image = (Drawable) in.readValue(Drawable.class.getClassLoader());
        email = in.readString();
        nickname = in.readString();
        city = in.readString();
        birthday = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User getNullUser() {
        return new User(null, null, null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(image);
        dest.writeString(email);
        dest.writeString(nickname);
        dest.writeString(city);
        dest.writeString(birthday);
    }
}
