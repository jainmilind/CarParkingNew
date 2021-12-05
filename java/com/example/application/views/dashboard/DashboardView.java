package com.example.application.views.dashboard;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Dashboard")
public class DashboardView extends Div {
    public DashboardView() {
        //TODO: Fix null pointer exception for Worker
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        System.out.println(user.getRole());
        TextField role = new TextField("Role");
//        try {
        VerticalLayout imageHolder = new VerticalLayout();
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
        avatar.setMaxHeight("250px");
        imageHolder.add(avatar);

        H2 title = new H2(user.getFirstName() + " " + user.getLastName());
        imageHolder.add(title);
        imageHolder.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

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

        FormLayout formLayout;
        if (user.getRole() == Role.USER)
            formLayout = new FormLayout(imageHolder, username, address, email, mobile, registrationNumber);
        else
            formLayout = new FormLayout(imageHolder, username, address, email, mobile);

        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formLayout.setColspan(imageHolder, 2);

        add(formLayout);


    }
}