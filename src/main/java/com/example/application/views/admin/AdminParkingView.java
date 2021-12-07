package com.example.application.views.admin;

import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.service.ParkingSlotService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.Optional;

@PageTitle("Parking Slots")
public class AdminParkingView extends Div {

    private Grid<ParkingSlot> grid;

    private TextField name = new TextField("Location");
    private IntegerField price = new IntegerField("Price");
    private IntegerField totalSpots = new IntegerField("Total Spots");
    //      TODO: Change CRUD to display workers
    //    private TextField firstName = new TextField("First name");
    //    private TextField lastName = new TextField("Last name");
    //    private TextField username = new TextField("Username");
    //    private PasswordField password = new PasswordField("Password");
    //    private TextArea address = new TextArea("Address");
    //    private TextField email = new TextField("Email");
    //    private TextField mobile = new TextField("Mobile number");
    //    private TextField location = new TextField("Location");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private Binder<ParkingSlot> binder;

    private ParkingSlot parkingSlot = new ParkingSlot();

    private ParkingSlotService parkingSlotService;

    public AdminParkingView(@Autowired ParkingSlotService parkingSlotService) {
        setId("admin-view");
        this.parkingSlotService = parkingSlotService;
        // Configure Grid
        grid = new Grid<>(ParkingSlot.class);
        grid.setColumns("name", "price", "totalSpots");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.setDataProvider(new CrudServiceDataProvider<ParkingSlot, Void>(parkingSlotService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<ParkingSlot> personFromBackend = parkingSlotService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (personFromBackend.isPresent()) {
                    populateForm(personFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new Binder<>(ParkingSlot.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.parkingSlot == null) {
                    this.parkingSlot = new ParkingSlot();
                }
                binder.writeBean(this.parkingSlot);
                parkingSlotService.update(this.parkingSlot);
                clearForm();
                refreshGrid();
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the parking-slot details.");
            }
        });

        delete.addClickListener(e ->{
                parkingSlotService.delete(this.parkingSlot.getId());
                clearForm();
                refreshGrid();
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[]{name, price, totalSpots};
        for (AbstractField field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ParkingSlot value) {
        this.parkingSlot = value;
        binder.readBean(this.parkingSlot);
    }
}
