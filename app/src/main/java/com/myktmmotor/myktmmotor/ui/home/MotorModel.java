package com.myktmmotor.myktmmotor.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class MotorModel implements Parcelable {

    private String name;
    private String model;
    private String topSpeed;
    private String price;
    private String color;
    private String year;
    private String dp;
    private String spec;
    private String motorId;

    public MotorModel(){}

    protected MotorModel(Parcel in) {
        name = in.readString();
        model = in.readString();
        topSpeed = in.readString();
        price = in.readString();
        color = in.readString();
        year = in.readString();
        dp = in.readString();
        spec = in.readString();
        motorId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(model);
        dest.writeString(topSpeed);
        dest.writeString(price);
        dest.writeString(color);
        dest.writeString(year);
        dest.writeString(dp);
        dest.writeString(spec);
        dest.writeString(motorId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MotorModel> CREATOR = new Creator<MotorModel>() {
        @Override
        public MotorModel createFromParcel(Parcel in) {
            return new MotorModel(in);
        }

        @Override
        public MotorModel[] newArray(int size) {
            return new MotorModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(String topSpeed) {
        this.topSpeed = topSpeed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getMotorId() {
        return motorId;
    }

    public void setMotorId(String motorId) {
        this.motorId = motorId;
    }
}
