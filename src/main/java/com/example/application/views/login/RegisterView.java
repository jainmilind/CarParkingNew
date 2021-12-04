package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.Route;

import java.util.regex.Pattern;

@Route("register")
public class RegisterView extends Composite {

    private final AuthService authService;

    public RegisterView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        TextArea address = new TextArea("Residential address");
        EmailField email = new EmailField("Email ID");
        TextField mobile = new TextField("Mobile number");
        TextField registrationNumber = new TextField("Car registration number");

        return new VerticalLayout(
                new H2("Register"),
                firstName,
                lastName,
                username,
                password1,
                password2,
                address,
                email,
                mobile,
                registrationNumber,
                new Button("Send", event -> register(
                        firstName.getValue(),
                        lastName.getValue(),
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue(),
                        address.getValue(),
                        mobile.getValue(),
                        email.getValue(),
                        registrationNumber.getValue()
                ))
        );
    }
    private boolean validateFields(String firstName, String lastName, String username, String password1, String password2,
                          String address, String mobile, String email, String registrationNumber) {
        if (firstName.trim().isEmpty()) {
            Notification.show("Enter a first name");
            return false;
        } else if (lastName.trim().isEmpty()) {
            Notification.show("Enter a last name");
            return false;
        } else if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
            return false;
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
            return false;
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
            return false;
        } else if (!Pattern.matches("[0-9]{10}", mobile)) {
            Notification.show("Enter a valid mobile number");
            return false;
        } else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email)) {
            Notification.show("Enter a valid email ID");
            return false;
        }
        return true;
    }

    private void register(String firstName, String lastName, String username, String password1, String password2,
                          String address, String mobile, String email, String registrationNumber) {
        if (validateFields(firstName, lastName, username, password1, password2,
                address, mobile, email, registrationNumber)) {
            authService.register(firstName, lastName, username, password1,
                address, mobile, email, registrationNumber);
            Notification.show("Check your email.");
        }
    }
}