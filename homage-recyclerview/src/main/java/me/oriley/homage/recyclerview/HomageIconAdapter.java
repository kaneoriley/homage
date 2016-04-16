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
import android.view.View;
import android.view.ViewGroup;
import me.oriley.homage.Homage;

@SuppressWarnings("unused")
public class HomageIconAdapter extends AbstractHomageAdapter<HomageIconHolder> {


    public HomageIconAdapter(@NonNull Homage homage) {
        super(homage);
    }


    @Override
    public HomageIconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomageIconView view = (HomageIconView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homage_icon_recycler_item, parent, false);
        return new HomageIconHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomageIconHolder holder, int position) {
        holder.setLibrary(mHomage.getLibraries().get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopup();
            }
        });
    }
}