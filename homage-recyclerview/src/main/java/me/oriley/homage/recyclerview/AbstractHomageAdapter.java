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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import me.oriley.homage.Homage;

@SuppressWarnings("WeakerAccess")
abstract class AbstractHomageAdapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {

    @NonNull
    protected final Homage mHomage;


    public AbstractHomageAdapter(@NonNull Homage homage) {
        mHomage = homage;
    }

    @Override
    public int getItemCount() {
        return mHomage.getLibraries().size();
    }
}
