package techxpose.co.allresult.Model;

public class DateSheetModel {

    public   DateSheetModel(){}



    private String name;
    private String link;
    private String description;
    private String date;
    private String course;
    private String examination;

    public DateSheetModel(String name, String link, String description, String date, String course, String examination) {
        this.name = name;
        this.link = link;
        this.description = description;
        this.date = date;
        this.course = course;
        this.examination = examination;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name+".";
    }

    public void setBranchname(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {       this.description = description;    }


}
