import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Primary
public class SessionFactoryS {

    private SessionFactory sessionFactory;

    private SessionFactoryS() {}

    public SessionFactory getInstance() {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return sessionFactory;
    }

    public void close() {
        sessionFactory.close();
        sessionFactory = null;
    }

}
