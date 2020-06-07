package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import utils.LoginUtil;

import java.util.Collection;

public class UserListController extends AbstractController {

    public UserListController() {
        super("/user/list");
    }

    @Override
    protected void get(final HttpRequest request, final HttpResponse httpResponse) {
        if (!LoginUtil.isLoggedIn(request)) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();

        httpResponse.forward("user/profile");
        httpResponse.addModel("users", users);
    }
}
