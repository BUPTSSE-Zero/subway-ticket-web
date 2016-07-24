package com.subwayticket.control.jobs;

import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.util.ScheduleUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ejb.EJBException;
import java.util.List;

/**
 * Created by zhou-shengyun on 7/24/16.
 */
@DisallowConcurrentExecution
public class AutoRefundCancelOrdersJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            System.out.println("Start job of automatically refunding or canceling order at 0:00...");
            TicketOrderDBHelperBean orderDBHelperBean = TicketOrderDBHelperBean.getInstance();
            List<TicketOrder> orderList = orderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_PAY);
            for(TicketOrder to : orderList){
                TicketOrderControl.cancelOrder(orderDBHelperBean, to);
                ScheduleUtil.cancelScheduleJob(TicketOrderControl.AUTO_CANCEL_ORDER_JOB_PREFIX + to.getTicketOrderId());
            }
            orderList = orderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET);
            for(TicketOrder to : orderList){
                TicketOrderControl.refundOrder(orderDBHelperBean, to);
            }
        }catch (EJBException e){
            e.printStackTrace();
            throw new JobExecutionException(e, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
