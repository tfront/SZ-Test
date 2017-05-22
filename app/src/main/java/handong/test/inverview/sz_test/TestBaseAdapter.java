package handong.test.inverview.sz_test;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class TestBaseAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private int[] mSectionIndices;
    private LayoutInflater mInflater;
    private String[] mDates;
    private List<Event> mEvents;

    private final DateTime START_DATE = new DateTime(2017, 1, 1, 0, 0, 0);
    private final DateTime END_DATE = new DateTime(2017, 12, 31, 0, 0, 0);

    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    public TestBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDates = getDates();
        mEvents = mergeEvents(mDates, convertEventsToMap(generateEvents()));
        mSectionIndices = getSectionIndices();
    }

    // TODO: replace with parameter, pass in from outside caller
    private List<Event> generateEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Hello World", "Beijing", "2017-01-01"));

        return events;
    }

    private Map<String, List<Event>> convertEventsToMap(List<Event> events) {
        Map<String, List<Event>> eventMap = new HashMap<>();
        for (Event event : events) {
            String date = event.date;
            if (!eventMap.containsKey(date)) {
                // initialise event list
                eventMap.put(date, new ArrayList<Event>());
            }
            eventMap.get(date).add(event);
        }
        return eventMap;
    }

    private String[] getDates() {
        List<String> ret = new ArrayList<>();

        Calendar calStart = Calendar.getInstance();
        calStart.set(2017, Calendar.JANUARY, 1);

        DateTime time = new DateTime(calStart);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2017, Calendar.DECEMBER, 31);

        while (time.getMillis() < calEnd.getTimeInMillis()) {
            ret.add(time.toString(DATETIME_FORMATTER));
            time = time.plusDays(1);
        }
        return ret.toArray(new String[ret.size()]);
    }

    private List<Event> mergeEvents(String[] dates, Map<String, List<Event>> eventMap) {
        List<Event> events = new ArrayList<>();
        for (String date : dates) {
            if (eventMap.containsKey(date)) {
                events.addAll(eventMap.get(date));
            } else {
                events.add(Event.emptyEvent(date));
            }
        }
        return events;
    }

    private int[] getSectionIndices() {
        int[] sections = new int[mEvents.size()];
        String lastEventDate = null;
        int lastEventIndex = 0;
        for (int i = 0; i < mEvents.size(); ++i) {
            Event e = mEvents.get(i);
            if (e.date.equals(lastEventDate)) {
                sections[i] = lastEventIndex;
            } else {
                lastEventIndex = i;
                lastEventDate = e.date;
            }
        }
        return sections;
    }

    @Override
    public int getCount() {
        return mEvents.size(); //mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.test_list_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mEvents.get(position).title);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        holder.text.setText(mDates[position]);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return position;
    }

    @Override
    public int getPositionForSection(int section) {
        return section;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mSectionIndices[position];
    }

    @Override
    public Object[] getSections() {
        return mDates;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

}
