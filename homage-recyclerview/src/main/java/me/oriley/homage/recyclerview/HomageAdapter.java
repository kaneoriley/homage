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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import me.oriley.homage.Homage;

@SuppressWarnings("unused")
public class HomageAdapter extends RecyclerView.Adapter<HomageViewHolder> {

    @NonNull
    private final Homage mHomage;


    public HomageAdapter(@NonNull Context context, @RawRes int rawResourceId) {
        mHomage = new Homage(context, rawResourceId);
    }

    public HomageAdapter(@NonNull Context context, @NonNull String assetPath) {
        mHomage = new Homage(context, assetPath);
    }


    public void addLicense(@NonNull String key, @StringRes int nameRes, @StringRes int urlRes, @StringRes int descRes) {
        mHomage.addLicense(key, nameRes, urlRes, descRes);
    }

    public void refresh() {
        mHomage.refreshLibraries();
        notifyDataSetChanged();
    }

    @Override
    public HomageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomageLibraryView view = (HomageLibraryView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homage_recycler_item, parent, false);
        return new HomageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomageViewHolder holder, int position) {
        holder.setLibrary(mHomage.getLibraries().get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.toggleExpanded();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHomage.getLibraries().size();
    }
}