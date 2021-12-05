package com.example.application.views.home;

import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.User;
import com.example.application.data.service.UserRepository;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@PageTitle("Select Worker")
@Route("worker-selection")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class WorkerSelectionView extends VerticalLayout implements AfterNavigationObserver{

    Grid<User> grid = new Grid<>();
    UserRepository workerRepository;
    ParkingSlot parkingSlot;
    //TODO: Remove the vaadin session attribute



    public WorkerSelectionView(UserRepository workerRepository) {

        this.workerRepository = workerRepository;
        parkingSlot = ComponentUtil.getData(UI.getCurrent(), ParkingSlot.class);

        setId("home-view");
        addClassName("home-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        ArrayList<User> workers = ParkingSlot.workers.get(parkingSlot.getName());
        grid.setItems(workers);

        add(
                new H2(parkingSlot.getName()),
                new H4("Select Worker - " + ParkingSlot.workers.get(parkingSlot.getName()).size() + " workers"),
                grid
        );
    }

    private HorizontalLayout createCard(User worker) {


        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc("images/worker.png");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);


        Span name = new Span(worker.retName());
        name.addClassName("name");

        Span rating = new Span("Rating - " + worker.getRating());
        rating.addClassName("rating");

        Button selectWorker = new Button("Select");
        selectWorker.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        selectWorker.setWidth("25%");
        selectWorker.addClickListener(e -> {
            //TODO: Implement navigation to payment and services view
            Notification.show("Worker " + worker.retName() + " selected");
            ComponentUtil.setData(UI.getCurrent(), User.class, worker);
            UI.getCurrent().navigate(CarServiceSelectionView.class);
        });

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        description.add(
                name,
                rating,
                new Label(String.format("%,d", worker.getUserRatings()) + " User Ratings")
        );
        description.setPadding(true);
        card.add(image, description, selectWorker);

        return card;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
    }



}
