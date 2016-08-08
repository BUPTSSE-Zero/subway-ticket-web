package com.subwayticket.database.control;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class EntityManagerFactory {
    private static javax.persistence.EntityManagerFactory subwayTicketDBPUEMF = Persistence.createEntityManagerFactory("SubwayTicketDBPU");
    private static EntityManager subwayTicketDBEntityManager = null;

    /**
     * 获取SubwayTicketDBPU对应的实体管理器
     * @return 实体管理器实例
     */
    public static EntityManager getSubwayTicketDBEntityManager(){
        if(subwayTicketDBEntityManager == null) {
            subwayTicketDBEntityManager = subwayTicketDBPUEMF.createEntityManager();
            subwayTicketDBEntityManager.setFlushMode(FlushModeType.COMMIT);
        }
        return subwayTicketDBEntityManager;
    }

    /**
     * 关闭SubwayTicketDBPU下的所有实体管理器
     */
    public static void closeSubwayTicketDBPU(){
        subwayTicketDBPUEMF.close();
    }
}
