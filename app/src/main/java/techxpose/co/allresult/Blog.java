package techxpose.co.allresult;

/**
 * Created by laddi on 23-February-2018.
 */

public class Blog {
public   Blog(){}



    private String branchname;

    public Blog(String branchname, String resultlink, String year, String date, String resultDate, String examination) {
        this.branchname = branchname;
        this.resultlink = resultlink;
        this.year = year;
        this.date = date;
        this.resultdate = resultDate;
        this.examination = examination;
    }

    private String resultlink;
    private String year;
    private String date;

    public String getResultDate() {
        return resultdate;
    }

    public void setResultDate(String resultDate) {
        this.resultdate = resultDate;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    private String resultdate;
    private String examination;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }







    public String getBranchname() {
        return branchname+".";
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public void setResultlink(String resultlink) {
        this.resultlink = resultlink;
    }

    public String getResultlink() {
        return resultlink;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {       this.year = year;    }


}
