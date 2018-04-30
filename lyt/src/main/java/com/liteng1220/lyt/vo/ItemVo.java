package com.liteng1220.lyt.vo;

public class ItemVo<T> {
    public int id;
    public int iconResId;
    public String name;
    public T data;

    public ItemVo(int id, int iconResId, String name, T data) {
        this.id = id;
        this.iconResId = iconResId;
        this.name = name;
        this.data = data;
    }

    public ItemVo() {
    }
}
