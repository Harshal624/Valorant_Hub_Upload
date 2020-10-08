package tfws.mobileapps.valoranthubupload;

public class Video {
    private String title;
    private String video;
    private String image;
    private String credit;
    private String team;
    private String type;
    private String site;

    public Video() {

    }

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public String getImage() {
        return image;
    }

    public String getCredit() {
        return credit;
    }

    public String getTeam() {
        return team;
    }

    public String getType() {
        return type;
    }

    public String getSite() {
        return site;
    }

    public Video(String title, String video, String image, String credit, String team, String type, String site) {
        this.title = title;
        this.video = video;
        this.image = image;
        this.credit = credit;
        this.team = team;
        this.type = type;
        this.site = site;
    }
}
