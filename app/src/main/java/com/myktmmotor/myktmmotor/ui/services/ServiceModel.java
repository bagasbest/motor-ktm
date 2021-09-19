package com.myktmmotor.myktmmotor.ui.services;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceModel implements Parcelable {

    private String name;
    private String address;
    private String dateTime;
    private String merk;
    private String phone;
    private String kendala;
    private String serviceId;
    private String status;
    private String serviceDate;
    private String uid;

    public ServiceModel(){}

    protected ServiceModel(Parcel in) {
        name = in.readString();
        address = in.readString();
        dateTime = in.readString();
        merk = in.readString();
        phone = in.readString();
        kendala = in.readString();
        serviceId = in.readString();
        status = in.readString();
        serviceDate = in.readString();
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(dateTime);
        dest.writeString(merk);
        dest.writeString(phone);
        dest.writeString(kendala);
        dest.writeString(serviceId);
        dest.writeString(status);
        dest.writeString(serviceDate);
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceModel> CREATOR = new Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel in) {
            return new ServiceModel(in);
        }

        @Override
        public ServiceModel[] newArray(int size) {
            return new ServiceModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKendala() {
        return kendala;
    }

    public void setKendala(String kendala) {
        this.kendala = kendala;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
