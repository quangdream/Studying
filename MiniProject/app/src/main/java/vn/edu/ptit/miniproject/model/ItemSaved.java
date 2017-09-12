package vn.edu.ptit.miniproject.model;

/**
 * Created by QuangPC on 7/24/2017.
 */

public class ItemSaved {
    private int id;
    private String title;
    private String desc;
    private String pubDate;
    private String image;
    private String pathFile;
    private int favorite;

    public ItemSaved() {
    }

    public String getTitle() {
        return title;
    }

    public ItemSaved(String title, String desc, String pubDate, String image, String pathFile) {
        this.title = title;
        this.desc = desc;
        this.pubDate = pubDate;
        this.image = image;
        this.pathFile = pathFile;
    }

    public ItemSaved(int id, String title, String desc, String pubDate, String image, String pathFile, int favorite) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.pubDate = pubDate;
        this.image = image;
        this.pathFile = pathFile;
        this.favorite = favorite;
    }


    public int getId() {
        return id;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }
}
