package com.aghajari.axemojiview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.R;
import com.aghajari.axemojiview.adapters.AXCategoryAdapter;

import com.aghajari.axemojiview.sticker.RecentSticker;
import com.aghajari.axemojiview.sticker.RecentStickerManager;
import com.aghajari.axemojiview.sticker.StickerCategory;
import com.aghajari.axemojiview.sticker.StickerLoader;
import com.aghajari.axemojiview.sticker.StickerProvider;
import com.aghajari.axemojiview.utils.Utils;

class AXCategoryRecycler extends AXEmojiLayout {

    RecentSticker recentStickerManager;
    public AXCategoryRecycler(Context context, AXEmojiLayout pager, StickerProvider provider, RecentSticker recentStickerManager) {
        super(context);
        this.recentStickerManager = recentStickerManager;
        this.pager = pager;
        init(provider);
    }

    AXEmojiLayout pager ;
    RecyclerView icons;
    View Divider;

    void init(StickerProvider provider){
        int iconSize = Utils.dpToPx(getContext(),24);

        icons = new RecyclerView(getContext());
        this.addView(icons,new AXEmojiLayout.LayoutParams(0,0,-1,-1));

        LinearLayoutManager lm =new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        icons.setLayoutManager(lm);

        icons.setItemAnimator(null);

        icons.setAdapter(new AXCategoryAdapter(pager,provider,recentStickerManager));

        icons.setOverScrollMode(View.OVER_SCROLL_NEVER);

        this.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getCategoryColor());

        this.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });


        Divider = new View(getContext());
        this.addView(Divider,new AXEmojiLayout.LayoutParams(
                0,Utils.dpToPx(getContext(),38),getContext().getResources().getDisplayMetrics().widthPixels,Utils.dpToPx(getContext(),1)));
        if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider())  Divider.setVisibility(GONE);
        Divider.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getDividerColor());
    }

    public void setPageIndex (int index){
        icons.getAdapter().notifyDataSetChanged();
    }

}
