package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.regex.Pattern;

@Route("register")
public class RegisterView extends VerticalLayout {

    private final AuthService authService;
//    private Component formLayout;
    public RegisterView(AuthService authService) {
        this.authService = authService;

        H3 title = new H3("Signup form");

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        TextArea address = new TextArea("Residential address");
        EmailField email = new EmailField("Email ID");
        TextField mobile = new TextField("Mobile number");
        TextField registrationNumber = new TextField("Car registration number");

        Span errorMessage = new Span();

        Button submitButton = new Button("Submit" , event -> register(
                        firstName.getValue(),
                        lastName.getValue(),
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue(),
                        address.getValue(),
                        mobile.getValue(),
                        email.getValue(),
                        registrationNumber.getValue()
                ));
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Create a FormLayout with all our components. The FormLayout doesn't have any
        // logic (validation, etc.), but it allows us to configure Responsiveness from
        // Java code and its defaults looks nicer than just using a VerticalLayout.
        FormLayout formLayout = new FormLayout(title, firstName, lastName, username, password1, password2,
                address, email, mobile, registrationNumber,submitButton);

        // Restrict maximum width and center on page
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        // Allow the form layout to be responsive. On device widths 0-490px we have one
        // column, then we have two. Field labels are always on top of the fields.
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        // These components take full width regardless if we use one column or two (it
        // just looks better that way)
        formLayout.setColspan(title, 2);
        formLayout.setColspan(errorMessage, 2);
        formLayout.setColspan(submitButton, 2);

        // Add some styles to the error message to make it pop out
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        // Add the form to the page
        add(formLayout);
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