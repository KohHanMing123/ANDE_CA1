package com.example.assignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {
    private String questionTitle;
    private List<String> options;

    public static QuestionFragment newInstance(String questionTitle, List<String> options) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString("questionTitle", questionTitle);
        args.putStringArrayList("options", (ArrayList<String>) options);
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
            for (String option : options) {
                Button optionButton = new Button(requireContext());
                optionButton.setText(option);
                optionsLayout.addView(optionButton);
            }
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionTitle = getArguments().getString("questionTitle", "");
            options = getArguments().getStringArrayList("options");
        }
    }
}
