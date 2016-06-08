package com.subwayticket.database.control;

import com.subwayticket.database.model.SystemMessage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/8/16.
 */
@Stateless(name = "SystemMessageDBHelperEJB")
public class SystemMessageDBHelperBean extends EntityManagerHelper {
    @PersistenceContext(unitName = "SubwayTicketDBPU")
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public SystemMessageDBHelperBean() {}

    public List<SystemMessage> getLatestMessage(int n){
        Query q = entityManager.createQuery("select sm from SystemMessage sm order by sm.releaseTime desc", SystemMessage.class);
        q.setMaxResults(n);
        return q.getResultList();
    }
}
