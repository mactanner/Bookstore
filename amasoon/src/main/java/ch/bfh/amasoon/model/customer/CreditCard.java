package ch.bfh.amasoon.model.customer;

import java.io.Serializable;
import java.util.Date;

public class CreditCard implements Serializable {

    public enum Type {

        MasterCard, Visa
    }
    private Type type;
    private String number;
    private Date expirationDate;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
