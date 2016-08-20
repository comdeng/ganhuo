package com.huimang.ganhuo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huimang.ganhuo.R;
import com.huimang.ganhuo.activity.ArticleActivity;
import com.huimang.ganhuo.bean.Article;
import com.huimang.ganhuo.fragment.ArticleFragment;

import java.util.List;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {

    Activity activity;
    private List<Article> list;

    public ArticleListAdapter(Activity activity, List<Article> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.article_list, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Article item = this.list.get(position);
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());
        holder.pubtime.setText(item.getTime());
        if (item.getAuthor().indexOf("&") != -1) {
            holder.author.setText(Html.fromHtml(item.getAuthor()));
        } else {
            holder.author.setText(item.getAuthor());
        }
        if (!"".equals(item.getCover())) {
            holder.cover.setImageURI(Uri.parse(item.getCover()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ArticleActivity.class);
                intent.putExtra("url", item.get__LINK__());
                Log.v("hapn", item.get__LINK__());
                intent.putExtra("origin", item.getUrl());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("summary", item.getSummary());
                intent.putExtra("author", item.getAuthor());
                intent.putExtra("pubtime", item.getTime());
                intent.putExtra("reply", item.getReply());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView pubtime;
        public TextView summary;
        public TextView title;
        public SimpleDraweeView cover;

        TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView)itemView.findViewById(R.id.author);
            this.pubtime = (TextView)itemView.findViewById(R.id.pubtime);
            this.summary = (TextView)itemView.findViewById(R.id.summary);
            this.title = (TextView)itemView.findViewById(R.id.title);
            this.cover = (SimpleDraweeView) itemView.findViewById(R.id.imgcover);
        }
    }
}

