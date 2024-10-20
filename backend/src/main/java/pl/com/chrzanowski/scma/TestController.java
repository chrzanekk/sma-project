package pl.com.chrzanowski.scma;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Test controller: Backend: PUBLIC CONTENT.";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "Test controller: Backend: User Content.";
    }

    @GetMapping("/mod")
    public String moderatorAccess() {
        return "Test controller: Backend: Moderator Board.";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Test controller: Backend: Admin Board.";
    }
}

