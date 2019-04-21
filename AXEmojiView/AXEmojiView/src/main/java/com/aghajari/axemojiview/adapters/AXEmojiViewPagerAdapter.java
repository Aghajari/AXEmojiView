package com.aghajari.axemojiview.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.shared.RecentEmoji;
import com.aghajari.axemojiview.shared.RecentEmojiManager;
import com.aghajari.axemojiview.shared.VariantEmoji;
import com.aghajari.axemojiview.view.AXEmojiRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AXEmojiViewPagerAdapter extends PagerAdapter {
    onEmojiEvents events;
    RecyclerView.OnScrollListener scrollListener;
    RecentEmoji recentEmoji;
    VariantEmoji variantEmoji;
    public List<AXEmojiRecyclerView> recyclerViews;
    public int add = 0;

    public AXEmojiViewPagerAdapter (onEmojiEvents events, RecyclerView.OnScrollListener scrollListener,
                                    RecentEmoji recentEmoji, VariantEmoji variantEmoji){
        this.events = events;
        this.scrollListener = scrollListener;
        this.recentEmoji = recentEmoji;
        this.variantEmoji = variantEmoji;
        recyclerViews = new ArrayList<AXEmojiRecyclerView>();
    }

    public RecyclerView.ItemDecoration itemDecoration = null;

    public Object instantiateItem(ViewGroup collection, int position) {

        AXEmojiRecyclerView recycler = new AXEmojiRecyclerView(collection.getContext());
        collection.addView(recycler);
        if (position==0 && add==1){
            recycler.setAdapter(new AXRecentEmojiRecyclerAdapter(recentEmoji, events,variantEmoji));
        }else {
            recycler.setAdapter(new AXEmojiRecyclerAdapter(AXEmojiManager.getInstance().getCategories()[position - add].getEmojis(),
                    events,variantEmoji));
        }
        recyclerViews.add(recycler);
        if (itemDecoration!=null) recycler.addItemDecoration(itemDecoration);
        if (scrollListener!=null) recycler.addOnScrollListener(scrollListener);
      return  recycler;
    }

    @Override
    public int getCount() {
        if (!recentEmoji.isEmptyA()) {add = 1;}else{add=0;}
        return AXEmojiManager.getInstance().getCategories().length+add;
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
