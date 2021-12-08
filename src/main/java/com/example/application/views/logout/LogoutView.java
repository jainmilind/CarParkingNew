package com.example.application.views.logout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("logout")
@PageTitle("Logout")
public class LogoutView extends Composite<VerticalLayout> {
    public LogoutView() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Logout");
        dialog.setText("Are you sure you want to logout?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> UI.getCurrent().getPage().setLocation("home"));

        dialog.setConfirmText("Yes");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            UI.getCurrent().getPage().setLocation("login");
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
        });

        dialog.open();

    }

}