package voting;
import java.util.ArrayList;
import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

public class Moderator {

    private static long counter = 15789;

    private long id;
    private String name;


    @NotEmpty
    @NotNull
    private String email;

    @NotEmpty
    @NotNull
    private String password;

    private ArrayList<Poll> polls;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date created_at;

    //Constructor
    public Moderator(String name, String email,String password) {
        this.id = generateModeratorId();
        this.name = name;
        this.email = email;
        this.password = password;
        this.setCreated_at();
        polls = new ArrayList<Poll>();

    }
    public Moderator() {
    }

    private final Long generateModeratorId() {
        return Long.valueOf(++counter);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at() {
        this.created_at = java.util.Calendar.getInstance().getTime();
    }

   @JsonIgnore
   public ArrayList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {this.polls  = polls;
    }

    public void addPoll(Poll poll) {this.polls.add(poll) ;
    }

    //Get poll created by moderator using poll id
    public Poll getPollById(String id) {
        Poll poll = null;
        for (Poll p : this.polls)
        {
            if (p.getId().equals(id)) {
                poll = p;
                break;
            }
        }
        return poll;
    }

    //Remove poll created by moderator using poll id
    public void removePollById(String id) {
        Poll poll = getPollById(id);
        this.polls.remove(poll);
    }


}
