package com.subwayticket.database.control;

import javax.ejb.Stateless;
import javax.persistence.*;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "SubwayTicketDBHelperEJB")
public class SystemDBHelperBean extends EntityManagerHelper {
    private EntityManager entityManager;
    private static EntityManagerFactory subwayTicketDBPUEMF = Persistence.createEntityManagerFactory("SubwayTicketDBPU");

    public static EntityManager initSubwayTicketDBPU(){
        EntityManager entityManager = subwayTicketDBPUEMF.createEntityManager();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        return entityManager;
    }

    public static void closeSubwayTicketDBPU(){
        subwayTicketDBPUEMF.close();
    }

    public SystemDBHelperBean() {
        entityManager = initSubwayTicketDBPU();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
