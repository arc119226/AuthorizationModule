package com.arcare.oauth.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arcare.oauth.util.GenericsUtil;
import com.arcare.oauth.util.Page;

@Repository
public class BasicDao<T, ID extends Serializable>{

    private Class<T> domainClass;


    @Autowired
    private SessionFactory sessionFactory;


    @SuppressWarnings("unchecked")
    public BasicDao() {
        domainClass = GenericsUtil.getSuperClassGenricType(getClass());
    }
    

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public boolean exists(ID id) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.idEq(id));
        criteria.setProjection(Projections.rowCount());

        return 0 < ((Integer) criteria.uniqueResult()).intValue();
    }


    public T findById(ID id) {
        return (T) this.sessionFactory.getCurrentSession().get(domainClass, id);
    }

    public void save(T entity) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void saveAll(List<T> entities) {
        Session session = this.sessionFactory.getCurrentSession();

        for (Iterator<T> it = entities.iterator(); it.hasNext();) {
            session.saveOrUpdate(it.next());
        }
    }

    public void delete(T entity) {
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteById(ID id) {
        this.sessionFactory.getCurrentSession().delete(this.findById(id));
    }

    public void deleteAll(List<T> entities) {
        Session session = this.sessionFactory.getCurrentSession();

        for (Iterator<T> it = entities.iterator(); it.hasNext();) {
            session.delete(it.next());
        }
    }

    protected void setOrderToCriteria(Criteria criteria, String[] ascProperties, String[] descProperties) {
        if (null != ascProperties && 0 < ascProperties.length) {
            for (int i = 0; i < ascProperties.length; i++) {
                criteria.addOrder(Order.asc(ascProperties[i]));
            }
        }

        if (null != descProperties && 0 < descProperties.length) {
            for (int i = 0; i < descProperties.length; i++) {
                criteria.addOrder(Order.desc(descProperties[i]));
            }
        }
    }
    
    public List<T> listAll() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(domainClass);
        return criteria.list();
    }

    public List<T> findByPropertyLike(String property, String value) {
        return findByPropertyLike(property, value, Page.DEFAULT_PAGE_SIZE);
    }

    public List<T> findByProperty(String property,String value){
        List<T> result = null;
        try {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq(property, value));
            criteria.setFirstResult(0);
            result = (List<T>) criteria.list();
        } catch (Exception cause) {
            result = (List<T>) new ArrayList<T>();
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findByPropertyLike(String property, String value, int pageSize) {
        List<T> result = null;

        try {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.like(property, value, MatchMode.START));
            criteria.setFirstResult(0);
            criteria.setMaxResults(pageSize);
            result = (List<T>) criteria.list();

        } catch (Exception cause) {
            result = (List<T>) new ArrayList<T>();
        }

        return result;
    }

    public Page<T> pageQuery(Criteria criteria, int pageNo, int pageSize) {
        return pageQuery(criteria, pageNo, pageSize, null);
    }

    public Page<T> pageQuery(Criteria criteria, int pageNo, int pageSize, List<Order> orders) {
        CriteriaImpl impl = (CriteriaImpl) criteria;

        Projection projection = impl.getProjection();

        Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (totalCount < 1) {
            return new Page<T>();
        }

        Integer currentIndex = Page.getStartOfPage(pageNo, pageSize, totalCount.intValue());
        if(null != orders){
            for(Order order : orders){
                criteria.addOrder(order);
            }
        }

        List<T> result = (criteria.setFirstResult(currentIndex).setMaxResults(pageSize)).list();

        return new Page<T>(new Long(currentIndex.longValue()), pageSize, new Long(totalCount.longValue()), result);
    }

    public Criteria createCriteria() {
        Session session = this.getSessionFactory().getCurrentSession();
        if (session == null) {
            session = this.getSessionFactory().openSession();
        }
        Criteria criteria = session.createCriteria(domainClass);
        return criteria;
    }


    public void update(T entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    public String getNamedQueryString(String name) {
        Query query = this.sessionFactory.getCurrentSession().getNamedQuery(name);
        return query.getQueryString();
    }

    public List<T> findByIds(List<ID> ids) {
        Query<T> query = this.sessionFactory.getCurrentSession().createQuery(
                String.format("from %s o where o.id in (:ids)", domainClass.getName()));
        query.setParameterList("ids", ids);
        return query.list();
    }

    public Integer count(String queryString, Map<String, Object> parameters) {
        Query<T> query =  this.sessionFactory.getCurrentSession().createQuery(queryString);

        if(null != parameters && 0 < parameters.size())  {
            Set<String> names = parameters.keySet();

            for(String name : names)  {
                query.setParameter(name, parameters.get(name));
            }
        }

        return ((Long)query.uniqueResult()).intValue();
    }
}
