package com.doodle.Authentication.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.City;
import com.doodle.Authentication.service.AuthService;
import com.doodle.utils.PrefManager;
import com.raywenderlich.android.validatetor.ValidateTor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends AndroidViewModel {

    ValidateTor validateTor;
    PrefManager manager;

    public SignupViewModel(@NonNull Application application) {
        super(application);
        validateTor = new ValidateTor();
        manager = new PrefManager(application);

    }


    //ViewModel

    public String additionFunctions(String v1, String v2) {
        int n1 = Integer.valueOf(v1);
        int n2 = Integer.valueOf(v2);
        int result = n1 + n2;
        return String.valueOf(result);
    }

    public void validateNameField(EditText edt_text) {
        String str = edt_text.getText().toString();

        if (validateTor.isEmpty(str)) {
            edt_text.setError("Field is empty!");
            App.setIsValidate(false);
        }
        if (validateTor.isAtleastLength(str, 2)
                && validateTor.isAtMostLength(str, 24)
                && validateTor.isAlpha(str)

        ) {
            App.setIsValidate(true);
        } else {
            {
                edt_text.setError("Be between 2-24 letters" + "\n" + "Letters only");

                App.setIsValidate(false);
            }
        }
    }

    public void validateEmailField(EditText edt_email) {

        String str = edt_email.getText().toString();

        if (validateTor.isEmpty(str)) {
            edt_email.setError("Field is empty!");
            App.setIsValidate(false);
        }

        if (!validateTor.isEmail(str)) {
            edt_email.setError("Invalid Email entered!");
            App.setIsValidate(false);
        } else {
            // Toast.makeText(getApplication(), "Valid Email!", Toast.LENGTH_SHORT).show();
            App.setIsValidate(true);
        }
    }

    String password, confirmPassword;

    public void validatePasswordField(EditText edt_password) {
        password = edt_password.getText().toString();
        // Check if password field is empty
        if (validateTor.isEmpty(password)) {
            edt_password.setError("Field is empty!");
            App.setIsValidate(false);
        }

        if (validateTor.isAtleastLength(password, 8)
                && validateTor.isAtMostLength(password, 20)
                && validateTor.hasAtleastOneDigit(password)
                && validateTor.hasAtleastOneLetter(password)
                && !validateTor.containsSubstring(password, " ")
        ) {
            // Valid Password
            App.setIsValidate(true);
        } else {
            // Invalid Password, handle in ui
            edt_password.setError("Be between 8-20 characters" + "\n" + "Contain at least 1 letter" + "\n" + "Contain at least 1 digit" + "\n" + "Not contain a space");
            App.setIsValidate(false);
        }
    }

    public void validateConfirmPasswordField(EditText edt_password) {
        confirmPassword = edt_password.getText().toString();
        // Check if password field is empty
        if (validateTor.isEmpty(confirmPassword)) {
            edt_password.setError("Field is empty!");
            App.setIsValidate(false);
        }

        if (validateTor.containsSubstring(confirmPassword, password)
        ) {
            // Valid Password
            App.setIsValidate(true);
        } else {
            // Invalid Password, handle in ui
            edt_password.setError("This field is required" + "\n" + "Password miss match");
            App.setIsValidate(false);
        }
}

    public void validateCreditCardField(EditText edt_email) {

        String str = edt_email.getText().toString();

        if (validateTor.isEmpty(str)) {
            edt_email.setError("Field is empty!");
        }

        if (!validateTor.validateCreditCard(str)) {
            edt_email.setError("Invalid Credit Card number!");
        } else {
            Toast.makeText(getApplication(), "Valid Credit Card Number!", Toast.LENGTH_SHORT).show();
        }
    }


    public void requestData(int id) {
        AuthService webService =
                AuthService.retrofitForCity.create(AuthService.class);
        Call<City> call = webService.cities(id);
        sendRequest(call);
    }


    City city;
    public void sendRequest(Call<City> call) {

        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {

                city = response.body();

                Log.d("Message", city.toString());
              //  Toast.makeText(getApplication(), "Message: " + city.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {

            }
        });
    }


}
