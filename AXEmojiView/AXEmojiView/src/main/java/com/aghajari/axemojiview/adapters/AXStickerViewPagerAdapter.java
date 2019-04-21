package com.aghajari.axemojiview.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.sticker.RecentSticker;
import com.aghajari.axemojiview.sticker.RecentStickerManager;
import com.aghajari.axemojiview.sticker.Sticker;
import com.aghajari.axemojiview.sticker.StickerProvider;
import com.aghajari.axemojiview.view.AXStickerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AXStickerViewPagerAdapter extends PagerAdapter {
    onStickerEvents events;
    RecyclerView.OnScrollListener scrollListener;
    public List<View> recyclerViews;
    public int add = 0;
    StickerProvider provider;
    RecentSticker recent;
    public AXStickerViewPagerAdapter(onStickerEvents events, RecyclerView.OnScrollListener scrollListener, StickerProvider provider,RecentSticker recentStickerManager){
        this.events = events;
        this.scrollListener = scrollListener;
        recyclerViews = new ArrayList<View>();
        this.provider = provider;
        this.recent = recentStickerManager;
    }

    public RecyclerView.ItemDecoration itemDecoration = null;

    public Object instantiateItem(ViewGroup collection, int position) {
        if ((position==0&&add==1) || !provider.getCategories()[position-add].useCustomView()) {
            AXStickerRecyclerView recycler = new AXStickerRecyclerView(collection.getContext());
            collection.addView(recycler);
            if (position == 0 && add == 1) {
                recycler.setAdapter(new AXRecentStickerRecyclerAdapter(recent, events, provider.getLoader()));
            } else {
                recycler.setAdapter(new AXStickerRecyclerAdapter(provider.getCategories()[position - add].getStickers(), events, provider.getLoader()));
            }
            recyclerViews.add(recycler);
            if (itemDecoration != null) recycler.addItemDecoration(itemDecoration);
            if (scrollListener != null) recycler.addOnScrollListener(scrollListener);
            if (position!=0||add==0) provider.getCategories()[position-add].bindView(recycler);
            return recycler;
        }else{
            View view = provider.getCategories()[position-add].getView(collection);
            collection.addView(view);
            provider.getCategories()[position-add].bindView(view);
            recyclerViews.add(view);
            if (view instanceof RecyclerView) {
                if (itemDecoration != null) ((RecyclerView)view).addItemDecoration(itemDecoration);
                if (scrollListener != null) ((RecyclerView)view).addOnScrollListener(scrollListener);
            }
            return view;
        }
    }

    @Override
    public int getCount() {
        if (!recent.isEmptyA() && provider.isRecentEnabled()) {add = 1;}else{add=0;}
        return provider.getCategories().length + add;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
