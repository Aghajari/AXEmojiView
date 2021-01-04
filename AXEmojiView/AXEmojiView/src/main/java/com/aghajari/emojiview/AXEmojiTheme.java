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

package com.aghajari.emojiview;

import android.graphics.Color;
import android.graphics.Typeface;

public class AXEmojiTheme {
    int defaultColor = 0xff9aa7af;
    int backgroundColor = 0xffebeff2;
    int selectedColor = 0xff4a5358;
    int selectionColor = 0xff0d917c;
    int dividerColor = Color.LTGRAY;
    boolean variantDividerEnabled = true;
    int variantDividerColor = Color.LTGRAY;
    int variantPopupBackgroundColor = Color.WHITE;
    int footerBackgroundColor = 0xffe3e7e8;
    int footerItemColor = 0xff636768;
    int footerSelectedItemColor = 0xff0d917c;
    boolean footerEnabled = true;
    int categoryColor = 0xffebeff2;
    int titleColor = Color.DKGRAY;
    Typeface titleTypeface = Typeface.DEFAULT_BOLD;
    boolean alwaysShowDivider = false;
    boolean categoryEnabled = true;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public int getSelectionColor() {
        return selectionColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSelectionColor(int selectionColor) {
        this.selectionColor = selectionColor;
    }

    public int getDividerColor() {
        return this.dividerColor;
    }

    public void setDividerColor(int color) {
        this.dividerColor = color;
    }

    public boolean isVariantDividerEnabled() {
        return variantDividerEnabled;
    }

    public void setVariantDividerEnabled(boolean variantDividerEnabled) {
        this.variantDividerEnabled = variantDividerEnabled;
    }

    public int getVariantDividerColor() {
        return variantDividerColor;
    }

    public void setVariantDividerColor(int variantDividerColor) {
        this.variantDividerColor = variantDividerColor;
    }

    public int getVariantPopupBackgroundColor() {
        return variantPopupBackgroundColor;
    }

    public void setVariantPopupBackgroundColor(int variantPopupBackgroundColor) {
        this.variantPopupBackgroundColor = variantPopupBackgroundColor;
    }

    public int getFooterBackgroundColor() {
        return footerBackgroundColor;
    }

    public int getFooterItemColor() {
        return footerItemColor;
    }

    public int getFooterSelectedItemColor() {
        return footerSelectedItemColor;
    }

    public void setFooterBackgroundColor(int footerBackgroundColor) {
        this.footerBackgroundColor = footerBackgroundColor;
    }

    public void setFooterItemColor(int footerItemColor) {
        this.footerItemColor = footerItemColor;
    }

    public void setFooterSelectedItemColor(int footerSelectedItemColor) {
        this.footerSelectedItemColor = footerSelectedItemColor;
    }

    public boolean isFooterEnabled() {
        return footerEnabled;
    }

    public void setFooterEnabled(boolean footerEnabled) {
        this.footerEnabled = footerEnabled;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public Typeface getTitleTypeface() {
        return titleTypeface;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setTitleTypeface(Typeface titleTypeface) {
        this.titleTypeface = titleTypeface;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public int getCategoryColor() {
        return this.categoryColor;
    }

    public void setAlwaysShowDivider(boolean value) {
        alwaysShowDivider = value;
    }

    public boolean isAlwaysShowDividerEnabled() {
        return alwaysShowDivider;
    }

    public boolean isCategoryEnabled() {
        return categoryEnabled;
    }

    public void setCategoryEnabled(boolean categoryEnabled) {
        this.categoryEnabled = categoryEnabled;
    }
}
