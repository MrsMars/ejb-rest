package com.aoher.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseState {

    private String status;

    public ResponseState() {
        super();
    }

    public ResponseState(String status) {
        if (status == null || status.trim().length() == 0) {
            throw new IllegalArgumentException("status cannot be empty or null");
        }
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
