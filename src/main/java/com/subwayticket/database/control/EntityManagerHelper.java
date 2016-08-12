package com.subwayticket.database.control;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public abstract class EntityManagerHelper {
    protected abstract EntityManager getEntityManager();

    public EntityManagerHelper() {
    }

    /**
     * 持久化一个实体类对象，相当于SQL中的INSERT语句
     * @param entity 实体类对象
     */
    public void create(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(entity);
        getEntityManager().getTransaction().commit();
        getEntityManager().refresh(entity);
    }

    /**
     * 将对一个实体的修改合并到数据库中，相当于SQL的UPDATE语句
     * @param entity 实体类对象
     * @throws javax.ejb.EJBException 若entity不处于持久化状态
     */
    public void merge(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(entity);
        getEntityManager().getTransaction().commit();
    }

    /**
     * 从数据库中查询实体类的最新数据
     * @param entity 实体类对象
     * @throws javax.ejb.EJBException 若entity不处于持久化状态
     */
    public void refresh(Object entity) {
        getEntityManager().refresh(entity);
    }

    /**
     * 从数据库中删除一个实体类对象对应的数据,相当于SQL中的DELETE语句
     * @param entity 实体类对象
     * @throws javax.ejb.EJBException 若entity不处于持久化状态
     */
    public void remove(Object entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(entity);
        getEntityManager().getTransaction().commit();
    }

    /**
     * 根据主键查询实体类对象
     * @param entityClass 实体类
     * @param id 实体类对应的主键
     * @return 实体类对象，若查询不到返回null
     */
    public Object find(Class<?> entityClass, Object id) {
        if(id == null)
            return null;
        return getEntityManager().find(entityClass, id);
    }

    /**
     * 查找一个实体类对应的表中的所有实体类对象
     * @param entityClass 实体类
     * @return 包含一个表中所有实体类对象的列表
     */
    public List findAll(Class<?> entityClass) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
