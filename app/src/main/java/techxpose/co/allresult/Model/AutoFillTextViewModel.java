package techxpose.co.allresult.Model;

public class AutoFillTextViewModel {

    public AutoFillTextViewModel(String branchName) {
        this.branchName = branchName;
    }

    private String branchName;
    private String resultDate;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }
}
