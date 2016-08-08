package com.subwayticket.model.result;

import com.subwayticket.database.model.TicketOrder;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
