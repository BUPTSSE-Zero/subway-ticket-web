package com.subwayticket.database.control;

import com.subwayticket.database.model.TicketOrder;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 * Created by zhou-shengyun on 7/7/16.
 */

@Stateless
public class TicketOrderDBHelperBean extends EntityManagerHelper {
    private EntityManager entityManager;

    public TicketOrderDBHelperBean(){
        entityManager = SystemDBHelperBean.initSubwayTicketDBPU();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public TicketOrder getOrderByExtractCode(String extractCode){
        if(extractCode == null || extractCode.isEmpty())
            return null;
        Query q = entityManager.createQuery("select t from TicketOrder t where t.ticketKey = :extractCode", TicketOrder.class);
        q.setParameter("extractCode", extractCode);
        try {
            return (TicketOrder) q.getSingleResult();
        }catch (NoResultException nre){
            return null;
        }catch (NonUniqueResultException nure){
            return null;
        }
    }
}
