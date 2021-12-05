package com.example.application.views.home;

import com.example.application.data.entity.CarService;
import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route("bill-summary")
@PageTitle("Bill Summary")
public class BillSummaryView extends VerticalLayout {

    private ParkingSlot parkingSlot;
    private User customer;
    private int totalCost = 0;

    public BillSummaryView(){
        parkingSlot = ComponentUtil.getData(UI.getCurrent(), ParkingSlot.class);
        customer = VaadinSession.getCurrent().getAttribute(User.class);

        setAlignItems(Alignment.CENTER);

        add(
                new H1("BILLING SUMMARY"),
                new H4("Parking - " + parkingSlot.getPrice())
        );

        addServicesCost();

        totalCost += parkingSlot.getPrice();

        add(new H4("Total -  " + totalCost));

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.setAlignItems(Alignment.CENTER);

        Button back = new Button("Back");
        Button next = new Button("Make Payment");

        back.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttons.addComponentAtIndex(0, back);
        buttons.addComponentAtIndex(1, next);

        back.addClickListener(e -> UI.getCurrent().navigate(CarServiceSelectionView.class));
        next.addClickListener(e -> Notification.show("Payment Made!"));

        add(buttons);

    }

    private void addServicesCost() {
        for (CarService carService : User.servicesSelected.get(customer.getUsername())) {
            add(new H4(carService.getServiceName() + " - " + carService.getServiceCharge()));
            totalCost += carService.getServiceCharge();
        }
    }
}
