package DAO.Impl;
import DAO.*;
import DAO.SessionFactory.SessionFactoryUtil;
import Entities.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.*;

public class UserDao implements IUserDao {
    private SessionFactory sessionFactory;

    public UserDao() {
        sessionFactory = SessionFactoryUtil.getSessionFactory();
    }

    public Collection<User> getNeedUsers(String login, int passwordHashCode) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("login", login));
        criteria.add(Restrictions.eq("passwordHashCode", passwordHashCode));
        List<User> userList = criteria.list();
        return userList;
    }
}
