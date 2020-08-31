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

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * Interface for defining emoji category.
 */
public interface EmojiCategory {
    /**
     * Returns all of the emojis it can display.
     */
    @NonNull
    Emoji[] getEmojis();

    /**
     * Returns the icon of the category that should be displayed.
     */
    @DrawableRes
    int getIcon();

    /**
     * Returns title of the category
     */
    CharSequence getTitle();
}
