package com.subwayticket.database.control;

import javax.ejb.Stateless;
import javax.persistence.*;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "SubwayTicketDBHelperEJB")
public class SystemDBHelperBean extends EntityManagerHelper {
    private EntityManager entityManager;

    private static EntityManager publicEntityManager = null;
    private static EntityManagerFactory subwayTicketDBPUEMF = Persistence.createEntityManagerFactory("SubwayTicketDBPU");

    public static EntityManager initSubwayTicketDBPU(){
        if(publicEntityManager == null) {
            publicEntityManager = subwayTicketDBPUEMF.createEntityManager();
            publicEntityManager.setFlushMode(FlushModeType.COMMIT);
        }
        return publicEntityManager;
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
