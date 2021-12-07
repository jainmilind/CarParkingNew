package com.example.application.views.home;

import com.example.application.data.entity.CarService;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

@PageTitle("Select Services")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class CarServiceSelectionView extends VerticalLayout implements AfterNavigationObserver {

    Grid<CarService> grid = new Grid<>();
    User customer;
    User worker;


    public CarServiceSelectionView() {

        worker = ComponentUtil.getData(UI.getCurrent(), User.class);
        customer = VaadinSession.getCurrent().getAttribute(User.class);

        setId("home-view");
        addClassName("car-service-selection-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        ArrayList<CarService> services = User.services.get(worker.getUsername());
        grid.setItems(services);

        Button done = new Button("Next");
        done.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        done.addClickListener(e -> {
            UI.getCurrent().navigate(BillSummaryView.class);
        });

        add(
                new H2(worker.retName()),
                new H4("Select Services - " + User.services.get(worker.getUsername()).size() + " services"),
                grid,
                done
        );

        setAlignItems(Alignment.CENTER);

    }

    private HorizontalLayout createCard(CarService carService) {

        carService.setCarNum(customer.getRegistrationNumber());

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc("images/car_service.png");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);
        image.setHeight("100%");


        Span name = new Span(carService.getServiceName());
        name.addClassName("name");

        Span price = new Span("Price - " + carService.getServiceCharge());
        price.addClassName("rating");

        VerticalLayout buttons = new VerticalLayout();

        Button selectService = new Button("Select");
        selectService.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        selectService.setWidth("25%");
        selectService.setVisible(
                User.servicesSelected.get(customer.getUsername()) == null
                        || !User.servicesSelected.get(customer.getUsername()).contains(carService));

        Button unselectService = new Button("Unselect");
        unselectService.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        unselectService.setWidth("25%");
        unselectService.setVisible(!selectService.isVisible());

        selectService.addClickListener(e -> {
            selectService.setVisible(false);
            unselectService.setVisible(true);
            worker.addCarService(carService);
            customer.addCarService(carService);
        });

        unselectService.addClickListener(e -> {
            unselectService.setVisible(false);
            selectService.setVisible(true);
            worker.removeCarService(carService);
            customer.removeCarService(carService);
        });

        buttons.add(selectService, unselectService);

        description.setPadding(true);
        name.setWidth("100%");
        card.addComponentAtIndex(0, name);
        price.setWidth("100%");
        card.addComponentAtIndex(1, price);
        buttons.setWidth("100%");
        card.addComponentAtIndex(2, buttons);

        return card;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }


    private void updateGrid() {

    }


}
