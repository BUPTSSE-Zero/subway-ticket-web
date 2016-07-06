package com.subwayticket.model.result;

import com.subwayticket.database.model.TicketOrder;

/**
 * Created by zhou-shengyun on 7/6/16.
 */
public class SubmitOrderResult extends Result {
    private TicketOrder ticketOrder;

    public SubmitOrderResult(int resultCode, String resultDescription, TicketOrder ticketOrder) {
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
