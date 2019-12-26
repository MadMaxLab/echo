package io.github.madmaxlab.echo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route("registration")
public class RegistrationView  extends FormLayout {
    public RegistrationView() {
        TextField firstName = new TextField("Имя", "Введите имя");
        TextField lastName = new TextField("Фамилия", "Введите фамилию");
        TextField email = new TextField("Email", "Введите Email");
        TextField login = new TextField("Логин", "Введите Логин");
        PasswordField password = new PasswordField("Пароль");
        add(firstName, lastName, email, login, password);


        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button save = new Button("Сохранить", event -> {
            buttonLayout.getUI().ifPresent(ui -> {
                ui.navigate("");
            });
        });
        Button cancel = new Button("Отменить");
        buttonLayout.add(save, cancel);
        add(buttonLayout, 3);

    }
}
