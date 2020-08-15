package com.aghajari.emojiview.view;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.adapters.AXCategoryAdapter;

import com.aghajari.emojiview.sticker.RecentSticker;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.aghajari.emojiview.utils.Utils;

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
       // int iconSize = Utils.dpToPx(getContext(),24);

        icons = new RecyclerView(getContext());
        this.addView(icons,new LayoutParams(0,0,-1,-1));

        LinearLayoutManager lm =new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        icons.setLayoutManager(lm);

        icons.setItemAnimator(null);

        icons.setAdapter(new AXCategoryAdapter(pager,provider,recentStickerManager));

        icons.setOverScrollMode(View.OVER_SCROLL_NEVER);

        this.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getCategoryColor());

        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });


        Divider = new View(getContext());
        this.addView(Divider,new LayoutParams(
                0,Utils.dpToPx(getContext(),38),getContext().getResources().getDisplayMetrics().widthPixels,Utils.dpToPx(getContext(),1)));
        if (!AXEmojiManager.getStickerViewTheme().shouldShowAlwaysDivider())  Divider.setVisibility(GONE);
        Divider.setBackgroundColor(AXEmojiManager.getStickerViewTheme().getDividerColor());
    }

    public void setPageIndex (int index){
        icons.getAdapter().notifyDataSetChanged();
    }

}
