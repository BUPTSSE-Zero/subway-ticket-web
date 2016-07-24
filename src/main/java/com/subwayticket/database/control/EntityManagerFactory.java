package com.subwayticket.database.control;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

/**
 * Created by zhou-shengyun on 7/24/16.
 */
public class EntityManagerFactory {
    private static javax.persistence.EntityManagerFactory subwayTicketDBPUEMF = Persistence.createEntityManagerFactory("SubwayTicketDBPU");
    private static EntityManager subwayTicketDBEntityManager = null;

    public static EntityManager getSubwayTicketDBEntityManager(){
        if(subwayTicketDBEntityManager == null) {
            subwayTicketDBEntityManager = subwayTicketDBPUEMF.createEntityManager();
            subwayTicketDBEntityManager.setFlushMode(FlushModeType.COMMIT);
        }
        return subwayTicketDBEntityManager;
    }

    public static void closeSubwayTicketDBPU(){
        subwayTicketDBPUEMF.close();
    }
}
