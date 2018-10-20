package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResLogin {

    @SerializedName("SERVER_RESPONSE")
    @Expose
    private Integer sERVERRESPONSE;
    @SerializedName("SERVER_MESSAGE")
    @Expose
    private String sERVERMESSAGE;

    @SerializedName("ID")
    @Expose
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getSERVERRESPONSE() {
        return sERVERRESPONSE;
    }

    public void setSERVERRESPONSE(Integer sERVERRESPONSE) {
        this.sERVERRESPONSE = sERVERRESPONSE;
    }

    public String getSERVERMESSAGE() {
        return sERVERMESSAGE;
    }

    public void setSERVERMESSAGE(String sERVERMESSAGE) {
        this.sERVERMESSAGE = sERVERMESSAGE;
    }

}
