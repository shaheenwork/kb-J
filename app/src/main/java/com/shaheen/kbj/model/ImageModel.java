package com.shaheen.kbj.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    String imageNAme;
    float name_top, name_left, number_top, number_left;
    int image_id,display_image_id;



    public ImageModel(String imageNAme, float name_top, float name_left, float number_top, float number_left, int image_id, int display_image_id) {
        this.imageNAme = imageNAme;
        this.name_top = name_top;
        this.name_left = name_left;
        this.number_top = number_top;
        this.number_left = number_left;
        this.image_id = image_id;
        this.display_image_id = display_image_id;
    }

    protected ImageModel(Parcel in) {
        imageNAme = in.readString();
        name_top = in.readFloat();
        name_left = in.readFloat();
        number_top = in.readFloat();
        number_left = in.readFloat();
        image_id = in.readInt();
        display_image_id = in.readInt();
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

    public int getDisplay_image_id() {
        return display_image_id;
    }

    public void setDisplay_image_id(int display_image_id) {
        this.display_image_id = display_image_id;
    }

    public String getImageNAme() {
        return imageNAme;
    }

    public void setImageNAme(String imageNAme) {
        this.imageNAme = imageNAme;
    }

    public float getName_top() {
        return name_top;
    }

    public void setName_top(float name_top) {
        this.name_top = name_top;
    }

    public float getName_left() {
        return name_left;
    }

    public void setName_left(float name_left) {
        this.name_left = name_left;
    }

    public float getNumber_top() {
        return number_top;
    }

    public void setNumber_top(float number_top) {
        this.number_top = number_top;
    }

    public float getNumber_left() {
        return number_left;
    }

    public void setNumber_left(float number_left) {
        this.number_left = number_left;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageNAme);
        parcel.writeFloat(name_top);
        parcel.writeFloat(name_left);
        parcel.writeFloat(number_top);
        parcel.writeFloat(number_left);
        parcel.writeInt(image_id);
        parcel.writeInt(display_image_id);
    }
}
