package com.example.application.views.home;

import com.example.application.data.entity.CarService;
import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.internal.Pair;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import org.vaadin.jonni.PaymentRequest;
import org.vaadin.jonni.PaymentRequest.PaymentResponse;


@PageTitle("Bill Summary")
public class BillSummaryView extends VerticalLayout {

    private ParkingSlot parkingSlot;
    private User customer;
    private int totalCost = 0;
    Button back = new Button("Back");
    Button next = new Button("Make Payment");
    Button homeButton = new Button("Home");

    public BillSummaryView() {
        parkingSlot = ComponentUtil.getData(UI.getCurrent(), ParkingSlot.class);
        customer = VaadinSession.getCurrent().getAttribute(User.class);

        setAlignItems(Alignment.CENTER);

        add(
                new H1("BILLING SUMMARY"),
                new H4("Parking - " + parkingSlot.getPrice() * ComponentUtil.getData(UI.getCurrent(), Integer.class))
        );

        int serviceCost = addServicesCost();

        totalCost = serviceCost + parkingSlot.getPrice() * ComponentUtil.getData(UI.getCurrent(), Integer.class);

        add(new H4("Total -  " + totalCost));

        HorizontalLayout buttons = new HorizontalLayout();

        buttons.setAlignItems(Alignment.CENTER);


        back.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttons.addComponentAtIndex(0, back);
        buttons.addComponentAtIndex(1, next);
		if (serviceCost > 0) {
	        back.addClickListener(e -> UI.getCurrent().navigate(CarServiceSelectionView.class));
		} else {
			back.addClickListener(e -> UI.getCurrent().navigate(WorkerSelectionView.class));
		}

        homeButton.setVisible(false);
        homeButton.addThemeVariants();
        homeButton.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));

		next.addClickListener(e -> User.servicesSelected.get(customer.getUsername()).clear());

        add(buttons, homeButton);

        PaymentRequest.queryIsSupported(isSupported -> {
			if (isSupported) {
				addPaymentRequestHandlerToButton();
			} else {
				next.addClickListener(click -> Notification
						.show("Payment collection is not supported on your browser!", 9000, Position.MIDDLE));
			}
		});

    }

    private int addServicesCost() {
		int serviceCost = 0;
        for (CarService carService : User.servicesSelected.get(customer.getUsername())) {
            add(new H4(carService.getServiceName() + " - " + carService.getServiceCharge()));
            serviceCost += carService.getServiceCharge();
        }
		return serviceCost;
    }

    private void addPaymentRequestHandlerToButton() {
		JsonArray supportedPaymentMethods = getSupportedMethods();

		JsonObject paymentDetails = getPaymentDetails();

		PaymentRequest paymentRequest = new PaymentRequest(supportedPaymentMethods, paymentDetails);
		paymentRequest.setPaymentResponseCallback((paymentResponse) -> {
			JsonObject eventData = paymentResponse.getEventData();
			Notification.show("Please wait a moment while we finish the payment via our payment gateway.", 2000,
					Position.MIDDLE);

			Command onPaymentGatewayRequestComplete = () -> {
				// Close the Payment Request native dialog
				paymentResponse.complete();
				String cardNumber = eventData.getObject("details").getString("cardNumber");
				String cardEnding = cardNumber.substring(cardNumber.length() - 4);
				Notification
						.show("Purchase complete! \nWe have charged the total (Rs." + totalCost + ") from your credit card ending in "
								+ cardEnding, 2000, Position.MIDDLE);
				back.setVisible(false);
				next.setVisible(false);
                homeButton.setVisible(true);
			};
			startPaymentGatewayQuery(paymentResponse, eventData, onPaymentGatewayRequestComplete);
		});
		paymentRequest.install(next);

	}

	/**
	 * simulates asynchronous communication with a payment gateway
	 *
	 * @param paymentResponse
	 * @param eventData
	 * @param onPaymentGatewayRequestComplete
	 */
	private void startPaymentGatewayQuery(PaymentResponse paymentResponse, JsonObject eventData,
                                          Command onPaymentGatewayRequestComplete) {
		UI ui = UI.getCurrent();
		Thread paymentGatewayThread = new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ui.access(onPaymentGatewayRequestComplete);

		});
		paymentGatewayThread.start();
	}

	/**
	 * @return <code>[{supportedMethods: 'basic-card'}]</code>
	 */
	private JsonArray getSupportedMethods() {
		JreJsonFactory jsonFactory = new JreJsonFactory();
		JsonArray supportedPaymentMethods = jsonFactory.createArray();
		JsonObject basicCard = jsonFactory.createObject();
		basicCard.put("supportedMethods", "basic-card");
		supportedPaymentMethods.set(0, basicCard);
		return supportedPaymentMethods;
	}

	/**
	 * @return <code>total: { label: 'Cart (10 items)', amount:{ currency: 'EUR', value:
	 *         1337 } }</code>
	 */
	private JsonObject getPaymentDetails() {
		JreJsonFactory jsonFactory = new JreJsonFactory();
		JsonObject paymentDetails = jsonFactory.createObject();

		JsonObject total = jsonFactory.createObject();
		total.put("label", "Cart (10 items)");
		JsonObject totalAmount = jsonFactory.createObject();
		totalAmount.put("currency", "INR");
		totalAmount.put("value",String.valueOf(totalCost));
		total.put("amount", totalAmount);
		paymentDetails.put("total", total);
		return paymentDetails;
	}
}
