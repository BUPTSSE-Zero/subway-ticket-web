package com.subwayticket.model.result;

import com.subwayticket.database.model.TicketOrder;

/**
 * Created by zhou-shengyun on 7/7/16.
 */
public class OrderInfoResult extends Result {
    private TicketOrder ticketOrder;

    public OrderInfoResult(int resultCode, String resultDescription, TicketOrder ticketOrder) {
        super(resultCode, resultDescription);
        this.ticketOrder = ticketOrder;
    }

    public TicketOrder getTicketOrder() {
        return ticketOrder;
    }

    public void setTicketOrder(TicketOrder ticketOrder) {
        this.ticketOrder = ticketOrder;
    }
}
