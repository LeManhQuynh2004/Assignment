package com.quynhlm.dev.assignment_and103.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quynhlm.dev.assignment_and103.Dao.ApiServer;
import com.quynhlm.dev.assignment_and103.ItemClickListener;
import com.quynhlm.dev.assignment_and103.Model.Student;
import com.quynhlm.dev.assignment_and103.R;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{
    Context context;
    List<Student> list;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.112:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiServer apiServer = retrofit.create(ApiServer .class);

    public StudentAdapter(Context context, List<Student> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = list.get(position);
        holder.tv_name.setText(student.getName());
        holder.tv_score.setText("ĐTB : "+student.getScore()+"");
        holder.tv_idSv.setText(student.getId_Sv());
        if(student.getStatus().equals("0")){
            holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_status.setText("(Đã ra trường)");
        }else{
            holder.tv_status.setTextColor(ContextCompat.getColor(context, R.color.blue));
            holder.tv_status.setText("(Đang học)");
        }
        Glide
                .with(context)
                .load(student.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.student)
                .into(holder.img_product);
        holder.img_delete.setOnClickListener(view -> {
            deleteItem(position);
        });

        holder.img_update.setOnClickListener(view -> {
            try {
                if (itemClickListener != null) {
                    itemClickListener.UpdateItem(position);
                }
            } catch (Exception e) {
                Log.e("TAG", "onBindViewHolder: " + e);
            }
        });

    }

    private void deleteItem(int position) {
        Student student = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Call<Void> call = apiServer.deleteStudent(student.get_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            list.remove(position); // Xóa sinh viên khỏi danh sách nếu thành công
                            notifyItemRemoved(position); // Cập nhật RecyclerView để hiển thị thay đổi
                            Toast.makeText(context, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa sinh viên thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onResponse: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TAG", "onFailure: "+t.getMessage());
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_idSv,tv_score,tv_status;
        ImageView img_delete,img_update,img_product;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_score = itemView.findViewById(R.id.tv_score);
            tv_idSv = itemView.findViewById(R.id.tv_idSv);
            tv_status = itemView.findViewById(R.id.tv_status);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_update = itemView.findViewById(R.id.img_update);
            img_product = itemView.findViewById(R.id.img_product);
        }
    }
}
