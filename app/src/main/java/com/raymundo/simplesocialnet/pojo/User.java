package com.raymundo.simplesocialnet.pojo;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class User implements Parcelable {

    private UUID uuid;
    private String name;
    private Drawable image;

    public User(String name, Drawable image) {
        uuid = UUID.randomUUID();
        this.name = name;
        this.image = image;
    }

    protected User(Parcel in) {
        name = in.readString();
        image = (Drawable) in.readValue(Drawable.class.getClassLoader());
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

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(image);
    }
}
