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

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.AXEmojiManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;


@SuppressWarnings("rawtypes")
public final class RecentStickerManager implements RecentSticker {
    private static String PREFERENCE_NAME = "recent-manager";
    static String TIME_DELIMITER = ";";
    static String EMOJI_DELIMITER = "~";
    private static String RECENT_STICKER = "recents";
    static int EMOJI_GUESS_SIZE = 5;
    public static int MAX_RECENTS = -1;

    final Context context;
    StickerList StickersList = new StickerList(0);

    public static boolean isEmpty(Context context) {
        final String savedRecentEmojis = context.getApplicationContext()
                .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(RECENT_STICKER, "");
        if (savedRecentEmojis.length() > 0) return false;
        return true;
    }

    @Override
    public boolean isEmpty() {
        final String savedRecentEmojis = context.getApplicationContext()
                .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(RECENT_STICKER, "");
        if (savedRecentEmojis.length() > 0) return false;
        if (AXEmojiManager.isShowingEmptyRecentEnabled() == false) {
            return true;
        }
        return false;
    }

    public RecentStickerManager(@NonNull final Context context, final String type) {
        RECENT_STICKER = type + "-recents";
        PREFERENCE_NAME = type + "-recent-manager";
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public Collection<Sticker> getRecentStickers() {
        if (StickersList.size() == 0) {
            final String savedRecentEmojis = getPreferences().getString(RECENT_STICKER, "");

            if (savedRecentEmojis.length() > 0) {
                final StringTokenizer stringTokenizer = new StringTokenizer(savedRecentEmojis, EMOJI_DELIMITER);
                StickersList = new StickerList(stringTokenizer.countTokens());

                while (stringTokenizer.hasMoreTokens()) {
                    final String token = stringTokenizer.nextToken();

                    final String[] parts = token.split(TIME_DELIMITER);

                    if (parts.length == 2) {
                        final Sticker sticker = Sticker.load(parts[0]);
                        if (sticker != null && sticker.getData() != null && sticker.getData().toString().length() > 0) {
                            final long timestamp = Long.parseLong(parts[1]);

                            StickersList.add(sticker, timestamp);
                        }
                    }
                }
            } else {
                StickersList = new StickerList(0);
            }
        }

        return StickersList.getStickers();
    }

    @Override
    public void addSticker(@NonNull final Sticker sticker) {
        StickersList.add(sticker);
    }

    @Override
    public void persist() {
        if (StickersList.size() > 0) {
            final StringBuilder stringBuilder = new StringBuilder(StickersList.size() * EMOJI_GUESS_SIZE);

            for (int i = 0; i < StickersList.size(); i++) {
                final Data data = StickersList.get(i);
                stringBuilder.append(Sticker.toString(data.Sticker))
                        .append(TIME_DELIMITER)
                        .append(data.timestamp)
                        .append(EMOJI_DELIMITER);
            }

            stringBuilder.setLength(stringBuilder.length() - EMOJI_DELIMITER.length());

            getPreferences().edit().putString(RECENT_STICKER, stringBuilder.toString()).apply();
        }
    }

    public void clear() {
        getPreferences().edit().putString(RECENT_STICKER, "").apply();
        StickersList.clear();
    }

    public void removeStickerFromRecent(Sticker sticker) {
        if (StickersList.size() > 0) {
            final StringBuilder stringBuilder = new StringBuilder(StickersList.size() * EMOJI_GUESS_SIZE);

            for (int i = 0; i < StickersList.size(); i++) {
                final Data data = StickersList.get(i);
                if (data.Sticker.equals(sticker)) {
                } else {
                    stringBuilder.append(Sticker.toString(data.Sticker))
                            .append(TIME_DELIMITER)
                            .append(data.timestamp)
                            .append(EMOJI_DELIMITER);
                }
            }

            stringBuilder.setLength(stringBuilder.length() - EMOJI_DELIMITER.length());

            getPreferences().edit().putString(RECENT_STICKER, stringBuilder.toString()).apply();
        }
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    static class StickerList {
        static final Comparator<Data> COMPARATOR = new Comparator<Data>() {
            @Override
            public int compare(final Data lhs, final Data rhs) {
                return Long.valueOf(rhs.timestamp).compareTo(lhs.timestamp);
            }
        };

        @NonNull
        public final List<Data> Stickers;

        public void remove(int position) {
            Stickers.remove(position);
        }

        public void clear() {
            Stickers.clear();
        }

        StickerList(final int size) {
            Stickers = new ArrayList<>(size);
        }

        public void add(final Sticker Sticker) {
            add(Sticker, System.currentTimeMillis());
        }

        public void add(final Sticker Sticker, final long timestamp) {
            boolean exit = false;
            for (int i = 0; i < Stickers.size(); i++) {
                Data data = Stickers.get(i);
                if (data.Sticker.equals(Sticker)) {
                    data.timestamp = timestamp;
                    exit = true;
                    break;
                }
            }
            if (exit) return;
            Stickers.add(0, new Data(Sticker, timestamp));

            if (Stickers.size() > MAX_RECENTS) {
                Stickers.remove(MAX_RECENTS);
            }
        }

        Collection<Sticker> getStickers() {
            Collections.sort(Stickers, COMPARATOR);

            final Collection<Sticker> sortedEmojis = new ArrayList<>(Stickers.size());

            for (final Data data : Stickers) {
                sortedEmojis.add(data.Sticker);
            }

            return sortedEmojis;
        }

        public int size() {
            return Stickers.size();
        }

        public Data get(final int index) {
            return Stickers.get(index);
        }
    }

    static class Data {
        public final Sticker Sticker;
        public long timestamp;

        Data(final Sticker Sticker, final long timestamp) {
            this.Sticker = Sticker;
            this.timestamp = timestamp;
        }
    }
}
