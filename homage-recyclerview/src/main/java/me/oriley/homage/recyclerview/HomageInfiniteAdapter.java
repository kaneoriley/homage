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
import me.oriley.homage.Homage;
import me.oriley.homage.Library;
import me.oriley.homage.recyclerview.HomageView.ExtraInfoMode;

public class HomageInfiniteAdapter extends HomageAdapter {


    public HomageInfiniteAdapter(@NonNull Homage homage, @NonNull ExtraInfoMode extraInfoMode, boolean showIcons) {
        super(homage, extraInfoMode, showIcons);
    }


    @NonNull
    @Override
    public Library getItem(int position) {
        return super.getItem(position % mLibraries.size());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}