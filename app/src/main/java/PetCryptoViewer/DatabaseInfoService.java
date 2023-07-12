package PetCryptoViewer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DatabaseInfoService {

    @PersistenceContext
    private EntityManager entityManager;

    public void printDatabaseConnectionInfo() {
        Session session = entityManager.unwrap(Session.class);
        ConnectionProvider connectionProvider = session.getSessionFactory().getSessionFactoryOptions().getServiceRegistry()
                .getService(ConnectionProvider.class);

        if (connectionProvider != null) {
            try {
                java.sql.Connection connection = connectionProvider.getConnection();

                // Получение свойств подключения к базе данных
                String url = connection.getMetaData().getURL();
                String username = connection.getMetaData().getUserName();

                System.out.println("URL bd: " + url);
                System.out.println("Username: " + username);

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
