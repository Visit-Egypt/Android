package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;

public class UserValidation extends User {
    private String[] errors = new String[4];

    public UserValidation(String firstName, String lastName, String email, String phoneNumber, String password) {
        super(firstName, lastName, email, phoneNumber, password);
    }

    public UserValidation() {

    }

    public Boolean checkEmailValidation() {
        String em = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return this.getEmail().matches(em);
    }

    public Boolean checkPasswordLengthValidation() {

        if ((this.getPassword().length() >= 8) && (this.getPassword().length() <= 32)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkFirstNameValidation() {
        String regx = "^[a-zA-z ]*$";
        return this.getFirstName().matches(regx);
    }

    public Boolean checkLastNameValidation() {
        String regx = "^[a-zA-z ]*$";
        return this.getLastName().matches(regx);
    }

    public String[] checkValidations() {

        if (this.checkFirstNameValidation()) {
            errors[0] = "";
        } else {
            errors[0] = "Name shouldn't contains numbers";
        }
        if (this.checkLastNameValidation()) {
            errors[1] = "";
        } else {
            errors[1] = "Name shouldn't contains numbers";
        }
        if (this.checkEmailValidation()) {
            errors[2] = "";
        } else {
            errors[2] = "your email should be like abc@xyz.com";
        }
        if (this.checkPasswordLengthValidation()) {
            errors[3] = "";
        } else {
            errors[3] = "your password length should be more than 8 and less than 32 characters";
        }
        return errors;
    }

    public Boolean isValidUser() {
        for (byte i = 0; i < errors.length; i++) {
            if (!errors[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
