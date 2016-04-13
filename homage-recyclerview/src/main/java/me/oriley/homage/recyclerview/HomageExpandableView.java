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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.oriley.homage.Library;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static me.oriley.homage.recyclerview.StringUtils.nullToEmpty;

@SuppressWarnings({"WeakerAccess", "unused"})
public class HomageExpandableView extends CardView {

    private static final String TAG = HomageExpandableView.class.getSimpleName();

    private static final int EXPAND_ANIMATION_MILLIS = 250;
    private static final float CHEVRON_ROTATION_AMOUNT = 180f;

    @NonNull
    private TextView mTitleView;

    @NonNull
    private TextView mSummaryView;

    @NonNull
    private TextView mDescriptionView;

    @NonNull
    private TextView mLicenseNameView;

    @NonNull
    private TextView mLicenseDescriptionView;

    @NonNull
    private LinearLayout mExpandedContainer;

    @NonNull
    private ImageView mChevronView;

    @NonNull
    private ImageView mWebButton;

    @Nullable
    private Library mLibrary;

    private boolean mAnimating;


    public HomageExpandableView(@NonNull Context context) {
        this(context, null);
    }

    public HomageExpandableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomageExpandableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        View.inflate(context, R.layout.homage_expandable_view, this);

        mTitleView = (TextView) findViewById(R.id.homage_expandable_view_title_view);
        mSummaryView = (TextView) findViewById(R.id.homage_expandable_view_summary_view);
        mDescriptionView = (TextView) findViewById(R.id.homage_expandable_view_description_view);
        mLicenseNameView = (TextView) findViewById(R.id.homage_expandable_view_license_name_view);
        mLicenseDescriptionView = (TextView) findViewById(R.id.homage_expandable_view_license_description_view);
        mExpandedContainer = (LinearLayout) findViewById(R.id.homage_expandable_view_expanded_container);
        mChevronView = (ImageView) findViewById(R.id.homage_expandable_view_chevron_view);
        mWebButton = (ImageView) findViewById(R.id.homage_expandable_view_web_button);

        mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        mLicenseNameView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        mLicenseDescriptionView.setTypeface(Typeface.MONOSPACE);
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

    public void toggleExpanded() {
        if (mAnimating) {
            return;
        }

        mAnimating = true;

        int containerWidth = mExpandedContainer.getMeasuredWidth();
        mExpandedContainer.measure(MeasureSpec.makeMeasureSpec(containerWidth, AT_MOST), UNSPECIFIED);
        final int containerHeight = mExpandedContainer.getMeasuredHeight();

        mExpandedContainer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        final boolean expanding = mExpandedContainer.getVisibility() != VISIBLE;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(expanding ? 0f : 1f, expanding ? 1f : 0f);

        if (expanding) {
            mExpandedContainer.setVisibility(View.VISIBLE);
            mExpandedContainer.setEnabled(true);
        }

        valueAnimator.setDuration(EXPAND_ANIMATION_MILLIS);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                int height = (int) (currentValue * containerHeight);
                mExpandedContainer.getLayoutParams().height = Math.max(height, 0);
                mExpandedContainer.requestLayout();
                mExpandedContainer.setAlpha(currentValue);
                mChevronView.setRotation(CHEVRON_ROTATION_AMOUNT * currentValue);
            }
        });
        valueAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expanding) {
                    mExpandedContainer.setVisibility(View.INVISIBLE);
                    mExpandedContainer.setEnabled(false);
                }
                mExpandedContainer.setLayerType(View.LAYER_TYPE_NONE, null);
                mAnimating = false;
            }
        });

        valueAnimator.start();
    }

    private void updateView() {
        updateTitle();
        updateSummary();
        updateWebButton();
        updateTextView(mDescriptionView, mLibrary != null ? mLibrary.getLibraryDescription() : null);
        updateTextView(mLicenseNameView, mLibrary != null ? mLibrary.getLicenseName() : null);
        updateLicenseDescription();

        mExpandedContainer.setVisibility(View.INVISIBLE);
        mExpandedContainer.setEnabled(false);
        mExpandedContainer.getLayoutParams().height = 0;
        mChevronView.setRotation(0f);
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

    private void updateLicenseDescription() {
        Spanned licenseDescription = mLibrary != null ? mLibrary.getLicenseDescription() : null;
        if (TextUtils.isEmpty(licenseDescription)) {
            updateTextView(mLicenseDescriptionView, null);
        } else {
            mLicenseDescriptionView.setText(licenseDescription);
            mLicenseDescriptionView.setVisibility(VISIBLE);
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
