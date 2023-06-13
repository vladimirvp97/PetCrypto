package PetCryptoViewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import PetCryptoViewer.Model.Pairs;

//
// ✅1. Сделать DAOofPairs под каждую сущность
// ✅2. Минимизировать выполнение логики в контроллере и делегировать ее в сервис. Доп: посмотреть статьи про паттерны проектирования микро-сервисов.
// ✅3. Переименовать Requester в BlockChainClient
// ✅4. Почитать нейминг конвенции и переименовать структуру папок, название методов(Camel Case).
// 5. Почитать про Ломбок
// 6. Почитать про правильное использование ObjectMapper. А именно правильно сократить его избыточное создание.
// 7. Поменять архитектуру в Requester. Что бы он тупо вызывался и возвращал что-то. Как это происходит в контроллере, пусть он возвращает сразу нам значение,
// а не после еще раз будем обращаться к БД.
// 8. Сделать архитектуру логичной и объективной. Посмотреть примеры проектов(в дискорде)
// 9. Сделать ручку с получением исторического значения из БД.
// 10. Разобраться почему нормально не сохраняются повторяющиеся значения в БД. Избежать блока try-catch.
// 11. После всех этих шагов задеплоить чтобы он подольше поработал.
@Controller
public class WebController {
    @Autowired
    private Service service;
    @GetMapping("/")
    public String showForm() {
        return "index";
    }
    @GetMapping ("/price")
    // /price?currency1=BTC&currency2=RUB
    @ResponseBody
    public String getPrice(@RequestParam String currency1, @RequestParam String currency2) throws Exception {
        Pairs requestedValue = service.getValueByNameOfCurrencies(currency1,currency2);
        if (requestedValue==null) {
            return "No have data about this currencies, please try the another request";
        }

        return "Value is " + requestedValue.getValue() +  " Time when data recieved: " + requestedValue.getConciseTime();

    }
}
