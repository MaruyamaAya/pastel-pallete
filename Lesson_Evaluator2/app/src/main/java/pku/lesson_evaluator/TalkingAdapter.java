package pku.lesson_evaluator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TalkingAdapter extends RecyclerView.Adapter<TalkingAdapter.ViewHolder>{

    private List<Talking_item> mTalkingList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView TalkingId;
        TextView TalkingTime;
        TextView TalkingContent;

        public ViewHolder(View itemView) {
            super(itemView);
            TalkingId=itemView.findViewById(R.id.talking_id);
            TalkingTime=itemView.findViewById(R.id.talking_time);
            TalkingContent=itemView.findViewById(R.id.talking_content);
        }
    }

    public TalkingAdapter(List<Talking_item> talking_items) {
        mTalkingList=talking_items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.talking_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Talking_item talkingItem=mTalkingList.get(position);
        holder.TalkingTime.setText(talkingItem.getTalkingTime());
        holder.TalkingId.setText(talkingItem.getTalkingId());
        holder.TalkingContent.setText(talkingItem.getTalkingContent());
    }

    @Override
    public int getItemCount() {
        return mTalkingList.size();
    }
}
