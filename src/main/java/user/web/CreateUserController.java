package user.web;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.dao.DataBase;
import user.domain.User;
import was.http.HttpRequest;
import was.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        List<Integer> list = new ArrayList<>();
        Multimap<Integer, Integer> multimap = HashMultimap.create();


        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        log.debug("user : {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}

