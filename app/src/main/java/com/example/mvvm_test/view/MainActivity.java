package com.example.mvvm_test.view;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm_test.R;
import com.example.mvvm_test.db.Numbers;
import com.example.mvvm_test.viewmodel.MainViewModelFactory;
import com.example.mvvm_test.viewmodel.MyViewModel;
import com.example.mvvm_test.model.NumbersResponsePojo;
import com.example.mvvm_test.model.repositories.WebRepo;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private MyViewModel mMyViewModel;
    private EditText mET_enterNumber;
    private Button mBTN_enteredNumber, mBTN_randomNumber;
    private TextView mTV_showFact;
    private RecyclerView mCustomRecyclerView;
    private WebRepo mWebRepo;
    private MainViewModelFactory mFactory;
    private CustomAdapter mAdapter;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intViewModel();
        initViews();
        setRecyclerFeatures();
        observable();
        setListeners();
    }

    private void setRecyclerFeatures() {
        mAdapter = new CustomAdapter(new CustomAdapter.NumbersDiff());
        mCustomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCustomRecyclerView.setAdapter(mAdapter);
    }

    private void observable() {
        mMyViewModel.getDataFromDB().observe(this, new Observer<List<Numbers>>() {
            @Override
            public void onChanged(List<Numbers> numbers) {
                mAdapter.submitList(numbers);
            }
        });

        mMyViewModel.getPojoLiveData().observe(this, new Observer<NumbersResponsePojo>() {
            @Override
            public void onChanged(NumbersResponsePojo numbersResponsePojo) {
                if (numbersResponsePojo == null) {
                    return;
                }
                if (numbersResponsePojo.getText() == null) {
                    return;
                }
                mTV_showFact.setText(numbersResponsePojo.getText());
            }
        });

        mMyViewModel.getErrorMessageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        mMyViewModel.getShowLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        mMyViewModel.getHideLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void intViewModel() {
        mWebRepo = new WebRepo();
        mFactory = new MainViewModelFactory(getApplication(), mWebRepo);
        mMyViewModel = new ViewModelProvider(this, mFactory).get(MyViewModel.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBTN_enteredNumber:
                String number = mET_enterNumber.getText().toString();
                if (!number.matches("[0-9]*")) {
                    makeText(MainActivity.this, "Format not supported, please, enter an " +
                            "integer", LENGTH_LONG).show();
                } else if (number != null && !number.equals("")) {
                    mMyViewModel.getNumbers(number);
                } else if (number.equals("")) {
                    makeText(MainActivity.this, "Field is empty, enter number, please",
                            LENGTH_LONG).show();
                } else {
                    makeText(MainActivity.this, "Error", LENGTH_LONG).show();
                }
                break;
            case R.id.mBTN_randomNumber:
                String number1 = mET_enterNumber.getText().toString();
                mMyViewModel.getRandomNumbers(number1);
                break;
            default:
                makeText(MainActivity.this, "default ", LENGTH_LONG).show();
        }
    }

    public void setListeners() {
        mBTN_enteredNumber.setOnClickListener(this::onClick);
        mBTN_randomNumber.setOnClickListener(this::onClick);
    }

    private void initViews() {
        mTV_showFact = findViewById(R.id.mTV_showFact);
        mBTN_enteredNumber = findViewById(R.id.mBTN_enteredNumber);
        mBTN_randomNumber = findViewById(R.id.mBTN_randomNumber);
        mET_enterNumber = findViewById(R.id.mET_enterNumber);
        mCustomRecyclerView = findViewById(R.id.customRecyclerView);
        progressBar = findViewById(R.id.progressBar);
    }
}