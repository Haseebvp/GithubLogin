package network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haseeb on 9/1/17.
 */
public class SingleRepoData {
    @SerializedName("files")
    @Expose
    private List<FIleData> files;

    @SerializedName("commit")
    @Expose
    private CommitData commit;



    public List<FIleData> getFiles() {
        return files;
    }

    public void setFiles(List<FIleData> files) {
        this.files = files;
    }

    public CommitData getCommit() {
        return commit;
    }

    public void setCommit(CommitData commit) {
        this.commit = commit;
    }
}
