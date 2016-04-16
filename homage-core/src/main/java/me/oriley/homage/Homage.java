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

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Locale.US;
import static me.oriley.homage.Homage.CoreLicense.*;
import static me.oriley.homage.HomageUtils.parseLibraries;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Homage {

    public enum CoreLicense {
        CC0, CC3, APACHE2, BSD2, BSD3, LGPL3, MIT, UNKNOWN
    }

    private static final String TAG = Homage.class.getSimpleName();

    @NonNull
    private final Map<String, License> mLicenses = new HashMap<>();

    @NonNull
    private final ArrayList<Library> mLibraries = new ArrayList<>();

    @NonNull
    private final Context mContext;

    @Nullable
    private String mAssetPath;

    @RawRes
    private int mResourceId;


    // Application context so don't stress :)
    private Homage(@NonNull Context context) {
        mContext = context.getApplicationContext();

        addLicense(CC0, R.string.homage_license_cc0_10_licenseName, R.string.homage_license_cc0_10_licenseWebsite, R.string.homage_license_cc0_10_licenseDescription);
        addLicense(CC3, R.string.homage_license_cc30_licenseName, R.string.homage_license_cc30_licenseWebsite, R.string.homage_license_cc30_licenseDescription);
        addLicense(APACHE2, R.string.homage_license_Apache_2_0_licenseName, R.string.homage_license_Apache_2_0_licenseWebsite, R.string.homage_license_Apache_2_0_licenseDescription);
        addLicense(BSD2, R.string.homage_license_bsd_2_licenseName, R.string.homage_license_bsd_2_licenseWebsite, R.string.homage_license_bsd_2_licenseDescription);
        addLicense(BSD3, R.string.homage_license_bsd_3_licenseName, R.string.homage_license_bsd_3_licenseWebsite, R.string.homage_license_bsd_3_licenseDescription);
        addLicense(LGPL3, R.string.homage_license_lgpl_3_0_licenseName, R.string.homage_license_lgpl_3_0_licenseWebsite, R.string.homage_license_lgpl_3_0_licenseDescription);
        addLicense(MIT, R.string.homage_license_mit_licenseName, R.string.homage_license_mit_licenseWebsite, R.string.homage_license_mit_licenseDescription);
        addLicense(UNKNOWN, R.string.homage_empty_license, R.string.homage_empty_license, R.string.homage_unrecognised_license);
    }

    public Homage(@NonNull Context context, @RawRes int licensesResourceId) {
        this(context);
        mResourceId = licensesResourceId;
    }

    public Homage(@NonNull Context context, @NonNull String assetPath) {
        this(context);
        mAssetPath = assetPath;
    }


    public void refreshLibraries() {
        mLibraries.clear();

        Library[] libraries;
        if (mAssetPath != null) {
            libraries = getLibraryArray(mContext, mAssetPath);
        } else if (mResourceId > 0) {
            libraries = getLibraryArray(mContext, mResourceId);
        } else {
            throw new IllegalStateException("No asset path or resource ID available");
        }

        if (libraries == null) {
            return;
        }

        for (Library library : libraries) {
            String licenseCode = library.getLicenseCode();

            License license;
            if (mLicenses.containsKey(licenseCode)) {
                license = mLicenses.get(licenseCode);
            } else {
                license = mLicenses.get(UNKNOWN.name().toLowerCase(US));
            }

            library.setLicense(license);
            mLibraries.add(library);
        }
    }

    @NonNull
    public List<Library> getLibraries() {
        return Collections.unmodifiableList(mLibraries);
    }

    private void addLicense(@NonNull CoreLicense coreLicense, @StringRes int nameRes, @StringRes int urlRes, @StringRes int descRes) {
        addLicense(coreLicense.name().toLowerCase(US), nameRes, urlRes, descRes);
    }

    public void addLicense(@NonNull String key, @StringRes int nameRes, @StringRes int urlRes, @StringRes int descRes) {
        String name = mContext.getString(nameRes);
        String url = mContext.getString(urlRes);
        Spanned description = Html.fromHtml(mContext.getString(descRes));
        addLicense(key, name, url, description);
    }

    public void addLicense(@NonNull String key, @NonNull String name, @NonNull String url, @NonNull String description) {
        addLicense(key, name, url, new SpannedString(description));
    }

    public void addLicense(@NonNull String key, @NonNull String name, @NonNull String url, @NonNull Spanned description) {
        License license = new License(name, url, description);
        mLicenses.put(key, license);
    }

    @Nullable
    private static Library[] getLibraryArray(@NonNull Context context, @RawRes int rawResourceId) {
        try {
            InputStream stream = context.getResources().openRawResource(rawResourceId);
            return parseLibraries(stream);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "NotFoundException reading license file: " + rawResourceId, e);
            return null;
        }
    }

    @Nullable
    private static Library[] getLibraryArray(@NonNull Context context, @NonNull String assetPath) {
        try {
            InputStream stream = context.getAssets().open(assetPath);
            return parseLibraries(stream);
        } catch (IOException e) {
            Log.e(TAG, "IOException reading license file: " + assetPath, e);
            return null;
        }
    }
}