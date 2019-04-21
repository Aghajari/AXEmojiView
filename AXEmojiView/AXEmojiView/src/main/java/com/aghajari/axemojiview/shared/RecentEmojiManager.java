package com.aghajari.axemojiview.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public final class RecentEmojiManager implements RecentEmoji {
   static  String PREFERENCE_NAME = "emoji-recent-manager";
   static  String TIME_DELIMITER = ";";
   static  String EMOJI_DELIMITER = "~";
   static  String RECENT_EMOJIS = "recent-emojis";
   static  int EMOJI_GUESS_SIZE = 5;
   public static  int MAX_RECENTS = -1;

  @NonNull public final Context context;
  @NonNull public EmojiList emojiList = new EmojiList(0);

  public static boolean isEmpty(Context context){
    final String savedRecentEmojis = context.getApplicationContext()
            .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(RECENT_EMOJIS, "");
    if (savedRecentEmojis.length() > 0) return  false;
    return  true;
  }

  @Override
  public boolean isEmptyA(){
    final String savedRecentEmojis = context.getApplicationContext()
            .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(RECENT_EMOJIS, "");
    if (savedRecentEmojis.length() > 0) return  false;
    if (AXEmojiManager.getInstance().isShowEmptyRecent()==false) {
      return true;
    }
    return  false;
  }

  public RecentEmojiManager(@NonNull final Context context) {
    this.context = context.getApplicationContext();
  }
@NonNull @Override public Collection<Emoji> getRecentEmojis() {
    if (emojiList.size() == 0) {
      final String savedRecentEmojis = getPreferences().getString(RECENT_EMOJIS, "");

      if (savedRecentEmojis.length() > 0) {
        final StringTokenizer stringTokenizer = new StringTokenizer(savedRecentEmojis, EMOJI_DELIMITER);
        emojiList = new EmojiList(stringTokenizer.countTokens());

        while (stringTokenizer.hasMoreTokens()) {
          final String token = stringTokenizer.nextToken();

          final String[] parts = token.split(TIME_DELIMITER);

          if (parts.length == 2) {
            final Emoji emoji = AXEmojiManager.getInstance().findEmoji(parts[0]);

            if (emoji != null && emoji.getLength() == parts[0].length()) {
              final long timestamp = Long.parseLong(parts[1]);

              emojiList.add(emoji, timestamp);
            }
          }
        }
      } else {
        emojiList = new EmojiList(0);
      }
    }

    return emojiList.getEmojis();
  }

  @Override public void addEmoji(@NonNull final Emoji emoji) {
    emojiList.add(emoji);
  }

  @Override public void persist() {
    if (emojiList.size() > 0) {
      final StringBuilder stringBuilder = new StringBuilder(emojiList.size() * EMOJI_GUESS_SIZE);

      for (int i = 0; i < emojiList.size(); i++) {
        final Data data = emojiList.get(i);
        stringBuilder.append(data.emoji.getUnicode())
            .append(TIME_DELIMITER)
            .append(data.timestamp)
            .append(EMOJI_DELIMITER);
      }

      stringBuilder.setLength(stringBuilder.length() - EMOJI_DELIMITER.length());

      getPreferences().edit().putString(RECENT_EMOJIS, stringBuilder.toString()).apply();
    }
  }

  @SuppressWarnings("deprecation")
public void RemoveEmojiFromRecent(String Unicode,int Resource) {
	    if (emojiList.size() > 0) {
	        final StringBuilder stringBuilder = new StringBuilder(emojiList.size() * EMOJI_GUESS_SIZE);

	        for (int i = 0; i < emojiList.size(); i++) {
	          final Data data = emojiList.get(i);
	          if (data.emoji.getBase().getUnicode()==Unicode && data.emoji.getBase().getResource()==Resource) {
	          }else {
	          stringBuilder.append(data.emoji.getUnicode())
	              .append(TIME_DELIMITER)
	              .append(data.timestamp)
	              .append(EMOJI_DELIMITER);
	          }
	        }

	        stringBuilder.setLength(stringBuilder.length() - EMOJI_DELIMITER.length());

	        getPreferences().edit().putString(RECENT_EMOJIS, stringBuilder.toString()).apply();
	      }
  }
  
  private SharedPreferences getPreferences() {
    return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
  }

  public static class EmojiList {
    static final Comparator<Data> COMPARATOR = new Comparator<Data>() {
      @Override public int compare(final Data lhs, final Data rhs) {
        return Long.valueOf(rhs.timestamp).compareTo(lhs.timestamp);
      }
    };

    @NonNull public final List<Data> emojis;

    public void Remove(int position) {
    	emojis.remove(position);
    }
    public void clear() {
    	emojis.clear();
    }
    
    EmojiList(final int size) {
      emojis = new ArrayList<>(size);
    }

    public void add(final Emoji emoji) {
      add(emoji, System.currentTimeMillis());
    }

    public void add(final Emoji emoji, final long timestamp) {
      final Iterator<Data> iterator = emojis.iterator();

      final Emoji emojiBase = emoji.getBase();

      while (iterator.hasNext()) {
        final Data data = iterator.next();
        if (data.emoji.getBase().equals(emojiBase)) { // Do the comparison by base so that skin tones are only saved once.
          iterator.remove();
        }
      }

      emojis.add(0, new Data(emoji, timestamp));

      if (emojis.size() > MAX_RECENTS) {
        emojis.remove(MAX_RECENTS);
      }
    }

    Collection<Emoji> getEmojis() {
      Collections.sort(emojis, COMPARATOR);

      final Collection<Emoji> sortedEmojis = new ArrayList<>(emojis.size());

      for (final Data data : emojis) {
        sortedEmojis.add(data.emoji);
      }

      return sortedEmojis;
    }

   public int size() {
      return emojis.size();
    }

   public Data get(final int index) {
      return emojis.get(index);
    }
  }

  public static class Data {
    public final Emoji emoji;
    public final long timestamp;

    Data(final Emoji emoji, final long timestamp) {
      this.emoji = emoji;
      this.timestamp = timestamp;
    }
  }
}
