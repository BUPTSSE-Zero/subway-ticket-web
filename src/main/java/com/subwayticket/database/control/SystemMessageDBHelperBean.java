package com.subwayticket.database.control;

import com.subwayticket.database.model.SystemMessage;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * 查询系统公告的EJB
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "SystemMessageDBHelperEJB")
public class SystemMessageDBHelperBean extends SystemDBHelperBean {
    public List<SystemMessage> getLatestMessage(int n){
        Query q = getEntityManager().createQuery("select sm from SystemMessage sm order by sm.releaseTime desc", SystemMessage.class);
        q.setMaxResults(n);
        return q.getResultList();
    }
}
