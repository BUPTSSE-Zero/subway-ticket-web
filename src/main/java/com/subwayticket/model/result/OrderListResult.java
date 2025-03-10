package com.subwayticket.model.result;

import com.subwayticket.database.model.TicketOrder;

import java.util.List;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class OrderListResult extends Result {
    private List<TicketOrder> ticketOrderList;

    public OrderListResult(int resultCode, String resultDescription, List<TicketOrder> ticketOrderList) {
        super(resultCode, resultDescription);
        this.ticketOrderList = ticketOrderList;
    }

    public List<TicketOrder> getTicketOrderList() {
        return ticketOrderList;
    }

    public void setTicketOrderList(List<TicketOrder> ticketOrderList) {
        this.ticketOrderList = ticketOrderList;
    }
}
