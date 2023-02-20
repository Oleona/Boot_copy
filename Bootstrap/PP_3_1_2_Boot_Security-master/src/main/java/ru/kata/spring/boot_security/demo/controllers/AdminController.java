package ru.kata.spring.boot_security.demo.controllers;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin")

public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(@AuthenticationPrincipal User currentUser, ModelMap model) {
        model.addAttribute("user", currentUser);
        model.addAttribute("listUsers", userService.listUsers());
        model.addAttribute("roles", roleService.roles());
        return "admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/new")
    public String createNewUser(@ModelAttribute("user") User user) {
        if (userService.userAlreadyExists(user.getUsername()) == null) {
            userService.add(user);
            return "redirect:/admin";
        } else {
            return "/error";
        }
    }

    @GetMapping("/{id}")
    public String edit(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("allRoles", roleService.roles());
        model.addAttribute("user", userService.getUserById(id));
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.update(user, id);
        return "redirect:/admin";
    }
}
