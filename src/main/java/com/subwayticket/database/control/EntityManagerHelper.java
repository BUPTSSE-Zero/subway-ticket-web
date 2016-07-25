package com.subwayticket.database.control;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public abstract class EntityManagerHelper {
    protected abstract EntityManager getEntityManager();

    public EntityManagerHelper() {
    }
    
    public void create(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(entity);
        getEntityManager().getTransaction().commit();
    }

    public void merge(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(entity);
        getEntityManager().getTransaction().commit();
    }

    public void refresh(Object entity) {
        getEntityManager().refresh(entity);
    }
    
    public void remove(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().getTransaction().commit();
    }

    public boolean containsEntity(Object entity){
        return getEntityManager().contains(entity);
    }

    public void detach(Object entity) {
        getEntityManager().detach(entity);
    }

    public Object find(Class<?> entityClass, Object id) {
        if(id == null)
            return null;
        return getEntityManager().find(entityClass, id);
    }

    public List findAll(Class<?> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List findRange(Class<?> entityClass, int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count(Class<?> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Object> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
