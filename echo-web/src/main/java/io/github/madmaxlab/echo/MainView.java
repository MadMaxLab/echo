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

    public MainView(@Autowired EchoWebSocketClient client) {
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
        // This is hardcode needs only for demo purpose.
        String yourName = "You";
        String opponentName = "potapov.m";
        for (int i = 0; i < 20; i++) {
            messages.add( new Message("Привет! Как дела?", "/icons/avatar1.png", opponentName, false));
        }
        chat.setMessages(messages);
        chat.addNewMessage( new Message("Привет! Как дела?", "/icons/avatar1.png", opponentName, false));
        chat.addNewMessage( new Message("Привет! Нормально, как сам?", "/icons/avatar2.png", yourName, true));
        chat.addNewMessage( new Message("Да вот, латынь учу. Смотри что получилось пока освоить.", "/icons/avatar1.png", opponentName, false));
        chat.addNewMessage( new Message("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "/icons/avatar1.png", opponentName, false));
        chat.addNewMessage( new Message("Круто, молодец!", "/icons/avatar2.png", yourName, true));
        chat.addNewMessage( new Message("А я новую фишку для нашего чата сделал! Теперь у нас отображаюстся аватарки пользователей!", "/icons/avatar2.png", yourName, true));
        chat.addNewMessage( new Message("Угу, вижу. А свое изображение выбрать можно?", "/icons/avatar1.png", opponentName, false));
        chat.addNewMessage( new Message("Конечно!", "/icons/avatar2.png", yourName, true));
        chat.addNewMessage( new Message("Прекрасно! Спасибо!", "/icons/avatar1.png", opponentName, false));
        chat.scrollToBottom();
        chat.addChatNewMessageListener(event -> {
            chat.addNewMessage(new Message(event.getMessage(), "/icons/avatar2.png", yourName, true));
            chat.clearInput();
            chat.scrollToBottom();
        });
        chat.addLazyLoadTriggerEvent(event -> {

        });
        hLayout.add(chat);
        ListBox<String> contactListBox = new ListBox<>();
        contactListBox.setItems("Потапов Михаил", "Иванов Иван", "Денисов Петр", "Петров Мансур");
        contactListBox.setValue("Потапов Михаил");
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
