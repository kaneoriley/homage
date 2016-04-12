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

package me.oriley.homage;

import android.support.annotation.NonNull;
import android.text.Spanned;

final class License {

    @NonNull
    private final String mName;

    @NonNull
    private final String mUrl;

    @NonNull
    private final Spanned mDescription;


    License(@NonNull String name, @NonNull String url, @NonNull Spanned description) {
        mName = name;
        mUrl = url;
        mDescription = description;
    }


    @NonNull
    String getName() {
        return mName;
    }

    @NonNull
    String getUrl() {
        return mUrl;
    }

    @NonNull
    Spanned getDescription() {
        return mDescription;
    }
}