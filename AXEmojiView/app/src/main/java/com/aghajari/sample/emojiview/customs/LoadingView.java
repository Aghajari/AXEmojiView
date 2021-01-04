package com.aghajari.sample.emojiview.customs;

import android.content.Context;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiLayout;

public class LoadingView extends AXEmojiLayout {

    ProgressBar progressBar;
    public LoadingView(Context context) {
        super(context);
        progressBar = new ProgressBar(context);
        LayoutParams lp = new LayoutParams(Utils.dpToPx(getContext(),44),Utils.dpToPx(getContext(),44));
        lp.gravity = Gravity.CENTER;
        lp.bottomMargin = lp.width/2;
        this.addView(progressBar,lp);
        progressBar.setIndeterminate(true);
    }
}
