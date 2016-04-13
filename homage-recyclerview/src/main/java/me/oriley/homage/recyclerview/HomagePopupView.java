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

package me.oriley.homage.recyclerview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.oriley.homage.Library;

import static me.oriley.homage.recyclerview.StringUtils.nullToEmpty;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HomagePopupView extends CardView {

    private static final String TAG = HomagePopupView.class.getSimpleName();

    @NonNull
    private TextView mTitleView;

    @NonNull
    private TextView mSummaryView;

    @NonNull
    private ImageView mWebButton;

    @Nullable
    private Library mLibrary;


    public HomagePopupView(@NonNull Context context) {
        this(context, null);
    }

    public HomagePopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomagePopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        View.inflate(context, R.layout.homage_popup_view, this);

        mTitleView = (TextView) findViewById(R.id.homage_popup_view_title_view);
        mSummaryView = (TextView) findViewById(R.id.homage_popup_view_summary_view);
        mWebButton = (ImageView) findViewById(R.id.homage_popup_view_web_button);

        mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        updateView();
    }


    public void setLibrary(@Nullable Library library) {
        if (mLibrary != library) {
            mLibrary = library;
            updateView();
        }
    }

    @Nullable
    public Library getLibrary() {
        return mLibrary;
    }

    public void openUrl(@NonNull String url) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "exception launching web activity for " + url, e);
        }
    }

    public void showPopup() {
        View dialogView = View.inflate(getContext(), R.layout.homage_popup_view_dialog_view, null);

        TextView descriptionView = (TextView) dialogView.findViewById(R.id.homage_popup_view_dialog_description_view);
        TextView licenseNameView = (TextView) dialogView.findViewById(R.id.homage_popup_view_dialog_license_name_view);
        TextView licenseDescriptionView = (TextView) dialogView.findViewById(R.id.homage_popup_view_dialog_license_description_view);

        licenseNameView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        licenseDescriptionView.setTypeface(Typeface.MONOSPACE);

        updateTextView(descriptionView, mLibrary != null ? mLibrary.getLibraryDescription() : null);
        updateTextView(licenseNameView, mLibrary != null ? mLibrary.getLicenseName() : null);
        updateLicenseDescription(licenseDescriptionView);

        Dialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .show();
    }

    private void updateView() {
        updateTitle();
        updateSummary();
        updateWebButton();
    }

    private void updateWebButton() {
        final String url = mLibrary != null ? mLibrary.getLibraryUrl() : null;
        if (!TextUtils.isEmpty(url)) {
            mWebButton.setVisibility(VISIBLE);
            mWebButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(url);
                }
            });
        } else {
            mWebButton.setVisibility(GONE);
            mWebButton.setOnClickListener(null);
        }
    }

    private void updateLicenseDescription(@NonNull TextView licenseDescriptionView) {
        Spanned licenseDescription = mLibrary != null ? mLibrary.getLicenseDescription() : null;
        if (TextUtils.isEmpty(licenseDescription)) {
            updateTextView(licenseDescriptionView, null);
        } else {
            licenseDescriptionView.setText(licenseDescription);
            licenseDescriptionView.setVisibility(VISIBLE);
        }
    }

    private void updateTitle() {
        String name = mLibrary != null ? mLibrary.getLibraryName() : null;
        String version = mLibrary != null ? mLibrary.getLibraryVersion() : null;
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(version)) {
            updateTextView(mTitleView, null);
        } else {
            updateTextView(mTitleView, nullToEmpty(name) + " " + nullToEmpty(version));
        }
    }

    private void updateSummary() {
        String owner = mLibrary != null ? mLibrary.getLibraryOwner() : null;
        String year = mLibrary != null ? mLibrary.getLibraryYear() : null;
        if (TextUtils.isEmpty(owner) && TextUtils.isEmpty(year)) {
            updateTextView(mSummaryView, null);
        } else {
            String joiner = ", ";
            if (year == null || owner == null) {
                joiner = "";
            }

            updateTextView(mSummaryView, nullToEmpty(year) + joiner + nullToEmpty(owner));
        }
    }

    private void updateTextView(@NonNull TextView view, @Nullable String text) {
        view.setText(text);
        view.setVisibility(!TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }
}
