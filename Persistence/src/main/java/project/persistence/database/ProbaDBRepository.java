package project.persistence.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.model.Distanta;
import project.model.Proba;
import project.model.Stil;
import project.persistence.interfaces.IProba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ProbaDBRepository implements IProba {

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public ProbaDBRepository(Properties props) {
        logger.info("Initializing ProbaDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public void add(Proba elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("insert into Proba (distanta,stil) values (?,?)")){
            preStmt.setString(1, elem.getDistanta().toString());
            preStmt.setString(2, elem.getStil().toString());

            int result = preStmt.executeUpdate();

            logger.trace("Saved {} instance",result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }



    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Proba> probe = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Proba")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    Distanta distanta = Distanta.valueOf(result.getString("distanta"));
                    Stil stil = Stil.valueOf(result.getString("stil"));

                    Proba p = new Proba(distanta, stil);
                    p.setId(id);
                    probe.add(p);
                }
            }
        }
        catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(probe);
        return probe;
    }

    @Override
    public Proba findBy(String id) {
        System.out.println("JDBC findBy 1 params");
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Proba where id=?")){
            preStmt.setLong(1,Long.parseLong(id));
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            System.out.println("findBy user, pass "+resOk);
            if (resOk) {
                Proba p = new Proba(Distanta.SHORT,Stil.LIBER);
                p.setDistanta(Distanta.valueOf(result.getString("distanta")));
                p.setStil(Stil.valueOf(result.getString("stil")));
                p.setId(result.getLong("id"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        System.out.println("JDBC delete 1 params");
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Proba where id=?")){
            preStmt.setLong(1,Long.parseLong(id));
            int result=preStmt.executeUpdate();
            System.out.println("delete by id "+ result);
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
    }
    @Override
    public void update(Long id, Proba pr) {
        System.out.println("JDBC update");
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Proba set stil=?, distanta=? where id=?")){
            preStmt.setString(1, pr.getStil().toString());
            preStmt.setString(2, pr.getDistanta().toString());
            preStmt.setLong(3, id);
            int result=preStmt.executeUpdate();
            System.out.println("update "+ result);
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
    }

    public Proba findBy2(String distanta, String stil) {
        System.out.println("JDBC findBy 2 params");
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Proba where distanta=? and stil=?")){
            preStmt.setString(1,distanta);
            preStmt.setString(2,stil);
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            System.out.println("findBy user, pass "+resOk);
            if (resOk) {
                Proba p = new Proba(Distanta.valueOf(distanta),Stil.valueOf(stil));
                p.setDistanta(Distanta.valueOf(result.getString("distanta")));
                p.setStil(Stil.valueOf(result.getString("stil")));
                p.setId(result.getLong("id"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return null;
    }

    public Iterable<Proba> findAllByStil(String stil) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Proba> probe = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Proba where stil=?")) {
            preStmt.setString(1,stil);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    Distanta distanta = Distanta.valueOf(result.getString("distanta"));
                    Stil still = Stil.valueOf(result.getString("stil"));

                    Proba p = new Proba(distanta, still);
                    p.setId(id);
                    probe.add(p);
                }
            }
        }
        catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(probe);
        return probe;
    }
}
