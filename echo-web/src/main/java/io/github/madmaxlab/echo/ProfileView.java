package io.github.madmaxlab.echo;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;

@Route("profile")
public class ProfileView  extends FormLayout {
    public ProfileView() {
        add(new Text("Profile is under construction"));
    }
}
