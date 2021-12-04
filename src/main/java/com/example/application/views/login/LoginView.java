package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@PageTitle("Login")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends Composite<LoginOverlay> {

    public LoginView(AuthService authService) {
        LoginOverlay loginOverlay = getContent();
        LoginI18n i18n = LoginI18n.createDefault();

        loginOverlay.setOpened(true);
        loginOverlay.setTitle("CarParking.Com");
        loginOverlay.setDescription("Built with ♥ by CodePhatGya");

        loginOverlay.addLoginListener(event -> {
            try {
                authService.authenticate(event.getUsername(), event.getPassword());
                UI.getCurrent().navigate("home");
            } catch (AuthService.AuthException e) {
                //                Notification.show("Wrong credentials."); // setError looks more sexy
                loginOverlay.setError(true);
            }
        });

        i18n.getForm().setForgotPassword("Sign Up");
        loginOverlay.setForgotPasswordButtonVisible(true);
        loginOverlay.addForgotPasswordListener(event -> {
            UI.getCurrent().navigate("register");
            loginOverlay.close(); // Just testing if sign up button is working or not
        });
        loginOverlay.setI18n(i18n);
    }

}