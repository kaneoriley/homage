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

package me.oriley.homage.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;

import static java.util.Locale.US;

@SuppressWarnings("WeakerAccess")
public final class ResourceUtils {

    @SuppressWarnings("unused")
    public enum ResourceType {
        ANIM, ANIMATOR, BOOL, COLOR, DIMEN, DRAWABLE, ID, INTEGER, LAYOUT, MENU, MIPMAP, RAW, STRING, STYLE, STYLEABLE
    }

    private static final String TAG = ResourceUtils.class.getSimpleName();

    private static final int INVALID = -1;


    private ResourceUtils() {
        throw new IllegalAccessError("no instances");
    }


    public static int getResourceId(@NonNull Context context, @NonNull String name, @NonNull ResourceType type) {
        String typeName = type.name().toLowerCase(US);
        try {
            Class resourceClass = Class.forName(context.getPackageName() + ".R$" + typeName);
            return getResourceId(name, resourceClass);
        } catch (Exception e) {
            Log.e(TAG, "Exception reading field " + name + " for type " + typeName, e);
            return INVALID;
        }
    }

    public static int getResourceId(@NonNull String name, @NonNull Class<?> resourceClass) {
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
