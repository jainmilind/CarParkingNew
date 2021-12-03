package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "login")
@PageTitle("Login")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends Composite<LoginOverlay> {

    public LoginView(AuthService authService) {
//        setId("login-view");
//        var username = new TextField("Username");
//        var password = new PasswordField("Password");
//        add(
//                new H1("Welcome"),
//                username,
//                password,
//                new Button("Login", event -> {
//                    try {
//                        authService.authenticate(username.getValue(), password.getValue());
//                        UI.getCurrent().navigate("home");
//                    } catch (AuthService.AuthException e) {
//                        Notification.show("Wrong credentials.");
//                    }
//                }),
//                new RouterLink("Register", RegisterView.class)
//        );
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
                Notification.show("Wrong credentials.");
            }
        });


    }

}
