package handong.test.inverview.sz_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class TestBaseAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private int[] mSectionIndices;
    private LayoutInflater mInflater;
    private String[] mDates;
    private List<Event> mEvents;

    public TestBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDates = getDates();
        mEvents = generateEvents();
        mSectionIndices = getSectionIndices();
    }

    private List<Event> generateEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Hello World", "Beijing", new DateTime(2017, 1, 1, 0, 0, 0)));

        return events;
    }

    private String[] getDates() {
        List<String> ret = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        Calendar calStart = Calendar.getInstance();
        calStart.set(2017, Calendar.JANUARY, 1);

        DateTime time = new DateTime(calStart);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2017, Calendar.DECEMBER, 31);

        while (time.getMillis() < calEnd.getTimeInMillis()) {
            ret.add(time.toString(formatter));
            time = time.plusDays(1);
        }
        return ret.toArray(new String[ret.size()]);
    }

    private int[] getSectionIndices() {
        int[] sections = new int[mEvents.size()];
        sections[0] = 0;
        return sections;
    }

    @Override
    public int getCount() {
        return mDates.length; //mEvents.size();
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
        return 0;
    }

    @Override
    public Object[] getSections() {
        return mDates;
    }

    public void clear() {
        mDates = new String[0];
        mSectionIndices = new int[0];
        notifyDataSetChanged();
    }

    public void restore() {
        mDates = getDates();
        mSectionIndices = getSectionIndices();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

}
