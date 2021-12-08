package com.example.application.views.home;

import com.example.application.data.entity.Booking;
import com.example.application.data.entity.CarService;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import org.hibernate.cache.internal.NoCachingTransactionSynchronizationImpl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@PageTitle("My Bookings")
public class BookingHistoryView extends VerticalLayout {

    private User customer;
    private Grid<Booking> grid = new Grid<>();

    public BookingHistoryView() {

        customer = VaadinSession.getCurrent().getAttribute(User.class);

        setId("home-view");
        addClassName("home-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);

        ArrayList<Booking> bookings = User.bookings.get(customer.getUsername());
        if (bookings != null)
            grid.setItems(bookings);
        else
            grid.setItems(new ArrayList<>());

        add(
                new H2("My History"),
                grid
        );
    }

    private HorizontalLayout createCard(Booking booking) {

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Rate the worker");
        RadioButtonGroup<Integer> rating = new RadioButtonGroup<>();
        rating.setLabel("Rating");
        rating.setItems(1, 2, 3, 4, 5);
        HorizontalLayout buttons = new HorizontalLayout();
        Button saveButton = new Button("Rate");
        Button cancelButton = new Button("Cancel");
        buttons.add(saveButton, cancelButton);

        dialog.add(new VerticalLayout(rating, buttons));

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");


        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);


        Span locationName = new Span(booking.getParkingSlot().getName());
        locationName.addClassName("name");

        Span startTime = new Span("Start - " + booking.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")));
        startTime.addClassName("rating");


        Span endTime = new Span("End - " + booking.getFinishTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")));
        endTime.addClassName("rating");

        Span workerName = new Span();
        workerName.addClassName("rating");

        if(booking.getWorker() != null)
            workerName = new Span("Worker - " + booking.getWorker().retName());


        Label duration = new Label("Duration - " + booking.getDuration());

        description.add(locationName, startTime, endTime, duration, workerName);

        int totalCost = booking.getParkingSlot().getPrice() * booking.getDuration();

        description.add(new Label("Parking fee - " + booking.getParkingSlot().getPrice() * booking.getDuration()));

        for (CarService carService : booking.getServices()) {
            description.add(new Label(carService.getServiceName() + " - " + carService.getServiceCharge()));
            totalCost += carService.getServiceCharge();
        }

        description.add(new Label("Total - " + totalCost));

        VerticalLayout buttonLayout = new VerticalLayout();
        Button done = new Button("Rate Worker");
        done.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        done.setWidth("50%");

        if(booking.getWorker() == null){
            done.setText("Worker not Selected");
        }

        done.setEnabled(booking.getWorker() != null);

//        done.setWidthFull();

        Span rated = new Span("Rated!");
        rated.addClassName("name");
        buttonLayout.add(done, rated);
        rated.setVisible(false);


        done.addClickListener(e -> dialog.open());

        saveButton.addClickListener(e -> {
            booking.getWorker().updateRatings(rating.getValue());
            rated.setVisible(true);
            done.setEnabled(false);
            dialog.close();
        });

        cancelButton.addClickListener(e -> dialog.close());

        description.setPadding(true);
        card.add(description, buttonLayout);

        return card;
    }

}
