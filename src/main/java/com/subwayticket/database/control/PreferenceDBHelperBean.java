package com.subwayticket.database.control;

import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.PreferSubwayStation;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * 查询用户常用设置的EJB
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
@Stateless(name = "PreferenceDBHelperEJB")
public class PreferenceDBHelperBean extends SystemDBHelperBean {
    public List<HistoryRoute> getHistoryRoutes(Account user){
        Query q = getEntityManager().createQuery("select hr from HistoryRoute hr " +
                "where hr.userId = :userId");
        q.setParameter("userId", user.getPhoneNumber());
        return q.getResultList();
    }

    public List<PreferRoute> getPreferRoutes(Account user){
        Query q = getEntityManager().createQuery("select pr from PreferRoute pr " +
                "where pr.userId = :userId");
        q.setParameter("userId", user.getPhoneNumber());
        return q.getResultList();
    }

    public List<PreferSubwayStation> getPreferStations(Account user){
        Query q = getEntityManager().createQuery("select pss from PreferSubwayStation pss " +
                "where pss.userId = :userId");
        q.setParameter("userId", user.getPhoneNumber());
        return q.getResultList();
    }
}
