package project.persistence.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.Inscriere;
import project.persistence.interfaces.IInscriere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class InscriereDBRepository implements IInscriere {

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public InscriereDBRepository(Properties props) {
        logger.info("Initializing InscriereDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public void add(Inscriere elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("insert into Inscriere (Participant,Proba) values (?,?)")){
            preStmt.setString(1, elem.getParticipant());
            preStmt.setString(2, elem.getProba());

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
    public Iterable<Inscriere> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Inscriere> inscrieri = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Inscriere")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String Participant = result.getString("Participant");
                    String Proba = result.getString("Proba");

                    Inscriere i = new Inscriere(Participant, Proba);
                    i.setId(id);
                    inscrieri.add(i);
                }
            }
        }
        catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(inscrieri);
        return inscrieri;
    }

    @Override
    public void update(Long l, Inscriere elem) {

    }
}
