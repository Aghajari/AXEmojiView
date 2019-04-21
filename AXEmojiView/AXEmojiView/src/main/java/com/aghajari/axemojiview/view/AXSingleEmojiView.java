package com.aghajari.axemojiview.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.aghajari.axemojiview.AXEmojiManager;
import com.aghajari.axemojiview.AXEmojiUtils;
import com.aghajari.axemojiview.adapters.AXEmojiViewPagerAdapter;
import com.aghajari.axemojiview.adapters.AXSingleEmojiPageAdapter;
import com.aghajari.axemojiview.adapters.onEmojiEvents;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.listener.onEmojiActions;
import com.aghajari.axemojiview.shared.RecentEmoji;
import com.aghajari.axemojiview.shared.RecentEmojiManager;
import com.aghajari.axemojiview.shared.VariantEmoji;
import com.aghajari.axemojiview.shared.VariantEmojiManager;
import com.aghajari.axemojiview.utils.Utils;

public class AXSingleEmojiView extends  AXEmojiLayout {
    public AXSingleEmojiView(Context context) {
        super(context);
        init();
    }

    AXCategoryViews categoryViews;
    AXEmojiSingleRecyclerView recyclerView;
    RecentEmoji recent;
    VariantEmoji variant;
    AXEmojiVariantPopup variantPopup;

    onEmojiEvents events = new onEmojiEvents() {
        @Override
        public void onClick(AXEmojiImageView view,boolean fromRecent,boolean fromVariant) {
            if (!fromVariant && variantPopup!=null && variantPopup.isShowing) return;
            Emoji emoji = view.currentEmoji;
            recent.addEmoji(emoji);
            if (editText!=null) AXEmojiUtils.input(editText,emoji);

            variant.addVariant(emoji);
            if (variantPopup!=null) variantPopup.dismiss();

            if (emojiActions!=null) emojiActions.onClick(view,view.currentEmoji,fromRecent,fromVariant);
        }

        @Override
        public void onLongClick(AXEmojiImageView view,boolean fromRecent,boolean fromVariant) {

            if ((!fromRecent || AXEmojiManager.getInstance().isRecentVariantEnabled()) && variantPopup!=null) {
                if (view.currentEmoji.getBase().hasVariants())
                    variantPopup.show(view, view.currentEmoji);
            }

            if (emojiActions!=null) emojiActions.onLongClick(view,view.currentEmoji,fromRecent,fromVariant);
        }
    };

    onEmojiActions emojiActions=null;

    /**
     * add emoji click and longClick listener
     * @param listener
     */
    public void setOnEmojiActionsListener(onEmojiActions listener){
        emojiActions = listener;
    }
    public void removeOnEmojiActionsListener(){
        emojiActions = null;
    }



    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        private int[] firstPositions;

        private boolean isShowing = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView==null){
                if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                }
                return;
            }
            if (dy==0) return;
            if (dy==1) dy=0;
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
                if (firstPositions == null) {
                    firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                int firstVisibleItemPosition = findMin(firstPositions);
                int visibleItemCount = layoutManager.getChildCount();
                if ((visibleItemCount > 0 && (firstVisibleItemPosition) == 0)) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                } else {
                    if (isShowing) {
                        isShowing = false;
                        categoryViews.Divider.setVisibility(VISIBLE);
                    }
                }
            }
                AXSingleEmojiPageAdapter adapter = (AXSingleEmojiPageAdapter) recyclerView.getAdapter();
                int[] firstCPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstCPositions);
                int firstCVisibleItemPosition = findMin(firstCPositions);
                for (int i = 0; i < adapter.titlesPosition.size(); i++) {
                    int index = adapter.titlesPosition.get(i);
                    if (firstCVisibleItemPosition >= index) {
                        if (adapter.titlesPosition.size() > i + 1 && firstCVisibleItemPosition < adapter.titlesPosition.get(i + 1)) {
                            categoryViews.setPageIndex(i + 1);
                            break;
                        } else if (adapter.titlesPosition.size() <= i + 1) {
                            categoryViews.setPageIndex(adapter.titlesPosition.size());
                            break;
                        }
                    } else if (i - 1 >= 0 && firstCVisibleItemPosition < index && firstCVisibleItemPosition > adapter.titlesPosition.get(i - 1)) {
                        categoryViews.setPageIndex(i - 1);
                        break;
                    } else if (i - 1 == 0 && firstCVisibleItemPosition < index) {
                        categoryViews.setPageIndex(0);
                        break;
                    }
                }
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        private int findMin(int[] firstPositions) {
            int min = firstPositions[0];
            for (int value : firstPositions) {
                if (value < min) {
                    min = value;
                }
            }
            return min;
        }
    };
    AXFooterParallax scrollListener2;


    private void init(){
        if (AXEmojiManager.getInstance().getRecentEmoji()!=null){
            recent = AXEmojiManager.getInstance().getRecentEmoji();
        }else {
            recent = new RecentEmojiManager(getContext());
        }
        if (AXEmojiManager.getInstance().getVariantEmoji()!=null){
            variant = AXEmojiManager.getInstance().getVariantEmoji();
        }else {
            variant = new VariantEmojiManager(getContext());
        }

        variant = new VariantEmojiManager(getContext());

        recyclerView = new AXEmojiSingleRecyclerView(getContext());
        recyclerView.setItemAnimator(null);
        this.addView(recyclerView,new LayoutParams(0, 0,-1,-1));
        recyclerView.setAdapter(new AXSingleEmojiPageAdapter(AXEmojiManager.getInstance().getCategories(),events,recent,variant));
        recyclerView.addOnScrollListener(scrollListener);

        categoryViews = new AXCategoryViews(getContext(),this,recent);
        this.addView(categoryViews,new LayoutParams(0,0,-1,Utils.dpToPx(getContext(),39)));

        this.setBackgroundColor(AXEmojiManager.getInstance().getEmojiViewTheme().getBackgroundColor());

        scrollListener2 = new AXFooterParallax(categoryViews, -Utils.dpToPx(getContext(), 38),50);
        scrollListener2.setDuration(Utils.dpToPx(getContext(), 38));
        scrollListener2.setIDLEHideSize(scrollListener2.getDuration() / 2);
        scrollListener2.setMinComputeScrollOffset(Utils.dpToPx(getContext(), 38));
        scrollListener2.setScrollSpeed((long) 1);
        scrollListener2.setChangeOnIDLEState(true);
        recyclerView.addOnScrollListener(scrollListener2);

    }

    public void setPageIndex (int index){
        if (index ==0 && categoryViews.recent==false){
            recyclerView.scrollToPosition(0);
        }else {
            int index2 = index-1;
            if (categoryViews.recent) index2 = index;
            recyclerView.scrollToPosition(((AXSingleEmojiPageAdapter) recyclerView.getAdapter()).titlesPosition.get(index2));
        }
        if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) {
            if (index > 1) {
                categoryViews.Divider.setVisibility(VISIBLE);
            } else if (index == 0) {
                categoryViews.Divider.setVisibility(GONE);
            } else {
                scrollListener.onScrolled(recyclerView, 0, 1);
            }
        }
        categoryViews.setPageIndex(index);
    }

    @Override
    public void dismiss(){
        if (variantPopup!=null) variantPopup.dismiss();
        recent.persist();
        variant.persist();
    }

    @Override
    public void setEditText (EditText editText){
        super.setEditText(editText);
        variantPopup = new AXEmojiVariantPopup(editText.getRootView(),events);
    }

    @Override
    protected void setScrollListener(RecyclerView.OnScrollListener listener){
        super.setScrollListener(listener);
        recyclerView.addOnScrollListener(listener);
    }

    @Override
    protected void refresh(){
        super.refresh();
        ((AXSingleEmojiPageAdapter)recyclerView.getAdapter()).refresh();
        recyclerView.scrollToPosition(0);
        scrollListener2.show();
        categoryViews.removeAllViews();
        categoryViews.init();
        if (!AXEmojiManager.getEmojiViewTheme().shouldShowAlwaysDivider()) categoryViews.Divider.setVisibility(GONE);
        categoryViews.setPageIndex(0);
    }

    @Override
    public int getPageIndex(){
        return categoryViews.index;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
