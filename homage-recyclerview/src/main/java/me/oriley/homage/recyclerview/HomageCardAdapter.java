package me.oriley.homage.recyclerview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.oriley.homage.Homage;

public class HomageCardAdapter extends HomageAdapterBase {

    public HomageCardAdapter(@NonNull Homage homage, @NonNull HomageView.ExtraInfoMode extraInfoMode, boolean showIcons) {
        super(homage, extraInfoMode, showIcons);
    }

    public HomageCardAdapter(@NonNull Homage homage, @NonNull HomageView.ExtraInfoMode extraInfoMode, boolean showIcons, boolean dark) {
        super(homage, extraInfoMode, showIcons, dark);
    }

    @Override
    public HomageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mDark ? R.layout.homage_recycler_card_dark : R.layout.homage_recycler_card_light, parent, false);
        HomageView homage = (HomageView) view.findViewById(R.id.homageView);
        homage.setExtraInfoMode(mExtraInfoMode);
        homage.setShowIcons(mShowIcons);
        return new HomageViewHolder(view, homage);
    }
}
