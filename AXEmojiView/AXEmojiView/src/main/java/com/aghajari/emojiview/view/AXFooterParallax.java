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

package com.aghajari.emojiview.view;

import android.animation.ObjectAnimator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;

class AXFooterParallax extends RecyclerView.OnScrollListener {

    RecyclerView RecyclerView;
    ObjectAnimator anim;
    private boolean menabled;
    private boolean monIdle;
    private long AnimTime;
    private int mComputeScrollOffset;
    private long Accept;
    private long scrollspeed;
    int minDy = -1;

    AXFooterParallax(View Toolbar, int ParalaxSize, int minDy) {
        this.minDy = minDy;
        this.menabled = true;
        this.monIdle = true;
        anim = ObjectAnimator.ofFloat(Toolbar, "translationY", 0, ParalaxSize);
        this.setDuration(Toolbar.getHeight());
        this.Accept = Toolbar.getHeight() / 2;
        this.scrollspeed = (long) 1.2;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView = recyclerView;
        if (menabled) {
            if (monIdle && newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                IDLE();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.RecyclerView = recyclerView;
        if (anim.getDuration() == anim.getCurrentPlayTime()) {
            if (minDy > Math.abs(dy)) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager == null) return;
                int firstVisibleItemPosition = 0;
                int visibleItemCount = layoutManager.getChildCount();
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                    staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                    firstVisibleItemPosition = findMin(firstPositions);
                }
                if ((visibleItemCount > 0 && (firstVisibleItemPosition) > 0)) {
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

    public boolean isEnabled() {
        return menabled;
    }

    public void setEnabled(boolean enabled) {
        this.menabled = enabled;
    }

    public void setChangeOnIDLEState(boolean enabled) {
        this.monIdle = enabled;
    }

    public long getDuration() {
        return anim.getDuration();
    }

    public void setDuration(long duration) {
        anim.setDuration(duration);
    }

    public void setMinComputeScrollOffset(int ComputeScrollOffset) {
        mComputeScrollOffset = ComputeScrollOffset;
    }

    public void setScrollSpeed(long s) {
        scrollspeed = s;
    }

    public void setIDLEHideSize(long s) {
        this.Accept = s;
    }

    public void onIDLE(boolean WithAnimation) {
        if (WithAnimation) {
            IDLE();
        } else {
            if (RecyclerView.computeVerticalScrollOffset() > mComputeScrollOffset) {
                if (AnimTime > 0 && AnimTime < anim.getDuration()) {
                    if (AnimTime < Accept) {
                        starts2(true);
                    } else {
                        starts2(false);
                    }
                }
            } else {
                if (AnimTime > 0 && AnimTime < anim.getDuration()) {
                    starts2(true);
                }
            }
        }
    }


    private void IDLE() {
        if (RecyclerView.computeVerticalScrollOffset() > mComputeScrollOffset) {
            if (AnimTime > 0 && AnimTime < anim.getDuration()) {
                if (AnimTime < Accept) {
                    starts(true);
                } else {
                    starts(false);
                }
            }
        } else {
            if (AnimTime > 0 && AnimTime < anim.getDuration()) {
                starts(true);
            }
        }
    }

    void starts(boolean e) {
        if (e) {
            anim.reverse();
            AnimTime = 0;
        } else {
            anim.start();
            AnimTime = anim.getDuration();
        }
    }

    private void starts2(boolean e) {
        if (e) {
            AnimTime = 0;
            anim.setCurrentPlayTime(AnimTime);
        } else {
            AnimTime = anim.getDuration();
            anim.setCurrentPlayTime(AnimTime);
        }
    }

    void show() {
        if (anim.getCurrentPlayTime() == anim.getDuration()) starts(true);
    }

    private void SCROLL(int dy) {
        if (AnimTime <= anim.getDuration() && AnimTime >= 0) {
            if (dy > 0) {
                AnimTime = AnimTime + Math.abs(dy / scrollspeed);
            } else {
                AnimTime = AnimTime - Math.abs(dy / scrollspeed);
            }
            if (AnimTime > anim.getDuration())
                AnimTime = anim.getDuration();
            if (AnimTime < 0)
                AnimTime = 0;
            anim.setCurrentPlayTime(AnimTime);
        } else {
            if (AnimTime > anim.getDuration())
                AnimTime = anim.getDuration();
            if (AnimTime < 0)
                AnimTime = 0;
        }
    }
}
