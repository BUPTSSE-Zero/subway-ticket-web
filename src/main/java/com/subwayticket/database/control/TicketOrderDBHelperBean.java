package com.subwayticket.database.control;

import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.TicketOrder;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.Date;
import java.util.List;

/**
 * Created by zhou-shengyun on 7/7/16.
 */

@Stateless(name = "TicketOrderDBHelperEJB")
public class TicketOrderDBHelperBean extends SystemDBHelperBean {
    public static TicketOrderDBHelperBean getInstance(){
        try {
            InitialContext initialContext = new InitialContext();
            return (TicketOrderDBHelperBean) initialContext.lookup("java:module/TicketOrderDBHelperEJB");
        }catch (NamingException ne){
            ne.printStackTrace();
            return null;
        }
    }

    public TicketOrder getOrderByOrderID(String ticketOrderID, Account user){
        if(ticketOrderID == null || user == null)
            return null;
        Query q = getEntityManager().createQuery("select t from TicketOrder t where t.user=:user and t.ticketOrderId=:ticketOrderID");
        q.setParameter("user", user);
        q.setParameter("ticketOrderID", ticketOrderID);
        try{
            return (TicketOrder) q.getSingleResult();
        }catch (NoResultException | NonUniqueResultException e){
            return null;
        }
    }

    private void setDateRange(Date startDate, Date endDate){
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        if(startDate.after(endDate)){
            int tempYear = endDate.getYear();
            int tempMonth = endDate.getMonth();
            int tempDate = endDate.getDate();
            endDate.setYear(startDate.getYear());
            endDate.setMonth(startDate.getMonth());
            endDate.setDate(startDate.getDate());
            startDate.setYear(tempYear);
            startDate.setMonth(tempMonth);
            startDate.setDate(tempDate);
        }
    }

    public List<TicketOrder> getAllOrderByDate(Account user, Date startDate, Date endDate){
        if(user == null)
            return null;
        setDateRange(startDate, endDate);
        Query q = getEntityManager().createQuery("select t from TicketOrder t where t.user=:user and t.ticketOrderTime between :startDate and :endDate " +
                                            "order by t.ticketOrderTime desc");
        q.setParameter("user", user);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    public List<TicketOrder> getAllOrderByStatus(char orderStatus){
        Query q = getEntityManager().createQuery("select t from TicketOrder t where t.status=:status order by t.ticketOrderTime asc");
        q.setParameter("status", orderStatus);
        return q.getResultList();
    }

    public List<TicketOrder> getAllOrderByStatus(char orderStatus, Account user) {
        return getAllOrderByStatusAndDate(orderStatus, user, null, null);
    }

    public List<TicketOrder> getAllOrderByStatusAndDate(char orderStatus, Account user, Date startDate, Date endDate){
        if (user == null)
            return null;
        Query q;
        if(startDate == null && endDate == null){
            q = getEntityManager().createQuery("select t from TicketOrder t where t.user=:user and t.status=:status order by t.ticketOrderTime desc");
        }else if(startDate != null && endDate != null){
            setDateRange(startDate, endDate);
            q = getEntityManager().createQuery("select t from TicketOrder t where t.user=:user and t.status=:status and t.ticketOrderTime between :startDate and :endDate " +
                                          "order by t.ticketOrderTime desc");
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
        }else{
            return null;
        }
        q.setParameter("user", user);
        q.setParameter("status", orderStatus);
        return q.getResultList();
    }

    public TicketOrder getOrderByExtractCode(String extractCode){
        if(extractCode == null || extractCode.isEmpty())
            return null;
        Query q = getEntityManager().createQuery("select t from TicketOrder t where t.extractCode = :extractCode", TicketOrder.class);
        q.setParameter("extractCode", extractCode);
        try {
            return (TicketOrder) q.getSingleResult();
        }catch (NoResultException | NonUniqueResultException e){
            return null;
        }
    }
}
