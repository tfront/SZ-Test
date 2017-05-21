package handong.test.inverview.sz_test;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Event {
    public String title;
    public DateTime time;
    public String location;

    public Event(String title, String location, DateTime time) {
        this.title = title;
        this.location = location;
        this.time = time;
    }
}
