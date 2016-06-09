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
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import me.oriley.homage.Homage;
import me.oriley.homage.Library;
import me.oriley.homage.recyclerview.HomageView.ExtraInfoMode;

@SuppressWarnings({"unused", "WeakerAccess"})
abstract class HomageAdapterBase extends RecyclerView.Adapter<HomageViewHolder> {

    @NonNull
    protected final Homage mHomage;

    @NonNull
    protected final List<Library> mLibraries;

    @NonNull
    protected final ExtraInfoMode mExtraInfoMode;

    protected final boolean mShowIcons;

    protected final boolean mDark;


    public HomageAdapterBase(@NonNull Homage homage, @NonNull ExtraInfoMode extraInfoMode, boolean showIcons) {
        this(homage, extraInfoMode, showIcons, false);
    }

    public HomageAdapterBase(@NonNull Homage homage, @NonNull ExtraInfoMode extraInfoMode, boolean showIcons, boolean dark) {
        mHomage = homage;
        mLibraries = mHomage.getLibraries();
        mExtraInfoMode = extraInfoMode;
        mShowIcons = showIcons;
        mDark = dark;
    }


    @Override
    public void onBindViewHolder(final HomageViewHolder holder, int position) {
        holder.setLibrary(getItem(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showExtraInfo();
            }
        });
    }

    @NonNull
    public Library getItem(int position) {
        return mLibraries.get(position);
    }

    @Override
    public int getItemCount() {
        return mLibraries.size();
    }
}