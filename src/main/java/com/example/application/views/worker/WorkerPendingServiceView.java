package com.example.application.views.worker;

import com.example.application.data.entity.CarService;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

@PageTitle("Pending")
public class WorkerPendingServiceView extends VerticalLayout {

    private User worker;
    private Grid<CarService> grid = new Grid<>();

    public WorkerPendingServiceView() {

        worker = VaadinSession.getCurrent().getAttribute(User.class);

        setId("home-view");
        addClassName("home-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        ArrayList<CarService> services = User.servicesSelected.get(worker.getUsername());
        grid.setItems(services);

        add(
                new H2("Services To Do"),
                grid
        );
    }

    private HorizontalLayout createCard(CarService carService) {


        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");


        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);


        Span serviceName = new Span(carService.getServiceName());
        serviceName.addClassName("name");

        Span carNum = new Span(carService.getCarNum());
        carNum.addClassName("rating");


        description.add(serviceName, carNum);

        Button done = new Button("Completed");
        done.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        done.setWidth("25%");

        done.addClickListener(e -> {
           User.servicesSelected.get(worker.getUsername()).remove(carService);
           grid.setItems(User.servicesSelected.get(worker.getUsername()));
        });

        description.setPadding(true);
        card.add(description, done);

        return card;
    }

}
