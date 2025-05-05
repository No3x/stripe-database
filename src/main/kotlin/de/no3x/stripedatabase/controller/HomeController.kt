package de.no3x.stripedatabase.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @PreAuthorize("hasPermission(null, 't1')")
    @GetMapping("/premium")
    fun productAccess(): String = "Welcome to the premium zone!"

    @GetMapping("/home")
    fun home(): String = "home"
}