package com.example.application.views.worker;

import com.example.application.data.entity.CarService;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;

@PageTitle("Worker")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
public class WorkerView extends VerticalLayout {

    public WorkerView() {

        H2 title = new H2("My Services");
        add(title);
        User worker = VaadinSession.getCurrent().getAttribute(User.class);
        ArrayList<CarService> services = User.services.get(worker.getUsername());
        for (CarService service : services) {

            HorizontalLayout serviceLayout = new HorizontalLayout();
            Label serviceName = new Label(service.getServiceName());
            IntegerField dollarField = new IntegerField();
            dollarField.setValue(service.getServiceCharge());
            Div dollarPrefix = new Div();
            dollarPrefix.setText("₹");
            dollarField.setPrefixComponent(dollarPrefix);
            serviceName.addClassName("service-name");
            serviceLayout.add(serviceName, dollarField);
            serviceLayout.setSpacing(true);
            add(serviceLayout);
            dollarField.addValueChangeListener(e -> service.setServiceCharge(dollarField.getValue()));

        }
        setAlignItems(Alignment.CENTER);
        addClassName("my-services-layout");
    }
}
