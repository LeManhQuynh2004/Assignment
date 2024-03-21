package com.quynhlm.dev.assignment_and103;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quynhlm.dev.assignment_and103.Adapter.StudentAdapter;
import com.quynhlm.dev.assignment_and103.Dao.ApiServer;
import com.quynhlm.dev.assignment_and103.Model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiServer apiServer;
    private Uri imageUri;
    private StudentAdapter studentAdapter;
    private List<Student> list = new ArrayList<>();

    private TextView tv_title;
    private ImageView upload;
    private EditText ed_name, ed_idSv, ed_email, ed_score;
    private RadioButton ck1, ch2;

    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handleCallData();
        fab_add.setOnClickListener(v -> {
            showDialogAdd(0, null);
        });
    }

    private void showDialogAdd(int type, Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_product, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        upload = view.findViewById(R.id.img_upload);
        ck1 = view.findViewById(R.id.checkbox1);
        ch2 = view.findViewById(R.id.checkbox2);
        ed_email = view.findViewById(R.id.ed_email);
        ed_score = view.findViewById(R.id.ed_score);
        ed_name = view.findViewById(R.id.ed_name);
        ed_idSv = view.findViewById(R.id.ed_idSv);
        tv_title = view.findViewById(R.id.tv_title);

        if(type != 0){
            upload.setImageURI(Uri.parse(student.getAvatar()));
            tv_title.setText("Sửa sinh viên");
            ed_email.setText(student.getEmail());
            ed_idSv.setText(student.getId_Sv());
            ed_name.setText(student.getName());
            ed_score.setText(student.getScore() + "");
            if(student.getStatus() == "0"){
                ck1.setChecked(true);
            }else{
                ch2.setChecked(true);
            }
        }
        upload.setOnClickListener(v1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        view.findViewById(R.id.bt_save).setOnClickListener(mv -> {
            String email = ed_email.getText().toString().trim();
            String score = ed_score.getText().toString().trim();
            String name = ed_name.getText().toString().trim();
            String idSv = ed_idSv.getText().toString().trim();
            String url = (imageUri != null) ? imageUri.toString() : "";
            if(type == 0){
                if (email.isEmpty() || score.isEmpty() || name.isEmpty() || idSv.isEmpty() || url.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ck1.isChecked() && !ch2.isChecked()) {
                    Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                String status = "";
                if (ck1.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }
                Student newStudent = new Student();
                newStudent.setAvatar(url);
                newStudent.setName(name);
                newStudent.setId_Sv(idSv);
                newStudent.setEmail(email);
                newStudent.setScore(Float.parseFloat(score));
                newStudent.setStatus(status);

                Call<Void> call = apiServer.createStudent(newStudent);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                            list.add(newStudent);
                            studentAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Thêm sinh viên thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TAG", "onResponse: " + t.getMessage());
                    }
                });
            }else{
                if (email.isEmpty() || score.isEmpty() || name.isEmpty() || idSv.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ck1.isChecked() && !ch2.isChecked()) {
                    Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                String status = "";
                if (ck1.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }
                String imageUrl = (imageUri != null) ? imageUri.toString() : student.getAvatar();
                student.setAvatar(imageUrl);
                student.setName(name);
                student.setId_Sv(idSv);
                student.setEmail(email);
                student.setScore(Float.parseFloat(score));
                student.setStatus(status);

                Call<Void> call = apiServer.putStudent(student.get_id(),student);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sửa sinh viên thành công", Toast.LENGTH_SHORT).show();
                            studentAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Sửa sinh viên thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TAG", "onResponse: " + t.getMessage());
                    }
                });

            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView_Student);
        fab_add = findViewById(R.id.fab_add);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.112:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }

    private void handleCallData() {
        Call<List<Student>> call = apiServer.getAllStudent();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    studentAdapter = new StudentAdapter(MainActivity.this, list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(studentAdapter);
                    studentAdapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void UpdateItem(int position) {
                            Student student = list.get(position);
                            showDialogAdd(1,student);
                        }
                    });
                    Toast.makeText(MainActivity.this, "Call successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi chi tiết
                    Log.e("TAG", "onResponse: " + response.code());
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        upload.setImageURI(imageUri);
                    }
                }
            }
    );
}