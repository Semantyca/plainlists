package com.semantyca.plainlists.api;


import com.toonext.adapter.SimpleReferenceEntity;

public class Label extends SimpleReferenceEntity implements ILabel {

    private String color;

    private Label parent;

    private boolean hidden;

    private String category;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Label getParent() {
        return parent;
    }

    public void setParent(Label parent) {
        this.parent = parent;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
