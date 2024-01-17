package com.example.assignment1;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

public class BiometricPromptUtils {
    private static final String TAG = "BiometricPromptUtils";

    public static BiometricPrompt createBiometricPrompt(
            AppCompatActivity activity,
            BiometricPrompt.AuthenticationCallback processSuccess) {
        // Using ContextCompat.getMainExecutor for compatibility
        Executor executor = ContextCompat.getMainExecutor(activity);

        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errCode, CharSequence errString) {
                super.onAuthenticationError(errCode, errString);
                Log.d(TAG, "errCode is " + errCode + " and errString is: " + errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "User biometric rejected.");
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, "Authentication was successful");
                processSuccess.onAuthenticationSucceeded(result);
            }
        };
        return new BiometricPrompt(activity, executor, callback);
    }

    public static BiometricPrompt.PromptInfo createPromptInfo(AppCompatActivity activity) {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(activity.getString(R.string.prompt_info_title))
                .setSubtitle(activity.getString(R.string.prompt_info_subtitle))
                .setDescription(activity.getString(R.string.prompt_info_description))
                .setConfirmationRequired(false)
                .setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_password))
                .build();
    }
}
