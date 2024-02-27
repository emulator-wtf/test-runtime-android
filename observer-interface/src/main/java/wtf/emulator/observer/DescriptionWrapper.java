package wtf.emulator.observer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

@SuppressWarnings("unused")
@AutoValue public abstract class DescriptionWrapper implements Parcelable {
    private final static String LOG_TAG = "EWRunListener";
    private static final int VERSION = 1;

    public static DescriptionWrapper create(@Nullable String className, @Nullable String methodName, @Nullable String displayName) {
        return new AutoValue_DescriptionWrapper(0, className, methodName, displayName);
    }

    public static final Creator<DescriptionWrapper> CREATOR = new Creator<DescriptionWrapper>() {
        @Override
        public DescriptionWrapper createFromParcel(Parcel in) {
            int version = in.readInt();
            if (version > 1) {
                Log.w(LOG_TAG, "Unknown DescriptionWrapper parcel version: " + version);
            }
            return new AutoValue_DescriptionWrapper(0, in.readString(), in.readString(), in.readString());
        }

        @Override
        public DescriptionWrapper[] newArray(int size) {
            return new DescriptionWrapper[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(1);
        dest.writeString(className());
        dest.writeString(methodName());
        dest.writeString(displayName());
    }

    public abstract @Nullable String className();
    public abstract @Nullable String methodName();
    public abstract @Nullable String displayName();
}
