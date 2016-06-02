package me.oriley.homage.recyclerview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.oriley.homage.Homage;

public class HomageAdapter extends HomageAdapterBase {

    public HomageAdapter(@NonNull Homage homage, @NonNull HomageView.ExtraInfoMode extraInfoMode, boolean showIcons) {
        super(homage, extraInfoMode, showIcons);
    }

    public HomageAdapter(@NonNull Homage homage, @NonNull HomageView.ExtraInfoMode extraInfoMode, boolean showIcons, boolean dark) {
        super(homage, extraInfoMode, showIcons, dark);
    }

    @Override
    public HomageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomageView view = (HomageView) LayoutInflater.from(parent.getContext())
                .inflate(mDark ? R.layout.homage_recycler_item_dark : R.layout.homage_recycler_item_light, parent, false);
        view.setExtraInfoMode(mExtraInfoMode);
        view.setShowIcons(mShowIcons);
        return new HomageViewHolder(view, view);
    }
}
