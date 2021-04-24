package com.fio.fiordor.notifyreminder.pojo;

public class Wiki {

    private String href;
    private String title;
    private String text;

    public Wiki(String href, String title, String text) {
        this.href = href;
        this.title = title;
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
