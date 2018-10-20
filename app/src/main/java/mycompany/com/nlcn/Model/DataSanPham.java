
package mycompany.com.nlcn.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSanPham {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("numpages")
    @Expose
    private Integer numpages;
    @SerializedName("sanphams")
    @Expose
    private List<Sanpham> sanphams = null;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getNumpages() {
        return numpages;
    }

    public void setNumpages(Integer numpages) {
        this.numpages = numpages;
    }

    public List<Sanpham> getSanphams() {
        return sanphams;
    }

    public void setSanphams(List<Sanpham> sanphams) {
        this.sanphams = sanphams;
    }

}
