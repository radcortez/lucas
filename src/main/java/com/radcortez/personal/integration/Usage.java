package com.radcortez.personal.integration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Roberto Cortez
 */
@XmlType(name = "usage")
public class Usage {
    @XmlElement(name = "usage_code")
    private String code;
    @XmlElement(name = "usage_full")
    private String full;
    @XmlElement(name = "usage_gender")
    private String gender;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getFull() {
        return full;
    }

    public void setFull(final String full) {
        this.full = full;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }
}
