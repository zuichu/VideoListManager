package me.zuichu.videolistmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.tv_recyclerview)
    TextView tv_recyclerview;
    @Bind(R.id.tv_listview)
    TextView tv_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_recyclerview.setOnClickListener(this);
        tv_listview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_recyclerview:
                intent = new Intent(this, RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_listview:
                intent = new Intent(this, ListViewActivity.class);
                startActivity(intent);
                break;
        }
    }
}
