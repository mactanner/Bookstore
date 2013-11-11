package ch.bfh.amasoon.presenter;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserBean implements Serializable {

    public boolean isLoggedIn() {
        return true; //TODO SB
    }

}
