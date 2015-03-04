package voting;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class PollController {

    private static final ConcurrentHashMap<String, Poll> pollList = new ConcurrentHashMap<String, Poll>();

    public Poll getPollById(String id) {
        return pollList.get(id);
    }


    //4. Create a poll
    @JsonView(View.PollSummary.class)
    @RequestMapping(value="api/v1/moderators/{id}/polls", method= RequestMethod.POST)
    public ResponseEntity pollPost(@PathVariable("id") long moderatorId,@Valid Poll p,BindingResult bindingResult, @RequestHeader(value = "Authorization") String s) {

       if (BasicAuth.isAuthorized(s) )
        {

            Moderator mod = ModeratorController.getModeratorById(moderatorId);

            ArrayList<String> errMsg = new ArrayList<String>();
            if (mod == null || bindingResult.hasErrors() || p.getchoice().size() == 0) {
                for (Object object : bindingResult.getAllErrors()) {
                    if(object instanceof FieldError) {
                        FieldError fieldError = (FieldError) object;
                        errMsg.add("ERROR: Please provide a value for " + fieldError.getField());
                    }

                }
                if (mod == null){
                    errMsg.add("ERROR: No moderator found for specified Moderator ID");
                }
                if (p.getchoice().size() == 0){
                    errMsg.add("ERROR: Please specify choice");
                }
                return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }

            Poll newPoll = new Poll(p.getQuestion(),p.getStarted_at(),p.getExpired_at(),p.getchoice());
            mod.addPoll(newPoll) ;
            pollList.putIfAbsent(newPoll.getId(), newPoll);
            return new ResponseEntity(newPoll, HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request",HttpStatus.BAD_REQUEST) ;
        }
        //return mod;
    }

    //5. View a poll without results
    @JsonView(View.PollSummary.class)
    @RequestMapping(value = "api/v1/polls/{id}", method = RequestMethod.GET)
    public Poll pollGet(@PathVariable("id") String id) {
        Poll p = getPollById(id) ;
        return p;
    }

    //6. View a poll with result
    @JsonView(View.PollSummaryWithResults.class)
    @RequestMapping(value="api/v1/moderators/{mid}/polls/{pid}", method = RequestMethod.GET)
    public ResponseEntity pollWithResultGet(@PathVariable("pid") String pid, @PathVariable("mid") long mid, @RequestHeader(value = "Authorization") String s) {
        if (BasicAuth.isAuthorized(s) )
        {
            Moderator mod = ModeratorController.getModeratorById(mid);
            Poll p = mod.getPollById(pid);
            return new ResponseEntity(p,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request",HttpStatus.BAD_REQUEST) ;
        }

    }

    //7. List all polls
    @JsonView(View.PollSummaryWithResults.class)
    @RequestMapping(value="api/v1/moderators/{mid}/polls", method = RequestMethod.GET)
    public ResponseEntity listAllPollsGet(@PathVariable("mid") long mid, @RequestHeader(value = "Authorization") String s) {
        if (BasicAuth.isAuthorized(s) )
        {
            Moderator mod = ModeratorController.getModeratorById(mid);
            return new ResponseEntity(mod.getPolls(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request",HttpStatus.BAD_REQUEST) ;
        }

    }

    //8. Delete a poll
    @RequestMapping(value="api/v1/moderators/{mid}/polls/{pid}", method = RequestMethod.DELETE)
    public ResponseEntity pollDelete(@PathVariable("pid") String pid, @PathVariable("mid") long mid, @RequestHeader(value = "Authorization") String s) {
        if (BasicAuth.isAuthorized(s) )
        {
            Moderator mod = ModeratorController.getModeratorById(mid);
            mod.removePollById(pid);
            pollList.remove(pid);
            return new ResponseEntity("",HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity("ERROR: You are not authorized to make this request",HttpStatus.BAD_REQUEST) ;
        }
    }

    //9. Vote a poll
    @JsonView(View.PollSummaryWithResults.class)
    @RequestMapping(value="api/v1/polls/{pid}",method=RequestMethod.PUT)
    public ResponseEntity pollVotePUT(@PathVariable("pid") String pid, @RequestParam(value="choice") String index) {
        Poll p = getPollById(pid);
        if (p != null)
        {
            p.getResults().set(Integer.parseInt(index), p.getResults().get(Integer.parseInt(index)) +1);
            return new ResponseEntity("",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity("ERROR: No poll found for specified Poll ID", HttpStatus.BAD_REQUEST);
    }

}