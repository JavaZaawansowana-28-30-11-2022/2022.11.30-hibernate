package com.comarch.szkolenia.hibernate;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class App {
    public static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().buildSessionFactory();

        User user = new User();
        user.setLogin("admin");
        user.setPassword("admin2");

        //persistUser(user);

        //System.out.println(user);

        //System.out.println(getAllUsers());

        //System.out.println(getUserByLogin("admin").get());

        User user2 = getUserByLogin("janusz2").get();
        //deleteUser(user2);
        user2.setPassword("zmienione");
        updateUser(user2);
    }

    public static void persistUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if(tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Query<User> query = session
                .createQuery("FROM com.comarch.szkolenia.hibernate.User", User.class);
        List<User> users = query.getResultList();
        session.close();
        return users;
    }

    public static Optional<User> getUserByLogin(String login) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery(
                "FROM com.comarch.szkolenia.hibernate.User WHERE login = :login",
                User.class);
        query.setParameter("login", login);
        try {
            User user = query.getSingleResult();
            session.close();
            return Optional.of(user);
        } catch (NoResultException e) {
            session.close();
            return Optional.empty();
        }
    }

    public static void deleteUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.remove(user);
        tx.commit();
        session.close();
    }

    public static void updateUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.merge(user);
        tx.commit();
        session.close();
    }
}
