package edu.iubat.vts.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

@Keep
@IgnoreExtraProperties
public class User implements Parcelable {
    @PropertyName("documentId")
    private String documentId;
    @PropertyName("displayIconPath")
    private String displayIconPath;
    @PropertyName("firstName")
    private String firstName;
    @PropertyName("lastName")
    private String lastName;
    @PropertyName("email")
    private String email;
    @PropertyName("designation")
    private String designation;
    @PropertyName("studentId")
    private String studentId;
    @PropertyName("student")
    private boolean student;
    @PropertyName("driver")
    private boolean driver;
    @PropertyName("admin")
    private boolean admin;
    @PropertyName("active")
    private boolean active;

    public User() { }

    public User(String documentId, String displayIconPath, String firstName, String lastName,
                String email, String designation, String studentId, boolean student,
                boolean driver, boolean admin, boolean active) {
        this.documentId = documentId;
        this.displayIconPath = displayIconPath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.designation = designation;
        this.studentId = studentId;
        this.student = student;
        this.driver = driver;
        this.admin = admin;
        this.active = active;
    }

    protected User(Parcel in) {
        documentId = in.readString();
        displayIconPath = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        designation = in.readString();
        studentId = in.readString();
        student = in.readByte() != 0;
        driver = in.readByte() != 0;
        admin = in.readByte() != 0;
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(displayIconPath);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(designation);
        dest.writeString(studentId);
        dest.writeByte((byte) (student ? 1 : 0));
        dest.writeByte((byte) (driver ? 1 : 0));
        dest.writeByte((byte) (admin ? 1 : 0));
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDisplayIconPath() {
        return displayIconPath;
    }

    public void setDisplayIconPath(String displayIconPath) {
        this.displayIconPath = displayIconPath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getName() {
        String name = firstName;
        if (lastName != null && !lastName.isEmpty()) {
            name += " " + lastName;
        }
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return student == user.student &&
                driver == user.driver &&
                admin == user.admin &&
                active == user.active &&
                Objects.equals(documentId, user.documentId) &&
                Objects.equals(displayIconPath, user.displayIconPath) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(designation, user.designation) &&
                Objects.equals(studentId, user.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, displayIconPath, firstName, lastName, email, designation,
                studentId, student, driver, admin, active);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "documentId='" + documentId + '\'' +
                ", displayIconPath='" + displayIconPath + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", designation='" + designation + '\'' +
                ", studentId='" + studentId + '\'' +
                ", student=" + student +
                ", driver=" + driver +
                ", admin=" + admin +
                ", active=" + active +
                '}';
    }

    public static class DiffUtilCallback extends DiffUtil.ItemCallback<User> {

        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getDocumentId().equals(newItem.getDocumentId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    }
}
