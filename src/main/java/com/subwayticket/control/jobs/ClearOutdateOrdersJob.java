package com.subwayticket.control.jobs;

import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.TicketOrder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by zhou-shengyun on 7/23/16.
 */

@DisallowConcurrentExecution
public class ClearOutdateOrdersJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            TicketOrderDBHelperBean orderDBHelperBean = TicketOrderDBHelperBean.getInstance();
            System.out.println("Start job of clearing outdate orders...");
            List<TicketOrder> orderList = orderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_PAY);
            for(TicketOrder to : orderList){
                if(System.currentTimeMillis() - to.getTicketOrderTime().getTime() > TicketOrderControl.MAX_NOTPAY_MINUTES * 60 * 1000){
                    TicketOrderControl.cancelOrder(orderDBHelperBean, to);
                }else{
                    break;
                }
            }
            Date currentTime = new Date();
            orderList = orderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET);
            for(TicketOrder to : orderList){
                if(to.getTicketOrderTime().getYear() < currentTime.getYear() ||
                        to.getTicketOrderTime().getMonth() < currentTime.getMonth() ||
                        to.getTicketOrderTime().getDate() < currentTime.getDate()){
                    TicketOrderControl.refundOrder(orderDBHelperBean, to);
                }else{
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new JobExecutionException(e, false);
        }
    }
}
