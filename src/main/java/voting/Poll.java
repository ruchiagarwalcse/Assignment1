package voting;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


public class Poll {

    @JsonView(View.PollSummary.class)
    private String id;


    @NotEmpty
    @NotNull
    @JsonView(View.PollSummary.class)
    private String question;


    @NotEmpty
    @NotNull
    @JsonView(View.PollSummary.class)
    private String started_at;


    @NotEmpty
    @NotNull
    @JsonView(View.PollSummary.class)
    private String expired_at;


    @NotEmpty
    @NotNull
    @JsonView(View.PollSummary.class)
    private ArrayList<String> choice;

    @JsonView(View.PollSummaryWithResults.class)
    private ArrayList<Long> results;

    private static long counter = 28465;

    private final String generatePollId() {
        return Long.toString(Long.valueOf(++counter),36).toUpperCase();
    }

    //Constructor
    public Poll(String question, String started_at,String expired_at, ArrayList<String> choice) {
        this.id = generatePollId();
        this.question = question;
        this.started_at = started_at;
        this.expired_at = expired_at;
        this.choice = new ArrayList<String>(choice) ;
        results = new ArrayList<Long>() ;
        for (int i=0; i<choice.size();i++ )
        {
                   results.add(new Long(0));
        }
    }
    public Poll() {
        choice = new ArrayList<String>();
        results = new ArrayList<Long>() ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = generatePollId();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getchoice() {
        return choice;
    }

    public void setChoice(ArrayList<String> choice) {

        this.choice = choice;
        for (int i=0; i<choice.size();i++ )
        {
            results.add(new Long(0));
        }
    }

    public ArrayList<Long> getResults() {
        return results;
    }

    public void setResutls(ArrayList<Long> results) {this.results = results;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;

    }
}
