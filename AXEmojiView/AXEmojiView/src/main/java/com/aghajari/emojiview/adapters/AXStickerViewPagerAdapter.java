/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.emojiview.adapters;

import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aghajari.emojiview.listener.OnStickerActions;
import com.aghajari.emojiview.sticker.RecentSticker;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.aghajari.emojiview.view.AXStickerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AXStickerViewPagerAdapter extends PagerAdapter {
    OnStickerActions events;
    RecyclerView.OnScrollListener scrollListener;
    public List<View> recyclerViews;
    public int add = 0;
    StickerProvider provider;
    RecentSticker recent;

    public AXStickerViewPagerAdapter(OnStickerActions events, RecyclerView.OnScrollListener scrollListener, StickerProvider provider, RecentSticker recentStickerManager) {
        this.events = events;
        this.scrollListener = scrollListener;
        recyclerViews = new ArrayList<View>();
        this.provider = provider;
        this.recent = recentStickerManager;
    }

    public RecyclerView.ItemDecoration itemDecoration = null;

    public Object instantiateItem(ViewGroup collection, int position) {
        if ((position == 0 && add == 1) || !provider.getCategories()[position - add].useCustomView()) {
            AXStickerRecyclerView recycler = new AXStickerRecyclerView(collection.getContext());
            collection.addView(recycler);
            if (position == 0 && add == 1) {
                recycler.setAdapter(new AXRecentStickerRecyclerAdapter(recent, events, provider.getLoader()));
            } else {
                recycler.setAdapter(new AXStickerRecyclerAdapter(provider.getCategories()[position - add]
                        , provider.getCategories()[position - add].getStickers(), events, provider.getLoader()
                        , provider.getCategories()[position - add].getEmptyView(collection)));
            }
            recyclerViews.add(recycler);
            if (itemDecoration != null) recycler.addItemDecoration(itemDecoration);
            if (scrollListener != null) recycler.addOnScrollListener(scrollListener);
            if (position != 0 || add == 0)
                provider.getCategories()[position - add].bindView(recycler);
            return recycler;
        } else {
            View view = provider.getCategories()[position - add].getView(collection);
            collection.addView(view);
            provider.getCategories()[position - add].bindView(view);
            recyclerViews.add(view);
            if (view instanceof RecyclerView) {
                if (itemDecoration != null) ((RecyclerView) view).addItemDecoration(itemDecoration);
                if (scrollListener != null)
                    ((RecyclerView) view).addOnScrollListener(scrollListener);
            }
            return view;
        }
    }

    @Override
    public int getCount() {
        if (!recent.isEmpty() && provider.isRecentEnabled()) {
            add = 1;
        } else {
            add = 0;
        }
        return provider.getCategories().length + add;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        recyclerViews.remove(object);
    }
}
