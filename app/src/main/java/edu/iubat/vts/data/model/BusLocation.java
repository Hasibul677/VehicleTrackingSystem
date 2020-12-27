package edu.iubat.vts.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

@Keep
public class BusLocation implements Parcelable {
    @PropertyName("latitude")
    private double latitude;
    @PropertyName("longitude")
    private double longitude;

    public BusLocation() { }

    public BusLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected BusLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BusLocation> CREATOR = new Creator<BusLocation>() {
        @Override
        public BusLocation createFromParcel(Parcel in) {
            return new BusLocation(in);
        }

        @Override
        public BusLocation[] newArray(int size) {
            return new BusLocation[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusLocation busLocation = (BusLocation) o;
        return Double.compare(busLocation.latitude, latitude) == 0 &&
                Double.compare(busLocation.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
