package project.services.rest;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Inscriere;
import project.model.Proba;
import project.persistence.InscriereORMRepository;
import project.persistence.RepositoryException;
import project.persistence.database.InscriereDBRepository;
import project.persistence.database.ProbaDBRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@CrossOrigin
@RequestMapping("/project/inscriere")
public class InscriereController {

    private static final String template = "Hello, %s!";

    @Autowired
    private InscriereORMRepository inscriereRepository;


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="id", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @RequestMapping( method=RequestMethod.GET)
    public Inscriere[] getAll(){
        List<Inscriere> list = StreamSupport.stream(inscriereRepository.findAll().spliterator(), false).collect(Collectors.toList());
        Inscriere[] myArray = new Inscriere[list.size()];
        list.toArray(myArray);
        return myArray;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id){

        Inscriere proba = inscriereRepository.findBy(id);
        if (proba==null)
            return new ResponseEntity<String>("Inscriere not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Inscriere>(proba, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST)
    public Inscriere create(@RequestBody Inscriere proba){
        inscriereRepository.add(proba);
        Inscriere pr = inscriereRepository.findBy2(proba.getParticipant(), proba.getProba());
        proba.setId(pr.getId());
        return proba;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Inscriere update(@PathVariable Long id, @RequestBody Inscriere proba) {
        System.out.println("Updating proba ...");
        inscriereRepository.update(id,proba);
        return inscriereRepository.findBy(id);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("Deleting inscriere ... "+id);
        try {
            Inscriere ins = new Inscriere();
            ins.setId(id);
            inscriereRepository.delete(ins);
            return new ResponseEntity<Inscriere>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{inscriere}/participant")
    public String distanta(@PathVariable Long inscriere){
        Inscriere result=inscriereRepository.findBy(inscriere);
        System.out.println("Result ..."+result);
        return result.getParticipant().toString();
    }

    @RequestMapping("/{inscriere}/proba")
    public String stil(@PathVariable Long inscriere){
        Inscriere result=inscriereRepository.findBy(inscriere);
        System.out.println("Result ..."+result);
        return result.getProba();
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String probaError(RepositoryException e) {
        return e.getMessage();
    }
}
