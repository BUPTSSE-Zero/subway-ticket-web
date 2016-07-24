package com.subwayticket.database.control;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "SubwayTicketDBHelperEJB")
public class SystemDBHelperBean extends EntityManagerHelper {
    private EntityManager entityManager;

    public static SystemDBHelperBean getInstance(){
        try {
            InitialContext initialContext = new InitialContext();
            return (SystemDBHelperBean) initialContext.lookup("java:module/SubwayTicketDBHelperEJB");
        }catch (NamingException ne){
            ne.printStackTrace();
            return null;
        }
    }

    public SystemDBHelperBean() {
        entityManager = EntityManagerFactory.getSubwayTicketDBEntityManager();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
