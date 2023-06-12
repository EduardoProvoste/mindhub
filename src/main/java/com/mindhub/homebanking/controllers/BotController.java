package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.BotService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class BotController {

    private final ChatgptService chatgptService;
    @Autowired
    private BotService botService;

    @GetMapping("/message")
    public String getMessage() {
        String message = "Tip sobre inversiones en menos de 30 palabras";
        String responseMessage = chatgptService.sendMessage(message);
        return responseMessage;
    }

    @GetMapping("/uf")
    public double getValueUf() {
        return botService.getValueUf();
    }
    @GetMapping("/dollar")
    public double getValueDollar() {
        return botService.getValueDollar();
    }

}
