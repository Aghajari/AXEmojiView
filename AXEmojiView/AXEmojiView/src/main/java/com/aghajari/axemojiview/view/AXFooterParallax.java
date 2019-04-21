package com.aghajari.axemojiview.view;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
class AXFooterParallax extends RecyclerView.OnScrollListener {
	
	RecyclerView RecyclerView;
	ObjectAnimator anim;
	private boolean menabled;
	private boolean monIdle;
	public OnScrollListener OnScrollListener;
	private long AnimTime;
	private int mComputeScrollOffset;
	private long Accept;
	private long scrollspeed;
	int minDy = -1;

	boolean isShowing = true;
	AXFooterParallax(View Toolbar,int ParalaxSize,int minDy) {
		this.minDy = minDy;
		this.menabled=true;
		this.monIdle=true;
		anim=ObjectAnimator.ofFloat(Toolbar,"translationY", 0,ParalaxSize);
		this.setDuration(Toolbar.getHeight());
		this.Accept=Toolbar.getHeight()/2;
		this.scrollspeed=(long) 1.2;
	}

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		super.onScrollStateChanged(recyclerView, newState);
		RecyclerView = recyclerView;
		if (menabled) {
			if (monIdle && newState==android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE) {
				IDLE();
			}
		}
	}
	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		this.RecyclerView =recyclerView;
		if (anim.getDuration()==anim.getCurrentPlayTime()){
			if (minDy>Math.abs(dy)){
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				int firstVisibleItemPosition=0;
				int visibleItemCount = layoutManager.getChildCount();
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
					firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
				}else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
					StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
					int[]firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];

					staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
					 firstVisibleItemPosition = findMin(firstPositions);
				}
				if ((visibleItemCount > 0 && (firstVisibleItemPosition)>0)) {
					return;
				}
			}
		}
		if (menabled) {
			SCROLL(dy);
		}
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

	public boolean getEnabled() {
		return menabled;
	}
	public void setEnabled(boolean enabled) {
		this.menabled=enabled;
	}
	public boolean getChangeOnIDLEState() {
		return monIdle;
	}
	public void setChangeOnIDLEState(boolean enabled) {
		this.monIdle=enabled;
	}
	
	public long getCurrentPlayTime() {
		return anim.getCurrentPlayTime();
	}
	public void setCurrentPlayTime(long time) {
		AnimTime=time;
		anim.setCurrentPlayTime(time);
	}
	
	public long getDuration() {
		return anim.getDuration();
	}
	public void setDuration(long duration) {
		anim.setDuration(duration);
	}
	
	public int getMinComputeScrollOffset() {
		return mComputeScrollOffset;
	}
	public void setMinComputeScrollOffset(int ComputeScrollOffset) {
		mComputeScrollOffset=ComputeScrollOffset;
	}
	
	public long getScrollSpeed() {
		return this.scrollspeed;
	}
	public void setScrollSpeed(long s) {
		scrollspeed=s;
	}
	
	
	public long getIDLEHideSize() {
		return this.Accept;
	}
	
	
	public void setIDLEHideSize(long s) {
		this.Accept=s;
	}
	
	/**
	 * Get Object Animator
	 */
	public ObjectAnimator getObjectAnimator() {
		return anim;
	}

	/**
	 * Set Toolbar Parallax with scroll offset
	 */
	public void onScroll (int Dy) {
		SCROLL(Dy);
	}

	/**
	 * Start
	 */
	public void onIDLE(boolean WithAnimation) {
		if (WithAnimation) {
		IDLE();
		}else {
			if (RecyclerView.computeVerticalScrollOffset()>mComputeScrollOffset) {
				if (AnimTime>0 && AnimTime<anim.getDuration()) {
					if (AnimTime<Accept) {
						starts2(true);
					}else {
						starts2(false);
					}
				}
			}else {
				if (AnimTime>0 && AnimTime<anim.getDuration()) {
				starts2(true);
				}
			}
		}
	}


	private void IDLE() {
		if (RecyclerView.computeVerticalScrollOffset()>mComputeScrollOffset) {
			if (AnimTime>0 && AnimTime<anim.getDuration()) {
				if (AnimTime<Accept) {
					starts(true);
				}else {
					starts(false);

				}
			}
		}else {
			if (AnimTime>0 && AnimTime<anim.getDuration()) {
			starts(true);
			}
		}
	}

	void starts(boolean e) {
		if (e) {anim.reverse();AnimTime=0;}else {anim.start();AnimTime=anim.getDuration();}
	}
	private void starts2(boolean e) {
		if (e) {AnimTime=0;anim.setCurrentPlayTime(AnimTime);}else {AnimTime=anim.getDuration();anim.setCurrentPlayTime(AnimTime);}
	}
	void show() {
		if (anim.getCurrentPlayTime() == anim.getDuration()) starts(true);
	}

	private void SCROLL(int dy) {
		 if (AnimTime <= anim.getDuration() && AnimTime >= 0) {
         	if (dy>0) {
         		AnimTime=AnimTime+Math.abs(dy/scrollspeed);
         	}else {
         		AnimTime=AnimTime-Math.abs(dy/scrollspeed);
         	}
         	if (AnimTime>anim.getDuration()) {AnimTime=anim.getDuration();}
         	if (AnimTime<0) {AnimTime=0;}
         	anim.setCurrentPlayTime(AnimTime);
         }
         else {
         	if (AnimTime>anim.getDuration()) {AnimTime=anim.getDuration();}
         	if (AnimTime<0) {AnimTime=0;}
         }
	}
}
