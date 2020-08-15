package com.aghajari.sample.emojiview.sticker;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.aghajari.sample.emojiview.R;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;

public class WhatsAppStickers implements StickerCategory<Integer> {
    @NonNull
    @Override
    public Sticker[] getStickers() {
        return new Sticker[]{
                new Sticker<Integer>(R.drawable.sticker),
                new Sticker<Integer>(R.drawable.sticker1),
                new Sticker<Integer>(R.drawable.sticker2),
                new Sticker<Integer>(R.drawable.sticker3),
                new Sticker<Integer>(R.drawable.sticker4),
                new Sticker<Integer>(R.drawable.sticker5),
                new Sticker<Integer>(R.drawable.sticker6),
                new Sticker<Integer>(R.drawable.sticker7),
                new Sticker<Integer>(R.drawable.sticker8),
                new Sticker<Integer>(R.drawable.sticker9),
                new Sticker<Integer>(R.drawable.sticker10),
                new Sticker<Integer>(R.drawable.sticker11),
                new Sticker<Integer>(R.drawable.sticker12),
                new Sticker<Integer>(R.drawable.sticker13),
                new Sticker<Integer>(R.drawable.sticker14),
                new Sticker<Integer>(R.drawable.sticker15)
        };
    }

    @Override
    public Integer getCategoryData() {
        return R.drawable.sticker;
    }

    @Override
    public boolean useCustomView() {
        return false;
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view) {}

    @Override
    public View getEmptyView(ViewGroup viewGroup) {
        return null;
    }
}
