package com.example.application.views.worker;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.util.Map;

@PageTitle("Worker")
public class WorkerView extends Div {
    public WorkerView() {
        //TODO: Fix null pointer exception for Worker
        H2 title = new H2("My Services");
        Map<String, Integer> services = VaadinSession.getCurrent().getAttribute(User.class).services;
        for (Map.Entry<String, Integer> entry : services.entrySet()) {

            IntegerField dollarField = new IntegerField();
            dollarField.setValue(entry.getValue());
            Div dollarPrefix = new Div();
            dollarPrefix.setText("₹");
            dollarField.setPrefixComponent(dollarPrefix);
            add(dollarField);

        }
    }
}
