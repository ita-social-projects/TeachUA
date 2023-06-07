package com.softserve.teachua.data.home;

public class BannerItem<T> {
    private String title;
    private String description;
    private String urlButton;
    private String urlPicture;
    private String urlLocalPicture;
    private int number;
    private Class<T> clazz;

    public BannerItem(String title, String description, String urlButton, String urlPicture, Class<T> clazz) {
        this.title = title;
        this.description = description;
        this.urlButton = urlButton;
        this.urlPicture = urlPicture;
        this.clazz = clazz;
        urlLocalPicture = "";
        number = -1;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlButton() {
        return urlButton;
    }

    public String getUrlLocalPicture() {
        return urlLocalPicture;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public int getNumber() {
        return number;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public BannerItem setUrlLocalPicture(String urlLocalPicture) {
        this.urlLocalPicture = urlLocalPicture;
        return this;
    }

    public BannerItem setNumber(int number) {
        this.number = number;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //
        BannerItem<?> that = (BannerItem<?>) o;
        //
        if (number != that.number) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (urlButton != null ? !urlButton.equals(that.urlButton) : that.urlButton != null) return false;
        if (urlPicture != null ? !urlPicture.equals(that.urlPicture) : that.urlPicture != null) return false;
        if (urlLocalPicture != null ? !urlLocalPicture.equals(that.urlLocalPicture) : that.urlLocalPicture != null)
            return false;
        return clazz != null ? clazz.equals(that.clazz) : that.clazz == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (urlButton != null ? urlButton.hashCode() : 0);
        result = 31 * result + (urlPicture != null ? urlPicture.hashCode() : 0);
        result = 31 * result + (urlLocalPicture != null ? urlLocalPicture.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BannerItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlButton='" + urlButton + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", urlLocalPicture='" + urlLocalPicture + '\'' +
                ", number=" + number +
                ", clazz=" + clazz.getName() +
                '}';
    }
}
