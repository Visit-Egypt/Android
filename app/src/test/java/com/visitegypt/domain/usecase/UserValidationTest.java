package com.visitegypt.domain.usecase;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserValidationTest {
    private UserValidation user1, user2, user3;

    @Before

    public void setup() {
        user1 = new UserValidation(
                "Yehia",
                "Hendy",
                "yayahend11@gmail.com",
                "01067755108",
                "12345678");
        user2 = new UserValidation(
                "Yehia1",
                "Hendy2",
                "yayahend11@gmailcom",
                "0106775510",
                "123456");
        user3 = new UserValidation(
                "Yehia1",
                "Hendy2",
                "@gmail.com",
                "0106775510",
                "123456");
    }

    @Test
    public void test_validUserEmail() {
        assertTrue("This is valid email ", user1.checkEmailValidation());
    }

    @Test
    public void test_notValidUserEmail_emailDoseNotContainDomain() {

        assertFalse("This email dose not contain domain ", user2.checkEmailValidation());
    }

    @Test
    public void test_notValidUserEmail_emailDoseNotContainNameBeforeAtAnnotation() {

        assertFalse("This email dose not has a name before @  ", user3.checkEmailValidation());
    }


    @Test
    public void test_validUserPassword() {
        assertTrue("This is not Valid password", user1.checkPasswordLengthValidation());
    }

    @Test
    public void test_notValidUserPassword_userPasswordLengthIsLargerThanEightDigits() {

        assertFalse("This password is valid", user2.checkPasswordLengthValidation());
    }

    @Test
    public void test_validName() {
        assertTrue("This is not valid first name for user", user1.checkFirstNameValidation());
        assertTrue("This is not valid first name for user", user1.checkLastNameValidation());
    }
    @Test
    public void test_notValidName_nameShouldNotContainNumbers() {
        assertFalse("This is  valid  name for user", user2.checkFirstNameValidation());
        assertFalse("This is  valid  name for user", user2.checkLastNameValidation());
    }

}