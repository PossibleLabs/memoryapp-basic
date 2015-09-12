package com.possiblelabs.beatboxingmemory;

/**
 * Created by possiblelabs on 9/12/15.
 */
public class Item {

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int view;
    private int sound;
    private int image1;
    private int image2;
    private int color;

    public Item(int view, int color, int sound, int image, int imageTwo) {
        this.view = view;
        this.color = color;
        this.sound = sound;
        this.image1 = image;
        this.image2 = imageTwo;
    }

    public int getSound() {
        return sound;
    }

    public int getImage1() {
        return image1;
    }

    public int getImage2() {
        return image2;
    }

}
