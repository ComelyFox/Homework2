package org.example.dao;

import org.example.entity.Users;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(Users user) {
        log.info("Начато сохранение пользователя: name={}, email={}, age={}, created_at={}",
                user.getName(), user.getEmail(), user.getAge(), user.getCreated_at());

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(user);

            tx.commit();
            log.info("Пользователь успешно сохранен: id={}", user.getId());
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
                log.info("Выполнен откат транзакции при сохранении пользователя");
            }
            log.error("Ошибка при сохранении пользователя", ex);
            throw ex;
        }
    }

    @Override
    public Optional<Users> findById(Long id) {
        log.info("Поиск пользователя по id={}", id);

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Users user = session.get(Users.class, id);

            tx.commit();

            if (user != null) {
                log.info("Пользователь найден: id={}, email={}, created_at = {}", user.getId(), user.getEmail(), user.getCreated_at());
            } else {
                log.info("Пользователь с id={} не найден", id);
            }

            return Optional.ofNullable(user);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
                log.info("Выполнен откат транзакции при поиске пользователя по id={}", id);
            }
            log.error("Ошибка при поиске пользователя по id {}", id, ex);
            throw ex;
        }
    }

    @Override
    public List<Users> findAll() {
        log.info("Запрос на получение всех пользователей");

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Query<Users> query = session.createQuery("From Users", Users.class);
            List<Users> result = query.getResultList();

            tx.commit();
            log.info("Получен список пользователей: количество={}", result.size());
            return result;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
                log.info("Выполнен откат транзакции при получении списка пользователей");
            }
            log.error("Ошибка при получении списка пользователей", ex);
            throw ex;
        }
    }

    @Override
    public void update(Users user) {
        log.info("Обновление пользователя: id={}, name={}, email={}, age={}, created_at={}",
                user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreated_at());

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(user);

            tx.commit();
            log.info("Пользователь успешно обновлен: id={}", user.getId());
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
                log.info("Выполнен откат транзакции при обновлении пользователя id={}", user.getId());
            }
            log.error("Ошибка при обновлении пользователя", ex);
            throw ex;
        }
    }

    @Override
    public void deleteById(Long id) {
        log.info("Удаление пользователя по id={}", id);

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Users user = session.get(Users.class, id);
            if (user != null) {
                session.remove(user);
                log.info("Пользователь найден и удален: id={}", id);
            } else {
                log.info("Пользователь с id={} не найден, удаление не выполнено", id);
            }

            tx.commit();
            log.info("Транзакция удаления пользователя id={} успешно завершена", id);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
                log.info("Выполнен откат транзакции при удалении пользователя id={}", id);
            }
            log.error("Ошибка при удалении пользователя с id {}", id, ex);
            throw ex;
        }
    }
}
