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

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Library {

    // "name"
    @Nullable
    private final String mLibraryName;

    // "icon"
    @Nullable
    private final String mLibraryIcon;

    // "version"
    @Nullable
    private final String mLibraryVersion;

    // "description"
    @Nullable
    private final String mLibraryDescription;

    // "year"
    @Nullable
    private final String mLibraryYear;

    // "owner"
    @Nullable
    private final String mLibraryOwner;

    // "ownerUrl"
    @Nullable
    private final String mLibraryOwnerUrl;

    // "url"
    @Nullable
    private final String mLibraryUrl;

    // "license"
    @Nullable
    private final String mLicenseCode;

    private License mLicense;

    @DrawableRes
    private int mIconResource;

    private boolean mIsIconUrl = false;

    Library(@Nullable String name,
            @Nullable String icon,
            @Nullable String version,
            @Nullable String description,
            @Nullable String year,
            @Nullable String owner,
            @Nullable String ownerUrl,
            @Nullable String url,
            @Nullable String license) {
        mLibraryName = name;
        mLibraryIcon = icon;
        mLibraryVersion = version;
        mLibraryDescription = description;
        mLibraryYear = year;
        mLibraryOwner = owner;
        mLibraryOwnerUrl = ownerUrl;
        mLibraryUrl = url;
        mLicenseCode = license;
    }


    @Nullable
    public String getLibraryName() {
        return mLibraryName;
    }

    @Nullable
    public String getLibraryIcon() {
        return mLibraryIcon;
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
    public String getLibraryOwnerUrl() {
        return mLibraryOwnerUrl;
    }

    @Nullable
    public String getLibraryUrl() {
        return mLibraryUrl;
    }

    @Nullable
    public String getLicenseCode() {
        return mLicenseCode;
    }

    @DrawableRes
    public int getIconResource() {
        return mIconResource;
    }

    public boolean getIsIconUrl() {
        return mIsIconUrl;
    }

    @NonNull
    public License getLicense() {
        validateLicense();
        return mLicense;
    }

    void setIconResource(@DrawableRes int iconResource) {
        mIconResource = iconResource;
    }

    void setIsIconUrl(boolean isIconUrl) {
        mIsIconUrl = isIconUrl;
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
