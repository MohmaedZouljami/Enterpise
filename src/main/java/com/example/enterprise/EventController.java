package com.example.enterprise;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    private List<Event> events = new ArrayList<>();
    private Long nextId = 1L;

    public EventController() {
        Location locatie1 = new Location(1L, "Campus Kaai", "Kaaiplein 1, Brussel", 200);
        Location locatie2 = new Location(2L, "Gemeenschapshuis", "Anderlechtsesteenweg 5", 100);

        events.add(new Event(nextId++, LocalDateTime.now(), "Buurtfeest", "Een gezellig buurtfeest", "NGO Anderlecht", "contact@ngo.be", locatie1));
        events.add(new Event(nextId++, LocalDateTime.now(), "Workshops", "Gratis workshops voor iedereen", "Partnerorganisatie", "info@partner.be", locatie2));
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("events", events);
        return "index";
    }

    @GetMapping("/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("locaties", getLocaties());
        return "new";
    }

    @PostMapping("/new")
    public String saveEvent(@ModelAttribute Event event) {
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

    private List<Location> getLocaties() {
        List<Location> locaties = new ArrayList<>();
        locaties.add(new Location(1L, "Campus Kaai", "Kaaiplein 1, Brussel", 200));
        locaties.add(new Location(2L, "Gemeenschapshuis", "Anderlechtsesteenweg 5", 100));
        return locaties;
    }
}
