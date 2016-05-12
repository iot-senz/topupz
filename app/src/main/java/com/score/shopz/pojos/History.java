package com.score.shopz.pojos;

/**
 * Created by eranga on 5/8/16.
 */
public class History {
    private String name;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public History(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }
}
