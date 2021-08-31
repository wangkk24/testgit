package com.pukka.ydepg.moudule.multviewforstb.multiview.module.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.pukka.ydepg.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PickFileActivity extends AppCompatActivity implements PickFileAdapter.FilePickClick{

    private String rootPath = Environment.getExternalStorageDirectory()+"";// 根目录
    private RecyclerView recyclerView;
    private TextView currentPath;
    private PickFileAdapter pickFileAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pick);
        recyclerView = findViewById(R.id.recyclerview);
        currentPath = findViewById(R.id.current_path);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        pickFileAdapter = new PickFileAdapter(this);
        pickFileAdapter.setFilePickClick(this);
        recyclerView.setAdapter(pickFileAdapter);
        getFileDir(rootPath);
    }

    // 获取文件列表方法
    private void getFileDir(String path) {
        currentPath.setText(path);//显示当前路径
        List<FileBean> items = new ArrayList<FileBean>();
        // 获取当前路径下的文件
        File presentFile = new File(path);
        File[] files = presentFile.listFiles();

        if (!path.equals(rootPath)) {
            // 返回根目录
            items.add(new FileBean("返回根目录", rootPath, false));
            // 返回上一级目录
            items.add(new FileBean("返回上一级目录", presentFile.getParent(), false));
        }else{
            items.add(new FileBean("使用APK内置地址", "assets", true));
        }

        // 添加当前路径下的所有的文件名和路径
        for (File f : files) {
            items.add(new FileBean(f.getName(), f.getPath(), f.isFile()));
        }

        // 设置列表适配器
        pickFileAdapter.setFileBeanList(items);
        pickFileAdapter.notifyDataSetChanged();
    }


        @Override
        public void onFileClick(FileBean fileBean) {
            if(fileBean.isFile()){
                Intent intent = new Intent();
                intent.putExtra("filePath", fileBean.getPath());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else{
                getFileDir(fileBean.getPath());
            }
        }
}
