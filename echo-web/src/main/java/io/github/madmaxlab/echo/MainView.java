package io.github.madmaxlab.echo;

import com.vaadin.componentfactory.Chat;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route
@PWA(name = "Echo web chat", shortName = "Echo Web")
@Slf4j
@CssImport("/styles/shared-styles.css")
public class MainView extends VerticalLayout {

    public MainView(@Autowired MessageBean bean) {
        MenuBar menu = new MenuBar();
        menu.addThemeVariants(MenuBarVariant.MATERIAL_OUTLINED);
        MenuItem mainMenu = menu.addItem(new Icon(VaadinIcon.MENU));
        MenuItem profile = menu.addItem(new Icon(VaadinIcon.USER));
        MenuItem notifications = menu.addItem(new Icon(VaadinIcon.BELL));
        profile.addClickListener(event -> menu.getUI().ifPresent(ui -> ui.navigate("registration")));

        add(menu);
        HorizontalLayout hLayout = new HorizontalLayout();

        Chat chat = new Chat();
        chat.setId("chat");
        chat.setLoading(false);
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            messages.add( new Message("Hello World!", "", "DEREZA", false));
        }

        chat.setMessages(messages);
        chat.scrollToBottom();
        chat.addChatNewMessageListener(event -> {
            chat.addNewMessage(new Message(event.getMessage(), "", "Mad Max", true));
            chat.clearInput();
        });
        chat.addLazyLoadTriggerEvent(event -> {

        });
        hLayout.add(chat);
        ListBox<String> contactListBox = new ListBox<>();
        contactListBox.setItems("DEREZA", "YAGOZA", "Vasyliy Petrovich", "Petr Abramovich");
        hLayout.add(contactListBox);
        hLayout.setWidthFull();
        add(hLayout);

        LoginOverlay loginOverlay = new LoginOverlay(LoginI18n.createDefault());
        loginOverlay.setTitle("Echo Web Chat");
        loginOverlay.setDescription("Пожалуй, лучший чат в мире!");
        loginOverlay.addLoginListener(event -> loginOverlay.close());
        loginOverlay.setForgotPasswordButtonVisible(false);
        loginOverlay.setOpened(true);
    }

}
