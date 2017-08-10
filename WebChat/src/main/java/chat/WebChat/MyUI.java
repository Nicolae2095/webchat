package chat.WebChat;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */

@Push
@Theme("mytheme")
public class MyUI extends UI implements Broadcaster.BroadcastListener {
	private static final String NAME_COOKIE = "userNameForChat";

	private VerticalLayout base = new VerticalLayout();

	private VerticalLayout mainLayout = new VerticalLayout();
	private RichTextArea webChat;
	private TextField userName;
	private TextField userMesssage;
	private boolean isCookieCreated = false;
	HorizontalLayout userImputComponentsBottom;
	PopupView emojiPopup;
	private Button emoji;
	private Emoji allEmoji = new Emoji();

	Cookie nameCookie;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		base.setMargin(false);
		base.setWidth("620px"); // 300
		base.setHeight("715px"); // 150

		base.addComponent(mainLayout);
		mainLayout.setSizeFull();

		mainLayout.setMargin(true);

		createTopComponents();
		createBottomComponents();

		setContent(mainLayout); // mainLayout
		
		Broadcaster.register(this);

		chekIfCookieAlredyIsCreated();

	}

	private void chekIfCookieAlredyIsCreated() {
		if (getCookieByName(NAME_COOKIE) != null) {
			userName.setValue(getCookieByName(NAME_COOKIE).getValue());
			userName.setReadOnly(true);
			isCookieCreated = true;
		} else {
			getPushConfiguration().setPushMode(PushMode.DISABLED);
		}
	}

	private void createTopComponents() {
		VerticalLayout chatLayout = new VerticalLayout();
		webChat = new RichTextArea();
	
		webChat.setSizeFull();
		
		webChat.setReadOnly(true);
	
		webChat.setValue(""+ChatMessages.getConversation());
		chatLayout.setSizeFull();
		chatLayout.setMargin(false);
		chatLayout.addComponent(webChat);
		
		
		webChat.setStyleName("my-theme1");
		
/*		Responsive.makeResponsive(webChat);
		webChat.setResponsive(true);
		
		Responsive.makeResponsive(mainLayout);
		mainLayout.setResponsive(true);*/
		
		
		mainLayout.addComponent(chatLayout);
		
		mainLayout.setExpandRatio(chatLayout, 0.8f);

	}

	private void createBottomComponents() {

		userImputComponentsBottom = new HorizontalLayout();
		userName = new TextField();
		userName.setCaption("Name:");
		userName.setWidth("100px");
		userName.setMaxLength(50);

		userMesssage = new TextField();
		userMesssage.setCaption("Message:");

		userMesssage.setMaxLength(350);

		Button sendMessegeButton = new Button("Send");
		
/*		sendMessegeButton.setStyleName("button-chat");
		sendMessegeButton.setResponsive(true);*/
		
		sendMessegeButton.addClickListener(e -> {
			if (!userMesssage.isEmpty() && !userName.isEmpty()) {
				if (areImputLengthValid()) {
					if (!isCookieCreated) {

						createCookie(userName.getValue());

						isCookieCreated = true;
						userName.setReadOnly(true);
						getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

					}
					// getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
					if (!isAdminComand()) {
						ChatMessages.addMessage(userName.getValue(), userMesssage.getValue());
					}

					Broadcaster.broadcast(ChatMessages.getConversation());

					System.out.println("size= " + ChatMessages.getConversation().length());
					// Page.getCurrent().reload();

					userMesssage.clear();
				}
			}

		});
		sendMessegeButton.setClickShortcut(KeyCode.ENTER);
		sendMessegeButton.setIcon(VaadinIcons.ENVELOPE_O);

		userImputComponentsBottom.addComponents(userName, userMesssage, sendMessegeButton);
		userImputComponentsBottom.setComponentAlignment(sendMessegeButton, Alignment.BOTTOM_RIGHT);

		createEmoji();
		mainLayout.addComponent(userImputComponentsBottom);

	}

	private void createEmoji() {
		emoji = new Button();
		emoji.setIcon(VaadinIcons.SMILEY_O);
		userImputComponentsBottom.addComponent(emoji);
		userImputComponentsBottom.setComponentAlignment(emoji, Alignment.BOTTOM_RIGHT);

		createEmojiPopup();
		userImputComponentsBottom.addComponent(emojiPopup);
		emoji.addClickListener(e -> {
			emojiPopup.setPopupVisible(true);
		});

	}

	private void createEmojiPopup() {
		VerticalLayout popupContentBase = new VerticalLayout();
		popupContentBase.setSpacing(false);

		ArrayList<Button> allEmojiButtons = allEmoji.getEmoji();

		for (Button button : allEmojiButtons) {
			button.addClickListener(e -> {
				userMesssage.setValue(userMesssage.getValue() + button.getCaption());
				emojiPopup.setPopupVisible(false);

			});

		}

		for (int i = 0; i < 8; i++) {
			HorizontalLayout rowEmoji = new HorizontalLayout();
			rowEmoji.setSpacing(false);
			rowEmoji.setMargin(false);

			for (int j = 0; j < 2; j++) {
				rowEmoji.addComponent(allEmojiButtons.get(i * 2 + j));
			}
			popupContentBase.addComponent(rowEmoji);

		}

		emojiPopup = new PopupView("", popupContentBase);

	}

	private boolean areImputLengthValid() {
		if (userMesssage.getValue().length() > 350) {
			userMesssage.setComponentError(new UserError("max length 350 chars"));
			return false;
		} else {
			userMesssage.setComponentError(null);
			if (userName.getValue().length() > 50) {
				userName.setComponentError(new UserError("max length 50 chars"));
				return false;
			} else {
				userName.setComponentError(null);
				userMesssage.setComponentError(null);
				return true;
			}
		}
	}

	private boolean isAdminComand() {
		if (userMesssage.getValue().equals("admin1-clear-chat")) {
			ChatMessages.clearCheatMessages();
			return true;
		}

	/*	if (userMesssage.getValue().startsWith("admin1-say-")) {

			String message = userMesssage.getValue();
			message = message.substring(11);
			ChatMessages.addSpecialMessage(message);

			return true;
		}*/

		return false;
	}

	public void createCookie(String userName) {
		nameCookie = new Cookie(NAME_COOKIE, userName);

		nameCookie.setMaxAge(10000); // 86000

		// Set the cookie path.
		nameCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

		// Save cookie
		
		

		VaadinService.getCurrentResponse().addCookie(nameCookie);
		// getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

	}

	public Cookie getCookieByName(String name) {
		// Fetch all cookies from the request
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		//System.out.println("cookies size = " + cookies.length);
		// Iterate to find cookie by its name
		for (Cookie cookie : cookies) {
			//System.out.println("cookie.getName() =" + cookie.getName());
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}

		return null;
	}

	@Override
	public void detach() {
		Broadcaster.unregister(this);
		super.detach();
	}

	@Override
	public void receiveBroadcast(final StringBuilder message) {
		// Must lock the session to execute logic safely
		access(new Runnable() {
			@Override
			public void run() {

				webChat.setValue(""+message);
			
				/*
				 * webChat.setSelection(webChat.getValue().length()-1, 0);
				 * 
				 * webChat.setCursorPosition(message.length()-1);
				 * webChat.setReadOnly(true);
				 * 
				 * userMesssage.setCursorPosition(0);
				 */

			}
		});
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false, heartbeatInterval = 10)
	public static class MyUIServlet extends VaadinServlet {
	}
}
