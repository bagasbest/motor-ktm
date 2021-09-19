package com.myktmmotor.myktmmotor.ui.rating;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingModel implements Parcelable {

    private double stars;
    private String comment;
    private String uid;

    public RatingModel(){}

    protected RatingModel(Parcel in) {
        stars = in.readDouble();
        comment = in.readString();
        uid = in.readString();
    }

    public static final Creator<RatingModel> CREATOR = new Creator<RatingModel>() {
        @Override
        public RatingModel createFromParcel(Parcel in) {
            return new RatingModel(in);
        }

        @Override
        public RatingModel[] newArray(int size) {
            return new RatingModel[size];
        }
    };

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(stars);
        parcel.writeString(comment);
        parcel.writeString(uid);
    }
}

