package handong.test.inverview.sz_test;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Event {
    public String title;
    public String date;
    public String location;
    public DateTime time;

    public boolean allDay = false;
    public boolean isEmpty = false;

    public Event(String date, boolean empty) {
        this.date = date;
        this.isEmpty = empty;
    }

    public Event(String title, String location, String date) {
        this.title = title;
        this.location = location;
        this.date = date;
    }

    public static Event emptyEvent(String date) {
        return new Event(date, true);
    }
}
