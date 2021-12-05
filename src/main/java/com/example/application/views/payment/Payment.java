package com.example.application.views.payment;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import org.vaadin.jonni.PaymentRequest;
import org.vaadin.jonni.PaymentRequest.PaymentResponse;

//import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * The main view contains a button and a template element.
 */
//@HtmlImport("styles/shared-styles.html")
@Route("pay")
public class Payment extends VerticalLayout {

	private Button button;

	public Payment() {
		PaymentRequest.queryIsSupported(isSupported -> {
			if (isSupported) {
				addPaymentRequestHandlerToButton();
			} else {
				button.addClickListener(click -> Notification
						.show("Payment collection is not supported on your browser!", 9000, Position.MIDDLE));
			}
		});
		button = new Button("Pay");
		add(new H1("CarParking.Com Payment Portal"), button);
//				new Paragraph("Please do not use real credit card information on this site!"),
//				new Paragraph(new Text("You can find some valid format "),
//						new Anchor("http://www.getcreditcardnumbers.com/", "test"), new Text(" "),
//						new Anchor("https://stripe.com/docs/testing#cards", "cards"), new Text(" "),
//						new Anchor("https://developer.paypal.com/developer/creditCardGenerator/", "online"),
//						new Text(".")));
	}

	private void addPaymentRequestHandlerToButton() {
		JsonArray supportedPaymentMethods = getSupportedMethods();

		JsonObject paymentDetails = getPaymentDetails();

		PaymentRequest paymentRequest = new PaymentRequest(supportedPaymentMethods, paymentDetails);
		paymentRequest.setPaymentResponseCallback((paymentResponse) -> {
			JsonObject eventData = paymentResponse.getEventData();
			Notification.show("Please wait a moment while we finish the payment via our payment gateway.", 1000,
					Position.MIDDLE);

			Command onPaymentGatewayRequestComplete = () -> {
				// Close the Payment Request native dialog
				paymentResponse.complete();
				String cardNumber = eventData.getObject("details").getString("cardNumber");
				String cardEnding = cardNumber.substring(cardNumber.length() - 4);
				Notification
						.show("Purchase complete! We have charged the total (₹1100) from your credit card ending in "
								+ cardEnding, 1000, Position.MIDDLE);
			};
			startPaymentGatewayQuery(paymentResponse, eventData, onPaymentGatewayRequestComplete);
		});
		paymentRequest.install(button);

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
		totalAmount.put("value", "1100");
		total.put("amount", totalAmount);
		paymentDetails.put("total", total);
		return paymentDetails;
	}
}
