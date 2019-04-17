package pku.lesson_evaluator;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> implements Filterable{
    
    private List<Class_item> mClassList;
    private List<Class_item> mFilteredClassList;
    
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView className;
        TextView classTeacher;
        TextView classScore;
        Button courseDetail;
        Button score;
        Button talking;
        mViewHolderClicks mListener;
        
        public ViewHolder(View itemView,mViewHolderClicks listener) {
            super(itemView);
            mListener=listener;
            className=itemView.findViewById(R.id.class_name);
            classTeacher=itemView.findViewById(R.id.class_teacher);
            classScore=itemView.findViewById(R.id.class_score);
            courseDetail=itemView.findViewById(R.id.course_detail);
            score=itemView.findViewById(R.id.score);
            talking=itemView.findViewById(R.id.talking);
            courseDetail.setOnClickListener(this);
            score.setOnClickListener(this);
            talking.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.course_detail:
                    //按照特定方式锁定
                    mListener.onCourseDetailClick(className.getText().toString());
                    break;
                case R.id.score:
                    mListener.onScoreClick(className.getText().toString()+"/"+classTeacher.getText().toString());
                    break;
                case R.id.talking:
                    mListener.onTalkingClick(className.getText().toString()+"/"+classTeacher.getText().toString());
                    break;
                default:
                    break;
            }
        }
    }

    public ClassAdapter(List<Class_item> class_items){
        mClassList=class_items;
        mFilteredClassList=class_items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        final ViewHolder holder=new ViewHolder(view, new mViewHolderClicks() {
            @Override
            public void onCourseDetailClick(String uid) {
                Intent intent=new Intent(view.getContext(),Class.class);
                intent.putExtra("UID",uid);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onScoreClick(String uid) {
                Intent intent=new Intent(view.getContext(),Score.class);
                intent.putExtra("UID",uid);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onTalkingClick(String uid) {
                Intent intent=new Intent(view.getContext(),Talking.class);
                intent.putExtra("UID",uid);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Class_item classItem=mFilteredClassList.get(position);
        holder.className.setText(classItem.getClassName());
        holder.classTeacher.setText(classItem.getClassTeacher());
        holder.classScore.setText(classItem.getClassScore());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredClassList = mClassList;
                } else {
                    List<Class_item> filteredList = new ArrayList<>();
                    for (Class_item classItem : mClassList) {
                        if (classItem.getClassName().toLowerCase().contains(charString)
                                || classItem.getClassName().toUpperCase().contains(charString)
                                || classItem.getClassScore().toLowerCase().contains(charString)
                                || classItem.getClassScore().toUpperCase().contains(charString)
                                || classItem.getClassTeacher().toUpperCase().contains(charString)
                                || classItem.getClassTeacher().toLowerCase().contains(charString)) {
                            filteredList.add(classItem);
                        }
                    }
                    mFilteredClassList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredClassList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredClassList = (List<Class_item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredClassList.size();
    }

    private interface mViewHolderClicks{
        public void onCourseDetailClick(String uid);
        public void onScoreClick(String uid);
        public void onTalkingClick(String uid);
    }
}
