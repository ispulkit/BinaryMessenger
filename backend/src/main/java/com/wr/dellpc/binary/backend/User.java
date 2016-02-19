package com.wr.dellpc.binary.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class User {

    private String email;
    private String gcmid;
    private String dispname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcmid() {
        return gcmid;
    }

    public void setGcmid(String gcmid) {
        this.gcmid = gcmid;
    }

    public String getDispname() {
        return dispname;
    }

    public void setDispname(String dispname) {
        this.dispname = dispname;
    }
}