package com.tiaoshei.ganhuo.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tiaoshei.ganhuo.activity.R;
import com.tiaoshei.ganhuo.model.Article;

import java.util.List;

/**
 * Created by ronnie on 15/5/29.
 */
public class ArticleListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Article> list;

    public ArticleListAdapter(Context ctx) {
        this.mInflater = LayoutInflater.from(ctx);
    }

    public void setData(List<Article> list)
    {
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = this.mInflater.inflate(R.layout.list_view, null);
            holder = new ViewHolder();
            holder.author = (TextView)view.findViewById(R.id.author);
            holder.pubtime = (TextView)view.findViewById(R.id.pubtime);
            holder.title = (TextView)view.findViewById(R.id.title);
            holder.summary = (TextView)view.findViewById(R.id.summary);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        Article item = this.getItem(i);
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());
        holder.pubtime.setText(item.getTime());
        if (item.getAuthor().indexOf("&") != -1) {
            holder.author.setText(Html.fromHtml(item.getAuthor()));
        } else {
            holder.author.setText(item.getAuthor());
        }

        return view;
    }

    @Override
    public Article getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    public final class ViewHolder
    {
        public TextView author;
        public TextView pubtime;
        public TextView summary;
        public TextView title;
    }
}
