package com.subwayticket.control.jobs;

import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.TicketOrder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ejb.EJBException;

/**
 * Created by zhou-shengyun on 7/24/16.
 */
public class AutoCancelOrderJob implements Job {
    public static final String JOB_KEY_ORDER_ID = "OrderId";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            TicketOrderDBHelperBean orderDBHelperBean = TicketOrderDBHelperBean.getInstance();
            String orderId = context.getJobDetail().getJobDataMap().getString(JOB_KEY_ORDER_ID);
            System.out.println("Start job of automatically canceling order " + orderId + "...");
            TicketOrder to = (TicketOrder) orderDBHelperBean.find(TicketOrder.class, orderId);
            if(to == null || to.getStatus() != TicketOrder.ORDER_STATUS_NOT_PAY)
                return;
            TicketOrderControl.cancelOrder(orderDBHelperBean, to);
        }catch (EJBException e){
            e.printStackTrace();
            throw new JobExecutionException(e, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
