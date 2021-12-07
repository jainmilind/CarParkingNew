package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.views.admin.AdminParkingView;
import com.example.application.views.admin.AdminWorkerView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.home.HomeView;
import com.example.application.views.logout.LogoutView;
import com.example.application.views.main.MainView;
import com.example.application.views.worker.WorkerPendingServiceView;
import com.example.application.views.worker.WorkerView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.checkerframework.checker.units.qual.A;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {



    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {

    }

    public class AuthException extends Exception {

    }

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final ParkingSlotRepository parkingSlotRepository;

    public AuthService(UserRepository userRepository, MailSender mailSender, ParkingSlotRepository parkingSlotRepository) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            if(user == null) Notification.show("User doesnt exist");
            else if(!user.checkPassword(password)) Notification.show("Wrong password");
            else Notification.show("User not active");
            throw new AuthException();
        }
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        var routes = new ArrayList<AuthorizedRoute>();

        if (role.equals(Role.USER)) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("dashboard", "Profile", DashboardView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("dashboard", "Profile", DashboardView.class));
            routes.add(new AuthorizedRoute("all-workers-view", "Worker View", AdminWorkerView.class));
            routes.add(new AuthorizedRoute("all-parking-view", "Parking Slots", AdminParkingView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        } else if (role.equals(Role.WORKER)) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("dashboard", "Profile", DashboardView.class));
            routes.add(new AuthorizedRoute("worker", "Manage Prices", WorkerView.class));
            routes.add(new AuthorizedRoute("to-do", "Pending Services", WorkerPendingServiceView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        }

        return routes;
    }

    public void register(String firstName, String lastName, String username, String password,
                         String address, String mobile, String email, String registrationNumber, Role role) {
        User user = new User(firstName, lastName, username, password, role,
                address, mobile, email, registrationNumber);

        if(role == Role.USER) {
            String text = "http://localhost:8080/activate?code=" + user.getActivationCode();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@example.com");
            message.setSubject("Confirmation email");
            message.setText(text);
            message.setTo(email);
            mailSender.send(message);
        }
        else if(role == Role.WORKER){
            user.setActive(true);
            parkingSlotRepository.getByName(user.getLocation()).addWorker(user);
        }

        userRepository.save(user);
    }

    public void activate(String activationCode) throws AuthException {
        User user = userRepository.getByActivationCode(activationCode);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        } else {
            throw new AuthException();
        }
    }

    public List<String> getLocations() {
        ArrayList<String> names = new ArrayList<>();
        parkingSlotRepository.findAll().forEach(slot -> names.add(slot.getName()));
        return names;
    }

}
