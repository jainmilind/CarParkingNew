package com.example.application.views.dashboard;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Dashboard")
public class DashboardView extends Div {
    public DashboardView() {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        TextField role = new TextField("Role");
        try {
            Image avatar = new Image();
            if (user.getRole().equals(Role.USER)) {
                avatar.setSrc("images/man.png");
                role.setValue("User");
            } else if (user.getRole().equals(Role.ADMIN)) {
                avatar.setSrc("images/profile.png");
                role.setValue("Admin");
            } else if (user.getRole().equals(Role.WORKER)) {
                avatar.setSrc("images/worker.png");
                role.setValue("Worker");
            }

            H2 title = new H2(user.getFirstName() + " " + user.getLastName());
            title.getStyle().set("align-self", "flex-start");

            TextField username = new TextField("Username");
            username.setValue(user.getUsername());
            username.setReadOnly(true);

            role.setReadOnly(true);

            TextArea address = new TextArea("Residential address");
            address.setValue(user.getAddress());
            address.setReadOnly(true);

            EmailField email = new EmailField("Email ID");
            email.setValue(user.getEmail());
            email.setReadOnly(true);

            TextField mobile = new TextField("Mobile number");
            mobile.setValue(user.getMobile());
            mobile.setReadOnly(true);

            TextField registrationNumber = new TextField("Car registration number");
            registrationNumber.setValue(user.getRegistrationNumber());
            registrationNumber.setReadOnly(true);

            // Create a FormLayout with all our components. The FormLayout doesn't have any
            // logic (validation, etc.), but it allows us to configure Responsiveness from
            // Java code and its defaults looks nicer than just using a VerticalLayout.
            FormLayout formLayout = new FormLayout(avatar, title, username,
                    address, email, mobile, registrationNumber);

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

            // Add the form to the page
            add(formLayout);
        } catch(Exception e) {
            Notification.show("Error");
        }

    }
}
