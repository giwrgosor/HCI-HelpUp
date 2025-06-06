package com.example.healthup.Pills;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.PillsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DisplayPillsActivity extends AppCompatActivity {

    private FloatingActionButton btn_addPill;
    private ImageButton btn_homePill;

    private RecyclerView recyclerView;
    private List<Pill> pillList;
    private PillsRecyclerViewAdapter adapter;

    private PillsDAO pillsDAO;
    private Pill pill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pills);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_addPill = findViewById(R.id.addPillIcon);
        btn_homePill = findViewById(R.id.homeViewPill);

        recyclerView = findViewById(R.id.pillsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pillsDAO = new PillsMemoryDAO();
        pillList = pillsDAO.findAll();

        adapter = new PillsRecyclerViewAdapter(this, pillList, pillsDAO);

        recyclerView.setAdapter(adapter);


        int spacingPx = 32;
        int grayColor = getResources().getColor(android.R.color.darker_gray);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, grayColor, spacingPx));

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.imageView2);
            if (background != null) {
                background.setImageResource(R.drawable.pills_dark_screen);
            }

            ImageView icon = findViewById(R.id.imageView4);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        btn_addPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPillsActivity.this, AddPillsActivity.class);
                startActivity(intent);
            }
        });

        btn_homePill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPillsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        pillList.clear();
        pillList.addAll(pillsDAO.findAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2001 && resultCode == RESULT_OK) {
            pillList.clear();
            pillList.addAll(pillsDAO.findAll());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }


}
