package project.persistence.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.Inscriere;
import project.model.Participant;
import project.persistence.interfaces.IParticipant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ParticipantDBRepository implements IParticipant {

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public ParticipantDBRepository(Properties props) {
        logger.info("Initializing ParticipantDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Inscriere> findAllByProba(String p) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Inscriere> inss = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select Participant,Proba from Inscriere where Proba=?")){
            preStmt.setString(1,p);
            ResultSet result=preStmt.executeQuery();
            System.out.println("findByProba proba "+ "resOk");
            while(result.next()){
                String participant = result.getString("Participant");
                String proba = result.getString("Proba");
                Inscriere ins = new Inscriere(participant,proba);
                inss.add(ins);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        logger.traceExit(inss);
        return inss;
    }

    @Override
    public void add(Participant elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("insert into Participant (name,age) values (?,?)")){
            preStmt.setString(1, elem.getName());
            preStmt.setInt(2, elem.getAge());
            preStmt.setString(3, elem.getPasswd());

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
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Participant")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    String passwd = result.getString("passwd");

                    Participant p = new Participant(name, age,passwd);
                    p.setId(id);
                    participants.add(p);
                }
            }
        }
        catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(participants);
        return participants;
    }

    @Override
    public void update(Long l, Participant elem) { }

    @Override
    public Participant findBy(String username, String pass) {
        System.out.println("JDBC findBy 2 params");
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select name from Participant where name=? and passwd=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,pass);
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            System.out.println("findBy user, pass "+resOk);
            if (resOk) {
                Participant user=new Participant(username,0,"");
                user.setName(result.getString("name"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return null;
    }
}
