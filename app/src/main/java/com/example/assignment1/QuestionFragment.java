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
    private boolean[] isOptionCorrect;
    private Button[] optionButtons;


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

        if (options != null && isOptionCorrect != null && options.size() == isOptionCorrect.length) {
            optionButtons = new Button[options.size()];
            for (int i = 0; i < options.size(); i++) {
                optionButtons[i] = createOptionButton(options.get(i), i, isOptionCorrect[i]);
                optionsLayout.addView(optionButtons[i]);
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

        disableAllButtons();

        for (int i = 0; i < optionButtons.length; i++) {
            Button currentButton = optionButtons[i];

            if (isOptionCorrect[i]) {
                currentButton.setBackgroundResource(R.drawable.correct_button_background);
            } else {
                // Incorrect option or unselected option
                if (i == selectedOptionIndex) {
                    // Selected option
                    if (isCorrect) {
                        // Correct option
                        currentButton.setBackgroundResource(R.drawable.correct_button_background);
                    } else {
                        // Incorrect option
                        currentButton.setBackgroundResource(R.drawable.incorrect_button_background);
                    }
                } else {
                    // Unselected and not correct option
                    currentButton.setBackgroundResource(R.drawable.rounded_button_background);
                }
            }
        }

        if (isCorrect) {
            showToast("Correct");
        } else {
            showToast("Incorrect");
        }
    }


    private void disableAllButtons() {
        for (Button button : optionButtons) {
            button.setEnabled(false);
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
            Log.d("QuestionFragment", "onCreate - isOptionCorrect size: " + isOptionCorrect.length);
            for (int i = 0; i < isOptionCorrect.length; i++) {
                Log.d("QuestionFragment", "onCreate - Option " + i + " isCorrect: " + isOptionCorrect[i]);
            }
        } else {
            Log.d("QuestionFragment", "onCreate - isOptionCorrect is null");
        }

        if (isOptionCorrect == null) {
            isOptionCorrect = new boolean[options.size()];
        }
    }
}
