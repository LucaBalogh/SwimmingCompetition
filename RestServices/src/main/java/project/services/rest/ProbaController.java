package project.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Proba;
import project.persistence.RepositoryException;
import project.persistence.database.ProbaDBRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@CrossOrigin
@RequestMapping("/project/proba")
public class ProbaController {

    private static final String template = "Hello, %s!";

    @Autowired
    private ProbaDBRepository probaRepository;


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="id", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @RequestMapping( method=RequestMethod.GET)
    public Proba[] getAll(){
        List<Proba> list = StreamSupport.stream(probaRepository.findAll().spliterator(), false).collect(Collectors.toList());
        Proba[] myArray = new Proba[list.size()];
        list.toArray(myArray);
        return myArray;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        Proba proba = probaRepository.findBy(id);
        if (proba==null)
            return new ResponseEntity<String>("Proba not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Proba>(proba, HttpStatus.OK);
    }

//    @RequestMapping(value = "/{stil}", method = RequestMethod.GET)
//    public Proba[] getAllByStil(@PathVariable String stil){
//        List<Proba> list = StreamSupport.stream(probaRepository.findAllByStil(stil).spliterator(), false).collect(Collectors.toList());
//        Proba[] myArray = new Proba[list.size()];
//        list.toArray(myArray);
//        return myArray;
//    }

    @RequestMapping(method = RequestMethod.POST)
    public Proba create(@RequestBody Proba proba){
        probaRepository.add(proba);
        Proba pr = probaRepository.findBy2(proba.getDistanta().toString(), proba.getStil().toString());
        proba.setId(pr.getId());
        return proba;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Proba update(@PathVariable Long id, @RequestBody Proba proba) {
        System.out.println("Updating proba ...");
        probaRepository.update(id,proba);
        return probaRepository.findBy(id.toString());
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting proba ... "+id);
        try {
            probaRepository.delete(id);
            return new ResponseEntity<Proba>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{proba}/distanta")
    public String distanta(@PathVariable String proba){
        Proba result=probaRepository.findBy(proba);
        System.out.println("Result ..."+result);
        return result.getDistanta().toString();
    }

    @RequestMapping("/{proba}/stil")
    public String stil(@PathVariable String proba){
        Proba result=probaRepository.findBy(proba);
        System.out.println("Result ..."+result);
        return result.getStil().toString();
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String probaError(RepositoryException e) {
        return e.getMessage();
    }
}
