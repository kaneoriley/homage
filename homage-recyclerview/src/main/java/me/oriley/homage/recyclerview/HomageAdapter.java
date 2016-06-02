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

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
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
