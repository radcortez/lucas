package com.radcortez.personal.integration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@XmlType(name = "name_detail")
public class NameDetail {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "gender")
    private String gender;
    @XmlElement(name = "number")
    private String number;
    @XmlElementWrapper(name = "usages")
    private List<Usage> usages;

    public NameDetail() {
    }

    public NameDetail(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(final List<Usage> usages) {
        this.usages = usages;
    }
}
