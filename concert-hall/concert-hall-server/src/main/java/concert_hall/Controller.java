package concert_hall;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    AsyncService service;

    public Controller(AsyncService service) {
        this.service = service;
    }

}
