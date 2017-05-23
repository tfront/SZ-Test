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

    private final String START_DATE = "2017-01-01";
    private final String END_DATE = "2017-01-31";

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
        events.add(new Event("Event 0", "Beijing", "2017-01-01"));
        events.add(new Event("Event 1", "Beijing", "2017-01-01"));
        events.add(new Event("Event 2", "Beijing", "2017-01-03"));
        events.add(new Event("Event 3", "Beijing", "2017-01-03"));

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

        DateTime startTime = DateTime.parse(START_DATE, DATETIME_FORMATTER);
        DateTime endTime = DateTime.parse(END_DATE, DATETIME_FORMATTER);
        while (startTime.getMillis() < endTime.getMillis()) {
            ret.add(startTime.toString(DATETIME_FORMATTER));
            startTime = startTime.plusDays(1);
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
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        sectionIndices.add(0);
        String lastDate = mEvents.get(0).date;
        for (int i = 1; i < mEvents.size(); i++) {
            String date = mEvents.get(i).date;
            if (!lastDate.equals(date)) {
                lastDate = date;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
//        int[] sections = new int[mEvents.size()];
//        String lastDate = null;
//        int lastIndex = 0;
//        for (int index = 0; index < mEvents.size(); ++index) {
//            Event e = mEvents.get(index);
//            if (!e.date.equals(lastDate)) {
//                lastDate = e.date;
//                lastIndex = index;
//            }
//            sections[index] = lastIndex;
//        }
//        return sections;
    }

    @Override
    public int getCount() {
        return mEvents.size();
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
            holder.text = (TextView) convertView.findViewById(R.id.textItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event e = mEvents.get(position);

        holder.text.setText(e.isEmpty ? "No event." : e.title);

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
        holder.text.setText(mDates[getSectionForPosition(position)]);

        return convertView;
    }

    /**
     * Remember that these have to be static, position=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        return getSectionForPosition(position);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
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
