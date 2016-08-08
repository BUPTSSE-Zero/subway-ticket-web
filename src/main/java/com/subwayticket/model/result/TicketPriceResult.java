package com.subwayticket.model.result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
