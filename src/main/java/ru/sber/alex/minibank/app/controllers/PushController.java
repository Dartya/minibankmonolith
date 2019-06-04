package ru.sber.alex.minibank.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sber.alex.minibank.businesslogic.dto.TransactionDto;
import ru.sber.alex.minibank.businesslogic.logic.CommonLogic;
import ru.sber.alex.minibank.businesslogic.services.PushService;

/**
 * Контроллер страницы пополнения счета
 */
@Controller
public class PushController {

    @Autowired
    private PushService pushService;

    /**
     * Возвращает страницу с формой пополнения счета.
     * @param model контейнер параметров, принимает при вызове страницы. В нее помещается пустой объект TransactionDto для дальнейшего его заполнения пользователем.
     * @return
     */
    @GetMapping("/push")
    public String pushMoneyGet(Model model){
        model.addAttribute("transaction", new TransactionDto());
        return "pushmoney";
    }

    /**
     * Инициирует цепочку логики добавления средств на счет.
     * @param transaction заполненный из формы TransactionDto, ранее переданный в нее методом pushMoneyGet().
     * @param model контейнер параметров. Заполняется методом, в зависимости от кода успешности операции будут выведены данные на соответствующих страницах.
     * @return либо страницу успешной операции, либо страницу ошибки.
     */
    @PostMapping("/push")
    public String pushMoneyPost(@ModelAttribute TransactionDto transaction, Model model){
        transaction.setLogin(CommonLogic.getCurrentLogin());
        int result = pushService.makePush(transaction);
        if (result != -1){
            model.addAttribute("message", "Счет пополнен!");
            return "successtransaction";
        }else {
            model.addAttribute("userError", true);
            model.addAttribute("errorMessage", "Ошибка при внесении средств.");
            return "error";
        }
    }
}
