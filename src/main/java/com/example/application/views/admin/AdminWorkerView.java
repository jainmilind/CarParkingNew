package com.example.application.views.admin;

import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.ParkingSlotRepository;
import com.example.application.data.service.UserRepository;
import com.example.application.views.home.CarServiceSelectionView;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;

import java.util.ArrayList;

@PageTitle("Workers")
public class AdminWorkerView extends VerticalLayout {

    Grid<User> grid = new Grid<>();
    UserRepository userRepository;
    ParkingSlotRepository parkingSlotRepository;
    ConfirmDialog dialog = new ConfirmDialog();


    public AdminWorkerView(UserRepository userRepository, ParkingSlotRepository parkingSlotRepository) {

        this.userRepository = userRepository;
        this.parkingSlotRepository = parkingSlotRepository;


        dialog.setHeader("Logout");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());

        dialog.setConfirmText("Yes");
        dialog.setConfirmButtonTheme("error primary");


        setId("home-view");
        addClassName("home-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        ArrayList<User> workers = userRepository.getAllByRole(Role.WORKER) ;
        grid.setItems(workers);

        Button addButton = new Button("Add Worker");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        addButton.addClickListener(e -> UI.getCurrent().navigate(NewWorkerForm.class));

        add(
                new H2("Edit Workers"),
                grid,
                addButton
        );
    }

    private HorizontalLayout createCard(User worker) {


        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        dialog.setText("Are you sure you want to remove" + worker.retName() + "?");

        dialog.addConfirmListener(event -> {
            ParkingSlot parkingSlot = parkingSlotRepository.getByName(worker.getLocation());
            parkingSlot.removeWorker(worker);
            userRepository.delete(worker);
            updateView();
            dialog.close();
        });

        Image image = new Image();
        image.setSrc("images/worker.png");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);


        Span name = new Span(worker.retName());
        name.addClassName("name");

        Span username = new Span("Username - " + worker.getUsername());
        username.addClassName("rating");

        Button deleteWorker = new Button("DELETE");
        deleteWorker.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteWorker.setWidth("25%");
        deleteWorker.addClickListener(e -> {
            dialog.open();
        });

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        description.add(
                name,
                username,
                new Label("Rating - " + worker.getRating()),
                new Label(String.format("%,d", worker.getUserRatings()) + " User Ratings"),
                new Label("Location - " + worker.getLocation()),
                new Label("Email - " + worker.getEmail()),
                new Label("Mobile Number - " + worker.getMobile()),
                new Label("Address - " + worker.getAddress())

        );
        description.setPadding(true);
        card.add(image, description, deleteWorker);

        return card;
    }

    private void updateView() {
        grid.setItems(userRepository.getAllByRole(Role.WORKER));
    }
}
