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


package com.aghajari.emojiview.sticker;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;

/**
 * Interface for defining a category.
 */
public interface StickerCategory<T> {
    /**
     * Returns all of the stickers it can display.
     */
    @SuppressWarnings("rawtypes")
    @NonNull
    Sticker[] getStickers();

    /**
     * Returns the icon of the category that should be displayed.
     */
    T getCategoryData();

    /**
     * Return true if you want to build your own CustomView
     */
    boolean useCustomView();

    /**
     * @return add custom view
     */
    View getView(ViewGroup viewGroup);

    /**
     * update your custom view
     */
    void bindView(View view);

    /**
     * set an emptyView if you aren't using custom view
     */
    View getEmptyView(ViewGroup viewGroup);
}
