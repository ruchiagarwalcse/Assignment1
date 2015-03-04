package voting;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class ModeratorController {

    private static ConcurrentHashMap<Long, Moderator> moderatorList= new ConcurrentHashMap<Long, Moderator>();
    public static Moderator getModeratorById(Long id) {
        return moderatorList.get(id);
    }

    private static final ConcurrentHashMap<String, Poll> pollList = new ConcurrentHashMap<String, Poll>();
    public Poll getPollById(String id) {
        return pollList.get(id);
    }

    //1. Create Moderator
    @RequestMapping(name = "api/v1/moderators", method = RequestMethod.POST)
    public ResponseEntity moderatorPost(@Valid Moderator m, BindingResult bindingResult, @RequestHeader(value = "Authorization") String s) {

        if (BasicAuth.isAuthorized(s)) {
        ArrayList<String> errMsg = new ArrayList<String>();
        if (m.getName() == null || m.getName().isEmpty() || bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    errMsg.add("ERROR: Please provide a value for " + fieldError.getField());
                }

            }
            if (m.getName() == null || m.getName().isEmpty()) {
                errMsg.add("ERROR: Please provide a value for name");
            }
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        }
        Moderator mod = new Moderator(m.getName(),m.getEmail(),m.getPassword());
            moderatorList.put(mod.getId(), mod);
        return new ResponseEntity<Moderator>(mod, HttpStatus.CREATED);
        }
        else{

            return new ResponseEntity("ERROR: You are not authorized to make this request", HttpStatus.BAD_REQUEST);
        }
    }

    //2. View Moderator
    @RequestMapping(value = "api/v1/moderators/{id}", method = RequestMethod.GET)
    public ResponseEntity moderatorGet(@PathVariable("id") long id,@RequestHeader(value = "Authorization") String s) {
        if (BasicAuth.isAuthorized(s) ) {
        Moderator m = getModeratorById(id);
        return new ResponseEntity(m, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request",HttpStatus.BAD_REQUEST) ;
        }
    }

    //3. Update Moderator
    @RequestMapping(value = "api/v1/moderators/{id}", method = RequestMethod.PUT)
    public ResponseEntity moderatorPut(@PathVariable("id") long id, @Valid Moderator m, BindingResult bindingResult, @RequestHeader(value = "Authorization") String s) {
        if (BasicAuth.isAuthorized(s) )
        {
            ArrayList<String> errMsg = new ArrayList<String>();
            if (bindingResult.hasErrors()) {
                for (Object object : bindingResult.getAllErrors()) {
                    if (object instanceof FieldError) {
                        FieldError fieldError = (FieldError) object;
                        errMsg.add("ERROR: Please provide a value for " + fieldError.getField());
                    }
                 }
                return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }
            Moderator mod = getModeratorById(id);
            mod.setEmail(m.getEmail());
            mod.setPassword(m.getPassword());
            return new ResponseEntity(mod, HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request", HttpStatus.BAD_REQUEST );
        }

    }

}