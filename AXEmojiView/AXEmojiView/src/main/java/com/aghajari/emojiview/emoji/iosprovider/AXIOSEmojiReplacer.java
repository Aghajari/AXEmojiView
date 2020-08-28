package com.aghajari.emojiview.emoji.iosprovider;

import java.util.ArrayList;
import java.util.List;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.utils.EmojiRange;
import com.aghajari.emojiview.utils.EmojiReplacer;
import com.aghajari.emojiview.emoji.iosprovider.AXIOSEmojiLoader.EmojiSpan;
import com.aghajari.emojiview.emoji.iosprovider.AXIOSEmojiLoader.SpanLocation;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.util.Log;


public abstract class AXIOSEmojiReplacer implements EmojiReplacer {
    @Override
    public void replaceWithImages(Context context, Spannable text, float emojiSize, Paint.FontMetrics fontMetrics, float defaultEmojiSize, EmojiReplacer fallback) {
       //AXEmojiLoader.replaceEmoji(text,fontMetrics,(int) emojiSize,false);
        if (text.length()==0) return;

       final AXEmojiManager emojiManager = AXEmojiManager.getInstance();
       final EmojiSpan[] existingSpans = text.getSpans(0, text.length(), EmojiSpan.class);
       final List<Integer> existingSpanPositions = new ArrayList<>(existingSpans.length);
       final int size = existingSpans.length;
       //noinspection ForLoopReplaceableByForEach
       for (int i = 0; i < size; i++) {
           existingSpanPositions.add(text.getSpanStart(existingSpans[i]));
       }
       if (existingSpanPositions.size()==0) {
    	   AXIOSEmojiLoader.replaceEmoji(text,fontMetrics,(int) emojiSize,false);
       }else {

       final List<EmojiRange> findAllEmojis = emojiManager.findAllEmojis(text);

       //noinspection ForLoopReplaceableByForEach
       for (int i = 0; i < findAllEmojis.size(); i++) {
           final EmojiRange location = findAllEmojis.get(i);

           if (!existingSpanPositions.contains(location.start)) {
        	   List<SpanLocation> list = AXIOSEmojiLoader.replaceEmoji2(location.emoji.getUnicode(), fontMetrics,(int) emojiSize, false, null);
               for (SpanLocation l : list) {
            	   l.start = location.start+l.start;
            	   l.end = location.start+l.end;
            	   if (!existingSpanPositions.contains(l.start)) {
            	   text.setSpan(l.span,l.start,l.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
                   }
               }
           }
       }
       }
    }
}
