package edu.iubat.vts.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

@Keep
public class Bus implements Parcelable {
    @PropertyName("documentId")
    private String documentId;
    @PropertyName("driverDocumentId")
    private String driverDocumentId;
    @PropertyName("conductorName")
    private String conductorName;
    @PropertyName("licenseNumber")
    private String licenseNumber;
    @PropertyName("busNumber")
    private String busNumber;

    public Bus() { }

    public Bus(String documentId, String driverDocumentId, String conductorName,
               String licenseNumber, String busNumber) {
        this.documentId = documentId;
        this.driverDocumentId = driverDocumentId;
        this.conductorName = conductorName;
        this.licenseNumber = licenseNumber;
        this.busNumber = busNumber;
    }

    protected Bus(Parcel in) {
        documentId = in.readString();
        driverDocumentId = in.readString();
        conductorName = in.readString();
        licenseNumber = in.readString();
        busNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(driverDocumentId);
        dest.writeString(conductorName);
        dest.writeString(licenseNumber);
        dest.writeString(busNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bus> CREATOR = new Creator<Bus>() {
        @Override
        public Bus createFromParcel(Parcel in) {
            return new Bus(in);
        }

        @Override
        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDriverDocumentId() {
        return driverDocumentId;
    }

    public void setDriverDocumentId(String driverDocumentId) {
        this.driverDocumentId = driverDocumentId;
    }

    public String getConductorName() {
        return conductorName;
    }

    public void setConductorName(String conductorName) {
        this.conductorName = conductorName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        return Objects.equals(documentId, bus.documentId) &&
                Objects.equals(driverDocumentId, bus.driverDocumentId) &&
                Objects.equals(conductorName, bus.conductorName) &&
                Objects.equals(licenseNumber, bus.licenseNumber) &&
                Objects.equals(busNumber, bus.busNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, driverDocumentId, conductorName, licenseNumber, busNumber);
    }

    @NonNull
    @Override
    public String toString() {
        return "Bus{" +
                "documentId='" + documentId + '\'' +
                ", driverDocumentId='" + driverDocumentId + '\'' +
                ", contractorName='" + conductorName + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", busNumber=" + busNumber +
                '}';
    }

    public static class DiffUtilCallback extends DiffUtil.ItemCallback<Bus> {

        @Override
        public boolean areItemsTheSame(@NonNull Bus oldItem, @NonNull Bus newItem) {
            return oldItem.getDocumentId().equals(newItem.getDocumentId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Bus oldItem, @NonNull Bus newItem) {
            return oldItem.equals(newItem);
        }
    }
}
