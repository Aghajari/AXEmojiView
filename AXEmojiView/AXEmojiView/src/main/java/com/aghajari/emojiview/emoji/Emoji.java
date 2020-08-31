/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.emojiview.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Emoji implements Serializable {
    private static final long serialVersionUID = 3L;
    private static final List<Emoji> EMPTY_EMOJI_LIST = emptyList();

    @NonNull
    private final String unicode;
    @DrawableRes
    private final int resource;
    @NonNull
    private List<Emoji> variants;
    @Nullable
    private Emoji base;

    public Emoji(@NonNull final int[] codePoints, @DrawableRes final int resource) {
        this(codePoints, resource, new Emoji[0]);
    }

    public Emoji(final int codePoint, @DrawableRes final int resource) {
        this(codePoint, resource, new Emoji[0]);
    }

    public Emoji(final int codePoint, @DrawableRes final int resource,
                 final Emoji... variants) {
        this(new int[]{codePoint}, resource, variants);
    }

    public Emoji(@NonNull final int[] codePoints, @DrawableRes final int resource, final Emoji... variants) {
        this.unicode = new String(codePoints, 0, codePoints.length);
        this.resource = resource;
        this.variants = variants.length == 0 ? EMPTY_EMOJI_LIST : asList(variants);
        for (final Emoji variant : variants) {
            variant.base = this;
        }
    }

    public Emoji(String code, int resource, final Emoji[] variants) {
        this.unicode = code;
        this.resource = resource;
        this.variants = variants.length == 0 ? EMPTY_EMOJI_LIST : asList(variants);
        for (final Emoji variant : variants) {
            variant.base = this;
        }
    }

    public Emoji(String code, int resource) {
        this.unicode = code;
        this.resource = resource;
        this.variants = EMPTY_EMOJI_LIST;
    }

    public void setVariants(Emoji[] variants) {
        this.variants = variants.length == 0 ? EMPTY_EMOJI_LIST : asList(variants);
        for (final Emoji variant : variants) {
            variant.base = this;
        }
    }

    @NonNull
    public String getUnicode() {
        return unicode;
    }

    /**
     * @deprecated Please migrate to getDrawable(). May return -1.
     */
    @Deprecated
    @DrawableRes
    public int getResource() {
        return resource;
    }

    @NonNull
    public Drawable getDrawable(final Context context) {
        return AppCompatResources.getDrawable(context, resource);
    }

    @NonNull
    public Drawable getDrawable(View view) {
        return AppCompatResources.getDrawable(view.getContext(), resource);
    }

    /**
     * @return other variants of this emoji
     */
    @NonNull
    public List<Emoji> getVariants() {
        return new ArrayList<>(variants);
    }


    /**
     * @return the base of emoji, or this instance if it doesn't have any other base
     */
    @NonNull
    public Emoji getBase() {
        Emoji result = this;

        while (result.base != null) {
            result = result.base;
        }

        return result;
    }

    public int getLength() {
        return unicode.length();
    }

    public boolean hasVariants() {
        return !variants.isEmpty();
    }

    public void destroy() {
        // For inheritors to override.
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Emoji emoji = (Emoji) o;
        return unicode.equals(emoji.unicode);
    }

    @Override
    public int hashCode() {
        int result = unicode.hashCode();
        result = 31 * result + resource;
        result = 31 * result + variants.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getUnicode();
    }

    public Bitmap getEmojiBitmap() {
        return null;
    }

    public boolean isLoading() {
        return false;
    }


}
