package DAO.Impl;

import DAO.IHallDao;
import DAO.SessionFactory.SessionFactoryUtil;
import Entities.Hall;
import antlr.debug.TraceListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class HallDao implements IHallDao {
    private static SessionFactory sessionFactory;
    public HallDao() {
        sessionFactory = SessionFactoryUtil.getSessionFactory();
    }

    public void addHall(Hall hall) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(hall);
        transaction.commit();
        session.close();
    }

    public List<Hall> getAllHalls() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List <Hall> hallList = new ArrayList<Hall>(
                session.createQuery("from Hall").list());
        session.getTransaction().commit();
        return hallList;
    }
    public List <Hall> searchHallById(int id) throws JsonProcessingException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Hall.class).add(Restrictions.idEq(id));
        return criteria.list();
    }
    public List <Hall> searchHallByName(String name) throws JsonProcessingException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Hall.class).add(Restrictions.like("nameOfHall", name));
        return criteria.list();
    }
    public void deleteHallById(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Hall hall = session.get(Hall.class, id);
        session.delete(hall);
        transaction.commit();
        session.close();
    }
}
