package eu.trnkatomas.precisesmssender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tomas on 27/06/16.
 */

public class MessageWrapper {

    String message;
    String number;
    Date scheduled;
    Date sent;
    Long id;
    DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    public MessageWrapper(String message, Date scheduled, String number, Long id) {
        this(message, number, scheduled, null, id);
    }

    public MessageWrapper(String message, String number, Date scheduled, Date sent, Long id) {
        this.message = message;
        this.number = number;
        this.scheduled = scheduled;
        this.sent = sent;
        this.id = id;
    }


    public MessageWrapper(String message, String number, Long scheduled, Long sent, Long id) {
        this.message = message;
        this.number = number;
        this.scheduled = new Date(scheduled);
        this.sent = (sent != null) ? new Date(sent) : null;
        this.id = id;
    }

    public String getDateAsString(){
        return formatter.format(this.scheduled);
    }

    public String getSentAsString(){
        return (this.sent != null) ? formatter.format(this.sent) : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date scheduled) {
        this.scheduled = scheduled;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateFormat getFormatter() {
        return formatter;
    }

    public void setFormatter(DateFormat formatter) {
        this.formatter = formatter;
    }
}
