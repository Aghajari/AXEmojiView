package com.aghajari.emojiview.utils;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;

public interface EmojiReplacer {
  void replaceWithImages(Context context, Spannable text, float emojiSize, Paint.FontMetrics fontMetrics, float defaultEmojiSize, EmojiReplacer fallback);
}
