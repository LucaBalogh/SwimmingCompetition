package project.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import project.model.Inscriere;
import project.persistence.interfaces.IInscriere;

import java.util.List;

@Component
public class InscriereORMRepository implements IInscriere {

    private SessionFactory sessionFactory;

    public InscriereORMRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //INSERT
    @Override
    public void add(Inscriere i){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Inscriere ins = new Inscriere(i.getParticipant(),i.getProba());
                session.save(ins);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //SELECT
    @Override
    public Iterable<Inscriere> findAll(){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Inscriere> ins =
                        session.createQuery("from Inscriere as i order by i.Id asc", Inscriere.class).
                                setMaxResults(10).
                                list();
                System.out.println(ins.size() + " record(s) found:");
                for (Inscriere i : ins) {
                    System.out.println(i.getParticipant() + ' ' + i.getProba());
                }
                tx.commit();
                return ins;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public void update(Long l, Inscriere i){
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Inscriere ins = session.load( Inscriere.class, l );

//                Query<Inscriere> query = session.createQuery("from Inscriere as u where u.Id=:idU", Inscriere.class);
//                query.setParameter("idU", 2L);
//                Inscriere ts = query.uniqueResult();

                ins.setParticipant( i.getParticipant() );
                ins.setProba( i.getProba() );

                session.update(ins);
                tx.commit();

            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    //DELETE
    public void delete(Inscriere i){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Query<Inscriere> query = session.createQuery("from Inscriere as u where u.Id=:idU", Inscriere.class);
//                query.setParameter("idU", id);
//                Inscriere ts = query.uniqueResult();

                Inscriere ins = session.createQuery("from Inscriere where Id like i.getId()", Inscriere.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem inscrierea " + ins.getId());
                session.delete(ins);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public Inscriere findBy(Long id){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<Inscriere> query = session.createQuery("from Inscriere as u where u.Id=:idU", Inscriere.class);
                query.setParameter("idU", id);
                Inscriere ins = query.uniqueResult();
                tx.commit();
                return ins;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    public Inscriere findBy2(String p, String pr){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<Inscriere> query = session.createQuery("from Inscriere as u where u.participant=:pU and u.proba=:prU", Inscriere.class);
                query.setParameter("pU", p);
                query.setParameter("prU", pr);
                Inscriere ins = query.uniqueResult();
                tx.commit();
                return ins;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    //    public static void main(String[] args) {
//        try {
//            initialize();
//
//            InscriereRepoORM test = new InscriereRepoORM();
//            test.add(new Inscriere("Filip", "MIXT - LONG"));
//            test.findAll();
//            test.delete(2L);
//            test.findAll();
////            test.findAll();
////            Inscriere t = new Inscriere("Ben","SHORT - LIBER");
////            t.setId(3);
////            test.update(t);
//        }catch (Exception e){
//            System.err.println("Exception "+e);
//            e.printStackTrace();
//        }finally {
//            close();
//        }
//    }

//    static SessionFactory sessionFactory;
//    static void initialize() {
//        // A SessionFactory is set up once for an application!
//        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .configure() // configures settings from hibernate.cfg.xml
//                .build();
//        try {
//            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
//        }
//        catch (Exception e) {
//            System.err.println("Exception "+e);
//            StandardServiceRegistryBuilder.destroy( registry );
//        }
//    }
//
//    static void close(){
//        if ( sessionFactory != null ) {
//            sessionFactory.close();
//        }
//
//    }
}
