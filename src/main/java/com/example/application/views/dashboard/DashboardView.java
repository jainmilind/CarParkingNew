package com.example.application.views.dashboard;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Dashboard")
public class DashboardView extends Div {
    public DashboardView() {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        try {
            Image avatar = new Image();
            if (user.getRole().equals(Role.USER)) {
                avatar.setSrc("images/man.png");
            } else if (user.getRole().equals(Role.ADMIN)) {
                avatar.setSrc("images/profile.png");
            } else if (user.getRole().equals(Role.WORKER)) {
                avatar.setSrc("images/worker.png");
            }
            add(avatar);

        } catch(Exception e) {
            Notification.show("Error");
        }

    }
}
