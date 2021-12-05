package com.example.application.views.worker;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Worker")
public class WorkerView extends Div {
    public WorkerView() {
        //TODO: Fix null pointer exception for Worker
        H2 title = new H2("My Services");
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        int[] prices = user.getPrices();
        String[] services = user.getServices();

        FormLayout formLayout = new FormLayout();
        formLayout.add(title);
        for (int i = 0; i < prices.length; ++i) {
            IntegerField dollarField = new IntegerField();
            dollarField.setValue(prices[i]);
            Div dollarPrefix = new Div();
            dollarPrefix.setText("₹");
            dollarField.setPrefixComponent(dollarPrefix);
            int finalI = i;
            dollarField.addValueChangeListener(event -> {
                prices[finalI] = event.getValue();
                user.setPrices(finalI, event.getValue());
                Notification.show("Current value: " + user.getPrices()[finalI]);
            });
            formLayout.addFormItem(dollarField, services[i]);
        }
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");
        formLayout.setColspan(title, 2);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        add(formLayout);
    }
}