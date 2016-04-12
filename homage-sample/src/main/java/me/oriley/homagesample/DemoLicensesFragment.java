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

package me.oriley.homagesample;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import me.oriley.homage.recyclerview.HomageAdapter;

@SuppressWarnings("WeakerAccess")
public final class DemoLicensesFragment extends RecyclerViewFragment {

    @NonNull
    @Override
    public RecyclerView.Adapter createAdapter() {
        HomageAdapter adapter = new HomageAdapter(getActivity(), R.raw.licenses);

        // Adds a custom license definition to enable matching in your JSON list
        adapter.addLicense("oriley", R.string.license_oriley_name, R.string.license_oriley_url, R.string.license_oriley_description);

        adapter.refresh();
        return adapter;
    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }
}
