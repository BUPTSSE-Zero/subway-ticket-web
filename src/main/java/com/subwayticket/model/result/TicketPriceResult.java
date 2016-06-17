package com.subwayticket.model.result;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
public class TicketPriceResult extends Result {
    private float price;

    public TicketPriceResult(int resultCode, String resultDescription, float price) {
        super(resultCode, resultDescription);
        this.price = price;
    }

    public TicketPriceResult() {}

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
