package com.shaheen.kbj;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    String imageNAme;
    float top_percent, bottom_percent, left_percent, right_percent;
    int image_id,activity;

    public ImageModel(int image_id,String imageNAme, float top_percent, float bottom_percent, float left_percent, float right_percent, int activity) {
        this.imageNAme = imageNAme;
        this.image_id = image_id;
        this.top_percent = top_percent;
        this.bottom_percent = bottom_percent;
        this.left_percent = left_percent;
        this.right_percent = right_percent;
        this.activity = activity;
    }

    protected ImageModel(Parcel in) {
        imageNAme = in.readString();
        top_percent = in.readFloat();
        bottom_percent = in.readFloat();
        left_percent = in.readFloat();
        right_percent = in.readFloat();
        image_id = in.readInt();
        activity = in.readInt();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getImageNAme() {
        return imageNAme;
    }

    public void setImageNAme(String imageNAme) {
        this.imageNAme = imageNAme;
    }

    public float getTop_percent() {
        return top_percent;
    }

    public void setTop_percent(float top_percent) {
        this.top_percent = top_percent;
    }

    public float getBottom_percent() {
        return bottom_percent;
    }

    public void setBottom_percent(float bottom_percent) {
        this.bottom_percent = bottom_percent;
    }

    public float getLeft_percent() {
        return left_percent;
    }

    public void setLeft_percent(float left_percent) {
        this.left_percent = left_percent;
    }

    public float getRight_percent() {
        return right_percent;
    }

    public void setRight_percent(float right_percent) {
        this.right_percent = right_percent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageNAme);
        parcel.writeFloat(top_percent);
        parcel.writeFloat(bottom_percent);
        parcel.writeFloat(left_percent);
        parcel.writeFloat(right_percent);
        parcel.writeInt(image_id);
        parcel.writeInt(activity);
    }
}
