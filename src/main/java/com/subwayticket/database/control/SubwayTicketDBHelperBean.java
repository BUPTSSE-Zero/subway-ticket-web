package com.subwayticket.database.control;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "SubwayTicketDBHelperEJB")
public class SubwayTicketDBHelperBean extends EntityManagerHelper {
    @PersistenceContext(unitName = "SubwayTicketDBPU")
    EntityManager entityManager;

    public SubwayTicketDBHelperBean() {
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
