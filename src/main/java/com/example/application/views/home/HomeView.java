package com.example.application.views.home;

import com.example.application.data.entity.Booking;
import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.User;
import com.example.application.data.service.ParkingSlotRepository;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.internal.Pair;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Home")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class HomeView extends VerticalLayout implements AfterNavigationObserver {

    Grid<ParkingSlot> grid = new Grid<>();
    ParkingSlotRepository parkingSlotRepository;
    DateTimePicker startDateTimePicker;
    DateTimePicker endDateTimePicker;
    ComboBox<String> locations;
    User customer;


    public HomeView(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
        setId("home-view");
        addClassName("home-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        customer = VaadinSession.getCurrent().getAttribute(User.class);

        startDateTimePicker = new DateTimePicker("Start date and time");
        startDateTimePicker.setValue(LocalDateTime.of(2021, 12, 8, 20, 0, 0));

        endDateTimePicker = new DateTimePicker("End date and time");
        endDateTimePicker.setValue(LocalDateTime.of(2021, 12, 8, 22, 0, 0));

        startDateTimePicker.addValueChangeListener(e -> {
            endDateTimePicker.setMin(e.getValue().plusHours(1));
            updateGrid();
        });

        endDateTimePicker.addValueChangeListener(e -> updateGrid());


        locations = new ComboBox<>("Location");
        setLocations();
        locations.addValueChangeListener(e -> updateGrid());

        HorizontalLayout filters = new HorizontalLayout();
        filters.add(locations, startDateTimePicker, endDateTimePicker);

        add(filters, grid);
    }

    private HorizontalLayout createCard(ParkingSlot parkingSlot) {

        Pair<LocalDateTime, LocalDateTime> p = new Pair<>(startDateTimePicker.getValue(), endDateTimePicker.getValue());

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(parkingSlot.getImage());
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(parkingSlot.getName());
        name.addClassName("name");

        header.add(name);

        Button bookSlot = new Button("Book Now");
        bookSlot.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bookSlot.setWidth("25%");
        bookSlot.setEnabled(parkingSlot.canBook(p));
        bookSlot.addClickListener(e -> {
            User.bookings.get(customer.getUsername())
                    .add(0, new Booking(customer, startDateTimePicker.getValue(), endDateTimePicker.getValue(), parkingSlot));
            parkingSlot.addBooking(p, VaadinSession.getCurrent().getAttribute(User.class));
            ComponentUtil.setData(UI.getCurrent(), ParkingSlot.class, parkingSlot);
            UI.getCurrent().navigate(WorkerSelectionView.class);
            updateGrid();
        });

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");
        String availability = parkingSlot.nextAvailability(p);
        Span availabilityBadge;
        if (availability.equals("Available")) {
            availabilityBadge = new Span(createIcon(VaadinIcon.CHECK), new Span(availability));
            availabilityBadge.getElement().getThemeList().add("badge success");
        } else {
            availabilityBadge = new Span(createIcon(VaadinIcon.CLOCK), new Span(availability));
            availabilityBadge.getElement().getThemeList().add("badge contrast");
        }
        description.add(
                header,
                new Label("Price: Rs." + parkingSlot.getPrice() + "/hr"),
                new Label("Duration: " + getDuration(p) + "hrs"),
                new Label("Total Cost: Rs. " + parkingSlot.getPrice() * getDuration(p)),
                new Label("Total spots: " + parkingSlot.getTotalSpots()),
                availabilityBadge
        );
        description.setPadding(true);
        card.add(image, description, bookSlot);

        ComponentUtil.setData(UI.getCurrent(), Integer.class, getDuration(p));

        return card;
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }

    private int getDuration(Pair<LocalDateTime, LocalDateTime> p) {
        int duration = 0;
        while (p.getFirst().plusHours(duration).isBefore(p.getSecond())) {
            duration++;
        }
        return duration;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        grid.setItems(parkingSlots);
    }

    private void setLocations() {
        List<String> names = new ArrayList<>();
        names.add("All");
        parkingSlotRepository.findAll().forEach(slot -> {
            names.add(slot.getName());
        });
        locations.setItems(names);
    }


    private void updateGrid() {

        if (locations.isEmpty() || locations.getValue().equals("All")) {
            grid.setItems(parkingSlotRepository.findAll());
        } else {
            grid.setItems(parkingSlotRepository.getByName(locations.getValue()));
        }
    }


}