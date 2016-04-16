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

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;

import me.oriley.homage.Library;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static me.oriley.homage.recyclerview.StringUtils.nullToEmpty;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HomageView extends CardView {

    public enum ExtraInfoMode {
        EXPANDABLE, POPUP
    }

    private static final String TAG = HomageView.class.getSimpleName();

    private static final int EXPAND_ANIMATION_MILLIS = 250;
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

    @NonNull
    private FrameLayout mExpandedContainer;

    @Nullable
    private Library mLibrary;

    @NonNull
    private ExtraInfoMode mExtraInfoMode = ExtraInfoMode.EXPANDABLE;

    // region expandedLayout
    @Nullable
    private TextView mExpandedDescription;

    @Nullable
    private TextView mExpandedLicenseName;

    @Nullable
    private TextView mExpandedLicenseDescription;

    @Nullable
    private HorizontalScrollView mExpandedLicenseHolder;

    @Nullable
    private LinearLayout mExpandedLayout;
    // endregion expandedLayout

    private boolean mAnimating;

    private boolean mShowIcons;


    public HomageView(@NonNull Context context) {
        this(context, null);
    }

    public HomageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        View.inflate(context, R.layout.homage_view, this);

        mTitleView = (TextView) findViewById(R.id.homage_view_title);
        mIconView = (ImageView) findViewById(R.id.homage_view_icon);
        mSummaryView = (TextView) findViewById(R.id.homage_view_summary);
        mChevronView = (ImageView) findViewById(R.id.homage_view_chevron);
        mWebButton = (ImageView) findViewById(R.id.homage_view_web_button);
        mExpandedContainer = (FrameLayout) findViewById(R.id.homage_view_expanded_container);

        mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
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
        if (mExtraInfoMode == ExtraInfoMode.EXPANDABLE && mExpandedLayout != null) {
            toggleExpanded(mExpandedLayout);
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

    private void toggleExpanded(@NonNull final View expandedLayout) {
        if (mAnimating) {
            return;
        }

        final boolean expanding = expandedLayout.getVisibility() != VISIBLE;
        if (expanding) {
            expandedLayout.setVisibility(View.VISIBLE);
            expandedLayout.setEnabled(true);
        }

        // Note: Must post to container so that the layout can be measured
        expandedLayout.post(new Runnable() {
            @Override
            public void run() {
                performAnimation(expandedLayout, expanding);
            }
        });
    }

    private void performAnimation(@NonNull final View expandedLayout, final boolean expanding) {
        int containerWidth = expandedLayout.getMeasuredWidth();
        expandedLayout.measure(MeasureSpec.makeMeasureSpec(containerWidth, AT_MOST), UNSPECIFIED);
        final int containerHeight = expandedLayout.getMeasuredHeight();

        expandedLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(expanding ? 0f : 1f, expanding ? 1f : 0f);

        valueAnimator.setDuration(EXPAND_ANIMATION_MILLIS);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                int height = (int) (currentValue * containerHeight);
                expandedLayout.getLayoutParams().height = Math.max(height, 0);
                expandedLayout.requestLayout();
                expandedLayout.setAlpha(currentValue);
                mChevronView.setRotation(CHEVRON_ROTATION_AMOUNT * currentValue);
            }
        });
        valueAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expanding) {
                    expandedLayout.setVisibility(View.GONE);
                    expandedLayout.setEnabled(false);
                }
                expandedLayout.setLayerType(View.LAYER_TYPE_NONE, null);
                mAnimating = false;
            }
        });

        valueAnimator.start();
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
            if (mExpandedLayout == null) {
                mExpandedLayout = (LinearLayout) View.inflate(getContext(), R.layout.homage_view_expanded_layout, null);
                mExpandedContainer.addView(mExpandedLayout);

                if (mExpandedLayout == null) {
                    throw new IllegalStateException("Invalid expanded view inflated");
                }

                mExpandedDescription = (TextView) mExpandedLayout.findViewById(R.id.homage_view_expanded_description);
                mExpandedLicenseName = (TextView) mExpandedLayout.findViewById(R.id.homage_view_expanded_license_name);
                mExpandedLicenseDescription = (TextView) mExpandedLayout.findViewById(R.id.homage_view_expanded_license_description);
                mExpandedLicenseHolder = (HorizontalScrollView) mExpandedLayout.findViewById(R.id.homage_view_expanded_license_holder);

                if (mExpandedDescription == null || mExpandedLicenseName == null || mExpandedLicenseDescription == null) {
                    throw new IllegalStateException("Invalid expanded view inflated");
                }

                mExpandedLicenseName.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                mExpandedLicenseDescription.setTypeface(Typeface.MONOSPACE);
            }

            mExpandedLayout.setEnabled(false);
            mExpandedLayout.getLayoutParams().height = 0;

            if (mExpandedDescription != null) {
                updateDescription(mExpandedDescription);
            }
            if (mExpandedLicenseHolder != null) {
                updateLicenseHolder(mExpandedLicenseHolder, mExpandedLicenseName, mExpandedLicenseDescription);
            }

            mExpandedLayout.setVisibility(View.GONE);
            mChevronView.setVisibility(VISIBLE);
            mChevronView.setRotation(0f);
        } else {
            if (mExpandedLayout != null) {
                mExpandedContainer.removeView(mExpandedLayout);
                mExpandedLayout = null;
            }
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

    private void updateTextView(@NonNull TextView view, @Nullable Spanned text) {
        view.setText(text);
        view.setVisibility(!TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }

    private void updateTextView(@NonNull TextView view, @Nullable String text) {
        view.setText(text);
        view.setVisibility(!TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }
}