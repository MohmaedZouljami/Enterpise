package com.example.enterprise;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    private final JavaMailSender mailSender;
    private List<Event> events = new ArrayList<>();
    private Long nextId = 1L;

    public EventController(JavaMailSender mailSender) {
        this.mailSender = mailSender;

        Location locatie1 = new Location(1L, "Campus Kaai", "Kaaiplein 1, Brussel", 200);
        Location locatie2 = new Location(2L, "Gemeenschapshuis", "Anderlechtsesteenweg 5", 100);

        events.add(new Event(nextId++, LocalDateTime.now(), "Buurtfeest", "Een gezellig buurtfeest", "NGO Anderlecht", "contact@ngo.be", locatie1));
        events.add(new Event(nextId++, LocalDateTime.now(), "Workshops", "Gratis workshops voor iedereen", "Partnerorganisatie", "info@partner.be", locatie2));
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Event> laatste10 = events.stream()
                .skip(Math.max(0, events.size() - 10))
                .toList();
        model.addAttribute("events", laatste10);
        return "index";
    }

    @GetMapping("/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("locaties", getLocaties());
        return "new";
    }

    @PostMapping("/new")
    public String saveEvent(@ModelAttribute Event event, Model model) {
        // Validatie: controleer of alle velden ingevuld zijn
        if (event.getTitel() == null || event.getTitel().isEmpty() ||
                event.getOmschrijving() == null || event.getOmschrijving().isEmpty() ||
                event.getOrganisatie() == null || event.getOrganisatie().isEmpty() ||
                event.getMailContactpersoon() == null || event.getMailContactpersoon().isEmpty() ||
                event.getTijdstip() == null) {

            model.addAttribute("fout", "Alle velden zijn verplicht!");
            model.addAttribute("locaties", getLocaties());
            return "new";
        }

        // Validatie: controleer of e-mailadres geldig is
        if (!event.getMailContactpersoon().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            model.addAttribute("fout", "Ongeldig e-mailadres!");
            model.addAttribute("locaties", getLocaties());
            return "new";
        }

        event.setId(nextId++);
        events.add(event);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Event event = events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("event", event);
        return "details";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String verstuurContact(@RequestParam String naam,
                                  @RequestParam String email,
                                  @RequestParam String bericht) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ngoanderlecht.be");
        message.setTo("admin@ngoanderlecht.be");
        message.setSubject("Nieuw bericht van " + naam);
        message.setText("Van: " + naam + "\nEmail: " + email + "\n\nBericht:\n" + bericht);

        mailSender.send(message);

        return "redirect:/";
    }

    private List<Location> getLocaties() {
        List<Location> locaties = new ArrayList<>();
        locaties.add(new Location(1L, "Campus Kaai", "Kaaiplein 1, Brussel", 200));
        locaties.add(new Location(2L, "Gemeenschapshuis", "Anderlechtsesteenweg 5", 100));
        return locaties;
    }
}