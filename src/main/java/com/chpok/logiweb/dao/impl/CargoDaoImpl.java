package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class CargoDaoImpl implements CargoDao {
    private static final String FIND_ALL_QUERY = "SELECT c FROM Cargo c";

    private final HibernateUtil hibernateUtil;

    public CargoDaoImpl(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public List<Cargo> findByName() {
        return null;
    }

    @Override
    public void save(Cargo entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB cargo saving exception", npe);
        }
    }

    @Override
    public Optional<Cargo> findById(Long id) {
        try (Session session = Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Cargo cargo = session.get(Cargo.class, id);

            session.getTransaction().commit();

            return Optional.of(cargo);
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB find cargo by id exception", npe);
        }
    }

    @Override
    public List<Cargo> findAll() {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final List<Cargo> allCargos = session.createQuery(FIND_ALL_QUERY, Cargo.class).getResultList();

            session.getTransaction().commit();

            return allCargos;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all cargos exception", npe);
        }
    }

    @Override
    public void update(Cargo entity) {
        try(Session session =
                    Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB cargo updating exception", npe);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final Cargo deletingCargo= session.load(Cargo.class, id);

            session.delete(deletingCargo);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB cargo deleting exception", npe);
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }
}
