package com.radcortez.personal.integration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@XmlRootElement(name = "x")
public class BehindTheName {
    @XmlElement(name = "name_detail")
    private List<NameDetail> nameDetails;

    @XmlElement(name = "error_code")
    private String errorCode;

    @XmlElement(name = "error")
    private String error;

    public List<NameDetail> getNameDetails() {
        return nameDetails;
    }

    public void setNameDetails(final List<NameDetail> nameDetails) {
        this.nameDetails = nameDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }
}
