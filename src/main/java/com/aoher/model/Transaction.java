package com.aoher.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction {

    @XmlAttribute(name = "amount")
    private double amount;

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "parent_id")
    private long parentId;

    public Transaction() {
    }

    public Transaction(double amount, String type) {
        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException("type cannot be empty or null");
        }
        this.amount = amount;
        this.type = type;
    }

    public Transaction(double amount, String type, long parentId) {
        this(amount, type);
        if (parentId <= 0) {
            throw new IllegalArgumentException("parent id cannot be equal or less than zero");
        }
        this.parentId = parentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public long getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
