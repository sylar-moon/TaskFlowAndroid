package com.example.taskflow.dialogs;

import static java.lang.System.out;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.example.taskflow.MainActivity;
import com.example.taskflow.R;
import com.example.taskflow.RetrofitClient;
import com.example.taskflow.api.AuthApi;
import com.example.taskflow.dto.RegistrationDto;
import com.example.taskflow.dto.TokenDto;
import com.example.taskflow.dto.UserAuthDto;
import com.example.taskflow.enums.SortedTaskEnum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthDialog {
    private AuthApi authApi;
    private SortedTaskEnum sortedTask;
    private MainActivity mainActivity;
    private final String USERPIC = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.flaticon.com%2Ffree-icon%2Fuser-picture_21104&psig=AOvVaw2Pd0pa1Q3esBQqX5tyYTXa&ust=1724594403571000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCOjuvfjkjYgDFQAAAAAdAAAAABAE";

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    View dialogView;
    TextView nameDialog;
    EditText usernameField ;
    EditText emailField ;
    EditText passwordField ;
    Button authorizationButton ;
    CheckBox registerCheckBox ;
    ImageButton googleButton ;
    AlertDialog alertDialog ;

    public AuthDialog(AuthApi authApi, SortedTaskEnum sortedTask, MainActivity mainActivity) {

        this.authApi = authApi;
        this.sortedTask = sortedTask;
        this.mainActivity = mainActivity;
        initDialogElements();
        initUIComponents();

    }

    private void initUIComponents() {

        assert dialogView != null;
        nameDialog = dialogView.findViewById(R.id.titleDialog);
        usernameField = dialogView.findViewById(R.id.editName);
        emailField = dialogView.findViewById(R.id.editEmail);
        passwordField = dialogView.findViewById(R.id.editPassword);
        authorizationButton = dialogView.findViewById(R.id.authorizationButton);
        registerCheckBox = dialogView.findViewById(R.id.registerRadioButton);
        googleButton = dialogView.findViewById(R.id.googleOauth);

    }

    private void initDialogElements() {
        builder = new AlertDialog.Builder(mainActivity);
       inflater = LayoutInflater.from(mainActivity);

        dialogView = inflater.inflate(R.layout.authorization_view, null);
        builder.setView(dialogView);

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void show() {
        setOnClickGoogleButton();
        setOnCheckedRegisterBox();
        setOnClickAuthButton();
    }

    private void setOnClickAuthButton() {
        authorizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (fieldsValidation(username, email, password)) return;

                Call<TokenDto> tokenCall = registerCheckBox.isChecked()
                        ? authApi.registration(new RegistrationDto(username, email, password, USERPIC))
                        : authApi.getAuthToken(new UserAuthDto(email, password));

                tokenCall.enqueue(new Callback<TokenDto>() {
                    @Override
                    public void onResponse(Call<TokenDto> call, Response<TokenDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String token = response.body().getToken();
                            RetrofitClient.updateToken(token);
                            mainActivity.initApi();
                            alertDialog.dismiss();
                            mainActivity.getAllMyTasks(sortedTask);
                        } else {
                            Toast.makeText(mainActivity, "Your email or password is not correct", Toast.LENGTH_SHORT).show();
                            emailField.setText("");
                            passwordField.setText("");
                            usernameField.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenDto> call, Throwable t) {
                        out.println("fail");
                        Toast.makeText(mainActivity, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean fieldsValidation(String username, String email, String password) {
        if (registerCheckBox.isChecked()) {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(mainActivity, "All fields are required for registration", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(mainActivity, "Email and password are required", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void setOnCheckedRegisterBox() {
        registerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    nameDialog.setText("Registration");
                    authorizationButton.setText("Sign up");
                    usernameField.setVisibility(View.VISIBLE);
                } else {
                    nameDialog.setText("Authorization");
                    authorizationButton.setText("Sign in");
                    usernameField.setVisibility(View.GONE);
                }
            }
        });
    }


    private void setOnClickGoogleButton() {
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

                builder.setToolbarColor(ContextCompat.getColor(mainActivity, R.color.appLogoColor));

                builder.addDefaultShareMenuItem();
                CustomTabsIntent customTabsIntent = builder.build();

                customTabsIntent.launchUrl(mainActivity, Uri.parse("https://9b2f-89-105-226-244.ngrok-free.app/oauth2/authorization/google"));
            }
        });
    }


}
