package com.example.assignment1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {
    private String questionTitle;
    private List<String> options;
    private boolean[] isOptionCorrect; // Array to store the correctness of each option

    public static QuestionFragment newInstance(String questionTitle, List<String> options, boolean[] isCorrectArray) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString("questionTitle", questionTitle);
        args.putStringArrayList("options", (ArrayList<String>) options);
        args.putBooleanArray("isCorrectArray", isCorrectArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        TextView questionTextView = view.findViewById(R.id.questionTextView);
        questionTextView.setText(questionTitle);

        LinearLayout optionsLayout = view.findViewById(R.id.optionsLayout);

        if (options != null) {
            isOptionCorrect = new boolean[options.size()]; // Initialize the array

            for (int i = 0; i < options.size(); i++) {
                Button optionButton = createOptionButton(options.get(i), i, isOptionCorrect[i]);
                optionsLayout.addView(optionButton);
            }
        }

        return view;
    }

    private Button createOptionButton(String optionText, final int optionIndex, final boolean isCorrect) {
        Button optionButton = new Button(requireContext());
        optionButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.button_height)
        ));

        optionButton.setText(optionText);
        optionButton.setTextColor(Color.WHITE);
        optionButton.setTextSize(16);
        optionButton.setBackgroundResource(R.drawable.rounded_button_background);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) optionButton.getLayoutParams();
        params.setMargins(16, 0, 16, 80);
        optionButton.setLayoutParams(params);

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(optionIndex);
            }
        });

        return optionButton;
    }

    private void handleOptionClick(int selectedOptionIndex) {
        boolean isCorrect = isOptionCorrect[selectedOptionIndex];

        Log.d("QuestionFragment", "Selected Option " + selectedOptionIndex + " isCorrect: " + isCorrect);

        if (isCorrect) {
            showToast("Correct");
        } else {
            showToast("Incorrect");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionTitle = getArguments().getString("questionTitle", "");
            options = getArguments().getStringArrayList("options");
            isOptionCorrect = getArguments().getBooleanArray("isCorrectArray");
        }

        if (isOptionCorrect != null) {
            for (int i = 0; i < isOptionCorrect.length; i++) {
                Log.d("QuestionFragment", "Option " + i + " isCorrect: " + isOptionCorrect[i]);
            }
        }
    }
}
