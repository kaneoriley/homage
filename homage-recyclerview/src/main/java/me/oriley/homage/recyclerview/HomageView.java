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
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.*;

import me.oriley.homage.Library;

import static me.oriley.homage.utils.ObjectUtils.validateNonNull;
import static me.oriley.homage.utils.StringUtils.nullToEmpty;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HomageView extends HomageExpandableCardView {

    public enum ExtraInfoMode {
        EXPANDABLE, POPUP
    }

    private static final String TAG = HomageView.class.getSimpleName();

    private static final float CHEVRON_ROTATION_AMOUNT = 180f;

    @NonNull
    private TextView mTitleView;

    @NonNull
    private ImageView mIconView;

    @NonNull
    private TextView mSummaryView;

    @NonNull
    private ImageView mChevronView;

    @NonNull
    private ImageView mWebButton;

    @Nullable
    private Library mLibrary;

    @NonNull
    private ExtraInfoMode mExtraInfoMode = ExtraInfoMode.EXPANDABLE;

    // region expandedLayout

    @NonNull
    private TextView mExpandedDescription;

    @NonNull
    private TextView mExpandedLicenseName;

    @NonNull
    private TextView mExpandedLicenseDescription;

    @NonNull
    private HorizontalScrollView mExpandedLicenseHolder;

    // endregion expandedLayout

    private boolean mShowIcons;


    public HomageView(@NonNull Context context) {
        this(context, null);
    }

    public HomageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCollapsedLayoutResource(R.layout.homage_view_collapsed_layout);
        setExpandedLayoutResource(R.layout.homage_view_expanded_layout);
    }


    @Override
    protected void onCollapsedViewInflated(@NonNull View view) {
        mTitleView = (TextView) view.findViewById(R.id.homage_view_title);
        mIconView = (ImageView) view.findViewById(R.id.homage_view_icon);
        mSummaryView = (TextView) view.findViewById(R.id.homage_view_summary);
        mChevronView = (ImageView) view.findViewById(R.id.homage_view_chevron);
        mWebButton = (ImageView) view.findViewById(R.id.homage_view_web_button);

        validateNonNull(mTitleView, mIconView, mSummaryView, mChevronView, mWebButton);

        mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onExpandedViewInflated(@NonNull View view) {
        mExpandedDescription = (TextView) view.findViewById(R.id.homage_view_expanded_description);
        mExpandedLicenseName = (TextView) view.findViewById(R.id.homage_view_expanded_license_name);
        mExpandedLicenseDescription = (TextView) view.findViewById(R.id.homage_view_expanded_license_description);
        mExpandedLicenseHolder = (HorizontalScrollView) view.findViewById(R.id.homage_view_expanded_license_holder);

        validateNonNull(mExpandedDescription, mExpandedLicenseName, mExpandedLicenseDescription, mExpandedLicenseHolder);

        mExpandedLicenseName.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        mExpandedLicenseDescription.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    protected void onExpandedAnimationUpdate(float level) {
        mChevronView.setRotation(CHEVRON_ROTATION_AMOUNT * level);
    }

    public void setLibrary(@Nullable Library library) {
        if (mLibrary != library) {
            mLibrary = library;
            updateViewIfBound();
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

    public void setExtraInfoMode(@NonNull ExtraInfoMode extraInfoMode) {
        if (mExtraInfoMode != extraInfoMode) {
            mExtraInfoMode = extraInfoMode;
            updateViewIfBound();
        }
    }

    public void setShowIcons(boolean showIcons) {
        if (mShowIcons != showIcons) {
            mShowIcons = showIcons;
            updateIcon();
        }
    }

    public void showExtraInfo() {
        if (mExtraInfoMode == ExtraInfoMode.EXPANDABLE) {
            toggleExpanded();
        } else if (mExtraInfoMode == ExtraInfoMode.POPUP){
            showPopup();
        }
    }

    private void showPopup() {
        View dialogView = View.inflate(getContext(), R.layout.homage_view_popup_layout, null);

        TextView descriptionView = (TextView) dialogView.findViewById(R.id.homage_view_popup_description);
        TextView licenseNameView = (TextView) dialogView.findViewById(R.id.homage_view_popup_license_name);
        TextView licenseDescriptionView = (TextView) dialogView.findViewById(R.id.homage_view_popup_license_description);
        View licenseHolder = dialogView.findViewById(R.id.homage_view_popup_license_holder);

        licenseNameView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        licenseDescriptionView.setTypeface(Typeface.MONOSPACE);

        updateDescription(descriptionView);
        updateLicenseHolder(licenseHolder, licenseNameView, licenseDescriptionView);

        Dialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .show();
    }

    private void updateViewIfBound() {
        if (mLibrary == null) {
            Log.d(TAG, "Not bound yet, aborting");
            return;
        }

        updateTitle();
        updateIcon();
        updateSummary();
        updateWebButton();

        if (mExtraInfoMode == ExtraInfoMode.EXPANDABLE) {
            resetExpandedState();
            updateDescription(mExpandedDescription);
            updateLicenseHolder(mExpandedLicenseHolder, mExpandedLicenseName, mExpandedLicenseDescription);
            mChevronView.setVisibility(VISIBLE);
            mChevronView.setRotation(0f);
        } else {
            mChevronView.setVisibility(GONE);
        }
    }

    private void updateDescription(@NonNull TextView view) {
        updateTextView(view, mLibrary != null ? mLibrary.getLibraryDescription() : null);
    }

    private void updateIcon() {
        int iconResource = mLibrary != null ? mLibrary.getIconResource() : -1;
        if (iconResource > 0 && mShowIcons) {
            mIconView.setImageResource(iconResource);
            mIconView.setVisibility(VISIBLE);
        } else {
            mIconView.setImageBitmap(null);
            mIconView.setVisibility(GONE);
        }
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

    private void updateLicenseHolder(@NonNull View holder, @Nullable TextView nameView, @Nullable TextView descriptionView) {
        String licenseName = mLibrary != null ? mLibrary.getLicenseName() : null;
        Spanned licenseDescription = mLibrary != null ? mLibrary.getLicenseDescription() : null;

        if (nameView != null) {
            updateTextView(nameView, licenseName);
        }
        if (descriptionView != null) {
            updateTextView(descriptionView, licenseDescription);
        }

        if (TextUtils.isEmpty(licenseName) && TextUtils.isEmpty(licenseDescription)) {
            holder.setVisibility(GONE);
        } else {
            holder.setVisibility(VISIBLE);
        }
    }

    private void updateLicenseName(@NonNull TextView view) {
        updateTextView(view, mLibrary != null ? mLibrary.getLicenseName() : null);
    }

    private void updateLicenseDescription(@NonNull TextView view) {
        updateTextView(view, mLibrary != null ? mLibrary.getLicenseDescription() : null);
    }

    private void updateTitle() {
        String name = mLibrary != null ? mLibrary.getLibraryName() : null;
        String version = mLibrary != null ? mLibrary.getLibraryVersion() : null;
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(version)) {
            updateTextView(mTitleView, (String) null);
        } else {
            updateTextView(mTitleView, nullToEmpty(name) + " " + nullToEmpty(version));
        }
    }

    private void updateSummary() {
        String owner = mLibrary != null ? mLibrary.getLibraryOwner() : null;
        String year = mLibrary != null ? mLibrary.getLibraryYear() : null;
        if (TextUtils.isEmpty(owner) && TextUtils.isEmpty(year)) {
            updateTextView(mSummaryView, (String) null);
        } else {
            String joiner = ", ";
            if (year == null || owner == null) {
                joiner = "";
            }

            updateTextView(mSummaryView, nullToEmpty(year) + joiner + nullToEmpty(owner));
        }
    }
}