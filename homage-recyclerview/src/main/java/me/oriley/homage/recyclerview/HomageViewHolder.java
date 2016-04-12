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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import me.oriley.homage.Library;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HomageViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    private final HomageLibraryView mLibraryView;


    public HomageViewHolder(@NonNull HomageLibraryView view) {
        super(view);
        mLibraryView = view;
    }


    @NonNull
    public HomageLibraryView getLibraryView() {
        return mLibraryView;
    }

    public void setLibrary(@Nullable Library library) {
        mLibraryView.setLibrary(library);
    }

    public void setOnClickListener(@Nullable View.OnClickListener listener) {
        mLibraryView.setOnClickListener(listener);
    }

    public void toggleExpanded() {
        mLibraryView.toggleExpanded();
    }
}