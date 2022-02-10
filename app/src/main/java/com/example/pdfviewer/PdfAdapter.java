package com.example.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder>
{
    private Context context;
    private String layout;
    private final ArrayList<File> fileList;

    public PdfAdapter(Context context, ArrayList<File> fileList,String layout) {
        this.context = context;
        this.layout=layout;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layout.equalsIgnoreCase("grid")){
            View gridView= LayoutInflater.from(context).inflate(R.layout.vertical_pdf_view,parent,false);
            return new ViewHolder(gridView);
        }else if (layout.equalsIgnoreCase("linear")){
            View linearView= LayoutInflater.from(context).inflate(R.layout.horizontal_pdf_view,parent,false);
            return new ViewHolder(linearView);
        }
        else
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.vertical_pdf_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pdfName.setText(fileList.get(position).getName());
        holder.pdfName.setSelected(true);
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context,PdfViewActivity.class);
            intent.putExtra("name",fileList.get(position).getName());
            intent.putExtra("path",fileList.get(position).getAbsolutePath());
            intent.putExtra("pdf",fileList.get(position).getAbsoluteFile());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView pdfName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName=itemView.findViewById(R.id.pdfNameTextView);
        }
    }
}
