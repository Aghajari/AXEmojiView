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

import java.util.Collection;

/**
 * Interface for providing some custom implementation for recent stickers.
 */
@SuppressWarnings("rawtypes")
public interface RecentSticker {
    /**
     * Returns the recent sticker. Could be loaded from a database, shared preferences or just hard
     * coded.<br>
     * <p>
     * This method will be called more than one time hence it is recommended to hold a collection of
     * recent sticker.
     */
    @NonNull
    Collection<Sticker> getRecentStickers();

    /**
     * Should add the sticker to the recent ones. After calling this method, {@link #getRecentStickers()}
     * should return the sticker that was just added.
     */
    void addSticker(@NonNull Sticker sticker);

    /**
     * Should persist all sticker.
     */
    void persist();

    /**
     * Returns true if recent is empty
     */
    boolean isEmpty();
}
