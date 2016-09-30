package com.deng.netlibrary.netLib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 邓鉴恒 on 16/9/14.
 */
public class RPCServerErrorModel implements Parcelable {

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.message);
    }

    public RPCServerErrorModel() {
    }

    protected RPCServerErrorModel(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
    }

    public static final Creator<RPCServerErrorModel> CREATOR = new Creator<RPCServerErrorModel>() {
        @Override
        public RPCServerErrorModel createFromParcel(Parcel source) {
            return new RPCServerErrorModel(source);
        }

        @Override
        public RPCServerErrorModel[] newArray(int size) {
            return new RPCServerErrorModel[size];
        }
    };
}
