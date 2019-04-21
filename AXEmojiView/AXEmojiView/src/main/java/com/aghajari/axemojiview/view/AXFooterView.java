package com.aghajari.axemojiview.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.AXEmojiUtils;
import com.aghajari.axemojiview.R;
import com.aghajari.axemojiview.adapters.AXFooterIconsAdapter;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.emoji.EmojiCategory;
import com.aghajari.axemojiview.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AXFooterView extends AXEmojiLayout {
    public AXFooterView(Context context, AXEmojiPager pager,int Left) {
        super(context);

        this.Left = Left;
        this.pager = pager;
        init();
    }

    int Left;
    AXEmojiPager pager ;
    RecyclerView icons;
    AppCompatImageView backSpace;
    AppCompatImageView leftIcon;

    private void init(){
        int iconSize = Utils.dpToPx(getContext(),24);
        backSpace = new AppCompatImageView(getContext());
        this.addView(backSpace,new LayoutParams(
                getContext().getResources().getDisplayMetrics().widthPixels-Utils.dpToPx(getContext(),38),Utils.dpToPx(getContext(),10),iconSize,iconSize));

        Drawable back = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.emoji_backspace));
        DrawableCompat.setTint(DrawableCompat.wrap(back),AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
        backSpace.setImageDrawable(back);
        Utils.setClickEffect(backSpace,true);

        backSpace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (pager.getEditText()!=null) AXEmojiUtils.backspace(pager.getEditText());
                if (pager.listener!=null) pager.listener.onClick(false);
            }
        });

        if (Left!=-1){
            leftIcon = new AppCompatImageView(getContext());
            this.addView(leftIcon,new LayoutParams(Utils.dpToPx(getContext(),8),Utils.dpToPx(getContext(),10),iconSize,iconSize));

            Drawable leftIconDr = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),Left));
            DrawableCompat.setTint(DrawableCompat.wrap(leftIconDr),AXEmojiManager.getEmojiViewTheme().getFooterItemColor());
            leftIcon.setImageDrawable(leftIconDr);
            Utils.setClickEffect(leftIcon,true);

            leftIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (pager.listener!=null) pager.listener.onClick(true);
                }
            });
        }

        icons = new RecyclerView(getContext());
        this.addView(icons,new AXEmojiLayout.LayoutParams(
                Utils.dpToPx(getContext(),44),0,getContext().getResources().getDisplayMetrics().widthPixels-Utils.dpToPx(getContext(),44),-1));

        LinearLayoutManager lm =new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        icons.setLayoutManager(lm);

        icons.setItemAnimator(null);

        icons.setAdapter(new AXFooterIconsAdapter(pager));

        icons.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (icons.getLayoutParams().width>pager.getPagesCount()*Utils.dpToPx(getContext(),40)){
            icons.getLayoutParams().width=pager.getPagesCount()*Utils.dpToPx(getContext(),40);
            ((LayoutParams)icons.getLayoutParams()).left =
                    (getContext().getResources().getDisplayMetrics().widthPixels/2) - (icons.getLayoutParams().width/2);
            this.requestLayout();
        }

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getFooterBackgroundColor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(Utils.dpToPx(getContext(),2));
        }

        this.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setPageIndex (int index){
        icons.getAdapter().notifyDataSetChanged();
    }

}
