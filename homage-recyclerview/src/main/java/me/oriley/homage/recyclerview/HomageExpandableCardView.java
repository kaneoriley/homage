/*
 * Copyright (C) 2016 Kane O'Riley
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.UNSPECIFIED;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class HomageExpandableCardView extends CardView {

    private static final int EXPAND_ANIMATION_MILLIS = 250;

    @NonNull
    private ViewStub mCollapsedStub;

    @NonNull
    private ViewStub mExpandedStub;

    @NonNull
    private View mCollapsedView;

    @NonNull
    private View mExpandedView;

    private boolean mAnimating;


    public HomageExpandableCardView(@NonNull Context context) {
        this(context, null);
    }

    public HomageExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomageExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.homage_expandable_card_view, this);
        mCollapsedStub = (ViewStub) findViewById(R.id.homage_expandable_card_view_collapsed_stub);
        mExpandedStub = (ViewStub) findViewById(R.id.homage_expandable_card_view_expanded_stub);

        mCollapsedView = mCollapsedStub;
        mExpandedView = mExpandedStub;
    }


    protected void setCollapsedLayoutResource(@LayoutRes int layoutResource) {
        if (mCollapsedStub.getLayoutResource() != layoutResource) {
            mCollapsedStub.setLayoutResource(layoutResource);
            mCollapsedView = mCollapsedStub.inflate();
            onCollapsedViewInflated(mCollapsedView);
        }
    }

    protected void setExpandedLayoutResource(@LayoutRes int layoutResource) {
        if (mExpandedStub.getLayoutResource() != layoutResource) {
            mExpandedStub.setLayoutResource(layoutResource);
            mExpandedView = mExpandedStub.inflate();
            initialiseExpandedView();
        }
    }

    private void initialiseExpandedView() {
        resetExpandedState();
        onExpandedViewInflated(mExpandedView);
    }

    protected abstract void onCollapsedViewInflated(@NonNull View view);

    protected abstract void onExpandedViewInflated(@NonNull View view);

    protected void resetExpandedState() {
        mExpandedView.setEnabled(false);
        mExpandedView.getLayoutParams().height = 0;
        mExpandedView.setVisibility(View.GONE);
    }

    public void toggleExpanded() {
        if (mAnimating) {
            return;
        }

        final boolean expanding = mExpandedView.getVisibility() != VISIBLE;
        if (expanding) {
            mExpandedView.setVisibility(View.VISIBLE);
            mExpandedView.setEnabled(true);
        }

        // Note: Must post to container so that the layout can be measured
        mExpandedView.post(new Runnable() {
            @Override
            public void run() {
                performAnimation(mExpandedView, expanding);
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
                onExpandedAnimationUpdate(currentValue);
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

    @NonNull
    protected View getCollapsedView() {
        return mCollapsedView;
    }

    @NonNull
    protected View getExpandedView() {
        return mExpandedView;
    }

    protected void onExpandedAnimationUpdate(float level) {
    }

    protected void updateTextView(@NonNull TextView view, @Nullable Spanned text) {
        view.setText(text);
        view.setVisibility(!TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }

    protected void updateTextView(@NonNull TextView view, @Nullable String text) {
        view.setText(text);
        view.setVisibility(!TextUtils.isEmpty(text) ? VISIBLE : GONE);
    }
}
