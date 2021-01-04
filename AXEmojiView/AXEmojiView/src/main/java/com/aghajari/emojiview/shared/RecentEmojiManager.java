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


package com.aghajari.emojiview.shared;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RecentEmojiManager implements RecentEmoji {
    static String PREFERENCE_NAME = "emoji-recent-manager";
    static String RECENT_EMOJIS = "recent-saved-emojis";
    public static boolean FILL_DEFAULT_HISTORY = true;
    public static int MAX_RECENT = -1;

    private static HashMap<String, Integer> emojiUseHistory = new HashMap<>();
    private static ArrayList<Emoji> recentEmoji = new ArrayList<>();

    public static String[] FILL_DEFAULT_RECENT_DATA = new String[]{
            "\uD83D\uDE02", "\uD83D\uDE18", "\u2764", "\uD83D\uDE0D", "\uD83D\uDE0A", "\uD83D\uDE01",
            "\uD83D\uDC4D", "\u263A", "\uD83D\uDE14", "\uD83D\uDE04", "\uD83D\uDE2D", "\uD83D\uDC8B",
            "\uD83D\uDE12", "\uD83D\uDE33", "\uD83D\uDE1C", "\uD83D\uDE48", "\uD83D\uDE09", "\uD83D\uDE03",
            "\uD83D\uDE22", "\uD83D\uDE1D", "\uD83D\uDE31", "\uD83D\uDE21", "\uD83D\uDE0F", "\uD83D\uDE1E",
            "\uD83D\uDE05", "\uD83D\uDE1A", "\uD83D\uDE4A", "\uD83D\uDE0C", "\uD83D\uDE00", "\uD83D\uDE0B",
            "\uD83D\uDE06", "\uD83D\uDC4C", "\uD83D\uDE10", "\uD83D\uDE15"};

    @NonNull
    private final Context context;

    public static boolean isEmpty(Context context) {
        return emojiUseHistory.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        if (!emojiUseHistory.isEmpty()) return false;
        if (!AXEmojiManager.isShowingEmptyRecentEnabled()) {
            return true;
        }
        return false;
    }

    public RecentEmojiManager(@NonNull final Context context) {
        this.context = context.getApplicationContext();
        reload();
    }

    @NonNull
    @Override
    public Collection<Emoji> getRecentEmojis() {
        return recentEmoji;
    }

    @Override
    public void reload() {
        loadRecentEmoji();
    }

    @Override
    public void addEmoji(@NonNull final Emoji emoji) {
        addRecentEmoji(emoji.getBase());
    }

    @Override
    public void persist() {
        saveRecentEmoji();
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void addRecentEmoji(Emoji emoji) {
        Integer count = emojiUseHistory.get(emoji.getBase().getUnicode());
        if (count == null) {
            count = 0;
        }
        if (MAX_RECENT <= 0) MAX_RECENT = 48;
        if (count == 0 && emojiUseHistory.size() >= MAX_RECENT) {
            Emoji mEmoji = recentEmoji.get(recentEmoji.size() - 1);
            emojiUseHistory.remove(mEmoji.getBase().getUnicode());
            recentEmoji.set(recentEmoji.size() - 1, emoji.getBase());
        } else {
            if (!emojiUseHistory.containsKey(emoji.getBase().getUnicode()))
                recentEmoji.add(emoji.getBase());
        }
        emojiUseHistory.put(emoji.getBase().getUnicode(), ++count);
    }

    public void sortEmoji() {
        recentEmoji.clear();
        for (HashMap.Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            recentEmoji.add(AXEmojiManager.getInstance().findEmoji(entry.getKey()));
        }
        Collections.sort(recentEmoji, new Comparator<Emoji>() {
            @Override
            public int compare(Emoji lhs, Emoji rhs) {
                Integer count1 = emojiUseHistory.get(lhs.getBase().getUnicode());
                Integer count2 = emojiUseHistory.get(rhs.getBase().getUnicode());
                if (count1 == null) {
                    count1 = 0;
                }
                if (count2 == null) {
                    count2 = 0;
                }
                if (count1 > count2) {
                    return -1;
                } else if (count1 < count2) {
                    return 1;
                }
                return 0;
            }
        });
        if (MAX_RECENT <= 0) MAX_RECENT = 48;
        while (recentEmoji.size() > MAX_RECENT) {
            recentEmoji.remove(recentEmoji.size() - 1);
        }
    }

    public void saveRecentEmoji() {
        SharedPreferences preferences = this.getPreferences();
        StringBuilder stringBuilder = new StringBuilder();
        for (HashMap.Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
        }
        preferences.edit().putString(RECENT_EMOJIS, stringBuilder.toString()).commit();
    }

    public void clearRecentEmoji() {
        emojiUseHistory.clear();
        recentEmoji.clear();
        saveRecentEmoji();
    }

    public void loadRecentEmoji() {
        SharedPreferences preferences = getPreferences();

        String str;
        try {
            emojiUseHistory.clear();
            if (preferences.contains(RECENT_EMOJIS)) {
                str = preferences.getString(RECENT_EMOJIS, "");
                if (str != null && str.length() > 0) {
                    String[] args = str.split(",");
                    for (String arg : args) {
                        String[] args2 = arg.split("=");
                        emojiUseHistory.put(args2[0], parseInt(args2[1]));
                    }
                }
            }
            if (emojiUseHistory.isEmpty() && FILL_DEFAULT_HISTORY) {
                if (FILL_DEFAULT_RECENT_DATA != null && FILL_DEFAULT_RECENT_DATA.length != 0) {
                    for (int i = 0; i < FILL_DEFAULT_RECENT_DATA.length; i++) {
                        emojiUseHistory.put(FILL_DEFAULT_RECENT_DATA[i], FILL_DEFAULT_RECENT_DATA.length - i);
                    }
                    saveRecentEmoji();
                }
            }
            sortEmoji();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Integer parseInt(CharSequence value) {
        if (value == null) {
            return 0;
        }
        int val = 0;
        try {
            Matcher matcher = Pattern.compile("[\\-0-9]+").matcher(value);
            if (matcher.find()) {
                String num = matcher.group(0);
                val = Integer.parseInt(num);
            }
        } catch (Exception ignore) {
        }
        return val;
    }
}
