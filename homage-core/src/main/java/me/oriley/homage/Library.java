/*
 * Copyright (C) 2016 Kane O'Riley
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.oriley.homage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Library {

    @SerializedName("name")
    String mLibraryName;

    @SerializedName("version")
    String mLibraryVersion;

    @SerializedName("description")
    String mLibraryDescription;

    @SerializedName("year")
    String mLibraryYear;

    @SerializedName("owner")
    String mLibraryOwner;

    @SerializedName("url")
    String mLibraryUrl;

    @SerializedName("license")
    String mLicenseCode;

    private License mLicense;

    @Nullable
    public String getLibraryName() {
        return mLibraryName;
    }

    @Nullable
    public String getLibraryVersion() {
        return mLibraryVersion;
    }

    @Nullable
    public String getLibraryDescription() {
        return mLibraryDescription;
    }

    @Nullable
    public String getLibraryYear() {
        return mLibraryYear;
    }

    @Nullable
    public String getLibraryOwner() {
        return mLibraryOwner;
    }

    @Nullable
    public String getLibraryUrl() {
        return mLibraryUrl;
    }

    @NonNull
    public String getLicenseCode() {
        return mLicenseCode;
    }

    @NonNull
    public License getLicense() {
        validateLicense();
        return mLicense;
    }

    void setLicense(@NonNull License license) {
        mLicense = license;
    }

    @NonNull
    public String getLicenseName() {
        validateLicense();
        return mLicense.getName();
    }

    @NonNull
    public String getLicenseUrl() {
        validateLicense();
        return mLicense.getUrl();
    }

    @NonNull
    public Spanned getLicenseDescription() {
        validateLicense();
        return mLicense.getDescription();
    }

    private void validateLicense() {
        if (mLicense == null) {
            throw new NullPointerException("License is null");
        }
    }
}
