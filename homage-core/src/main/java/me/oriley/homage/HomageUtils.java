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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

final class HomageUtils {

    private static final String TAG = HomageUtils.class.getSimpleName();

    private static final String JSON_KEY_LICENSES = "licenses";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_ICON = "icon";
    private static final String JSON_KEY_VERSION = "version";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_YEAR = "year";
    private static final String JSON_KEY_OWNER = "owner";
    private static final String JSON_KEY_URL = "url";
    private static final String JSON_KEY_LICENSE = "license";

    private static final int INVALID = -1;


    private HomageUtils() {
        throw new IllegalAccessError("no instances");
    }


    @Nullable
    static Library[] parseLibraries(@NonNull InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONObject json = new JSONObject(jsonBuilder.toString());

            List<Library> libraries = new ArrayList<>();
            JSONArray array = json.getJSONArray(JSON_KEY_LICENSES);
            for (int i = 0 ; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                libraries.add(parseLibrary(object));
            }

            return libraries.toArray(new Library[libraries.size()]);
        } catch (IOException e) {
            Log.e(TAG, "Exception reading input stream", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "Exception parsing JSON", e);
            return null;
        } finally {
            closeQuietly(reader);
        }
    }

    @NonNull
    private static Library parseLibrary(@NonNull JSONObject json) throws JSONException {
        String name = getOptionalString(json, JSON_KEY_NAME);
        String icon = getOptionalString(json, JSON_KEY_ICON);
        String version = getOptionalString(json, JSON_KEY_VERSION);
        String description = getOptionalString(json, JSON_KEY_DESCRIPTION);
        String year = getOptionalString(json, JSON_KEY_YEAR);
        String owner = getOptionalString(json, JSON_KEY_OWNER);
        String url = getOptionalString(json, JSON_KEY_URL);
        String license = getOptionalString(json, JSON_KEY_LICENSE);
        return new Library(name, icon, version, description, year, owner, url, license);
    }

    @Nullable
    private static String getOptionalString(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            return getString(jsonObject, key);
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    private static String getString(@NonNull JSONObject jsonObject, @NonNull String key) throws JSONException {
        return jsonObject.getString(key);
    }

    private static void closeQuietly(@Nullable Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

    static int getResourceId(@NonNull Context context, @NonNull String name, @NonNull String resourceType) {
        try {
            Class resourceClass = Class.forName(context.getPackageName() + ".R$" + resourceType);
            return getResourceId(name, resourceClass);
        } catch (Exception e) {
            Log.e(TAG, "Exception reading field " + name + " for type " + resourceType, e);
            return INVALID;
        }
    }

    static int getResourceId(@NonNull String name, @NonNull Class<?> resourceClass) {
        try {
            Field field = resourceClass.getDeclaredField(name);
            field.setAccessible(true);
            return field.getInt(field);
        } catch (Exception e) {
            Log.e(TAG, "Exception reading field " + name + " for class " + resourceClass.getName(), e);
            return INVALID;
        }
    }
}
