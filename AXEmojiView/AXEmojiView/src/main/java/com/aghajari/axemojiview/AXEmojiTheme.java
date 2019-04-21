package com.aghajari.axemojiview;

import android.graphics.Color;
import android.graphics.Typeface;


public class AXEmojiTheme {
    int DefaultColor = 0xff9aa7af;
    int BackgroundColor = 0xffebeff2;
    int SelectedColor = 0xff4a5358;
    int SelectionColor = 0xff0d917c;
    int DividerColor = Color.LTGRAY;
    boolean variantDividerEnabled = true;
    int variantDividerColor = Color.LTGRAY;
    int variantPopupBackgroundColor = Color.WHITE;
    int footerBackgroundColor = 0xffe3e7e8;
    int footerItemColor = 0xff636768;
    int footerSelectedItemColor = 0xff0d917c;
    boolean footerEnabled = true;
    int CategoryColor = 0xffebeff2;

    int titleColor = Color.DKGRAY;
    Typeface titleTypeface = Typeface.DEFAULT_BOLD;

    public int getBackgroundColor() {
        return BackgroundColor;
    }

    public int getDefaultColor() {
        return DefaultColor;
    }

    public int getSelectedColor() {
        return SelectedColor;
    }

    public int getSelectionColor() {
        return SelectionColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        BackgroundColor = backgroundColor;
    }

    public void setDefaultColor(int defaultColor) {
        DefaultColor = defaultColor;
    }

    public void setSelectedColor(int selectedColor) {
        SelectedColor = selectedColor;
    }

    public void setSelectionColor(int selectionColor) {
        SelectionColor = selectionColor;
    }

    public int getDividerColor() {
        return DividerColor;
    }

    public void setDividerColor(int shadowSize) {
        DividerColor = shadowSize;
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
        CategoryColor = categoryColor;
    }

    public int getCategoryColor() {
        return CategoryColor;
    }


    boolean alwaysShowDivider=false;
    public void setAlwaysShowDivider(boolean value){
        alwaysShowDivider = value;
    }
    public boolean shouldShowAlwaysDivider(){
        return alwaysShowDivider;
    }
}
