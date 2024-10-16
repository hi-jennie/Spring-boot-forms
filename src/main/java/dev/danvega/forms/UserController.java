package dev.danvega.forms;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/save")
    public String saveUser(@Valid User user, Model model) {
        userRepository.save(user);
        model.addAttribute("Message", "User has been saved successfully");
        return "index";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        // 从异常对象 ex 中获取失败验证的 User 对象，即用户提交的数据（即使验证失败，这些数据依然被保留）。
        User user = (User) ex.getBindingResult().getTarget();
        model.addAttribute("user", user);
        model.addAttribute("error", "Please fill out all required fields.");
        log.info("User Validation failed for: {}", user);
        return "index";
    }
}
