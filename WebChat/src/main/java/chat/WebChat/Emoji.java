package chat.WebChat;

import java.util.ArrayList;

import com.vaadin.ui.Button;

public class Emoji {

	private static final String SMILE = "(◕‿◕)";
	private static final String HAPPY = "٩(◕‿◕｡)۶";
	private static final String VINERI = "╰(▔∀▔)╯۶";
	private static final String LOVE = "(❤ω❤)";
	private static final String KISS = "(´ ε ` )♡";
	private static final String CRAY = "(ᗒᗣᗕ)՞";
	private static final String FK = "(凸ಠ益ಠ)凸";
	private static final String MIMIMI = "(｡•́︿•̀｡)";
	private static final String WAT = "ლ(ಠ_ಠ ლ)";
	private static final String ANGRY = "(・`ω´・)";
	private static final String SLEEP = "(∪｡∪)｡｡｡zzZ";
	private static final String CAT = "(^=◕ᴥ◕=^)";
	private static final String MMM_POPKI = "ʕ ᵔᴥᵔ ʔ";
	private static final String MUZ = "(￣▽￣)/♫•*¨*•.¸¸♪";
	private static final String TITI = "( • )( • )ԅ(≖‿≖ԅ)";
	private static final String CUT = "( ＾▽＾)っ✂╰⋃╯";

	public ArrayList<Button> allEmoji = new ArrayList();

	public ArrayList<Button> getEmoji() {
		initEmoji();
		return allEmoji;
	}

	private void initEmoji() {
		ArrayList<String> allEmojiString = new ArrayList();
		allEmojiString.add(SMILE);
		allEmojiString.add(HAPPY);
		allEmojiString.add(VINERI);

		allEmojiString.add(LOVE);
		allEmojiString.add(CUT);

		allEmojiString.add(CRAY);
		allEmojiString.add(FK);

		allEmojiString.add(MIMIMI);
		allEmojiString.add(WAT);

		allEmojiString.add(ANGRY);
		allEmojiString.add(SLEEP);
		allEmojiString.add(CAT);
		allEmojiString.add(MMM_POPKI);
		allEmojiString.add(MUZ);
		allEmojiString.add(TITI);
		allEmojiString.add(KISS);

		for (int i = 0; i < allEmojiString.size(); i++) {
			allEmoji.add(new Button(allEmojiString.get(i)));
		}

		allEmoji.get(0).setWidth("100px");
		allEmoji.get(11).setWidth("125px");
		allEmoji.get(10).setWidth("123px");
		allEmoji.get(9).setWidth("138px");
		allEmoji.get(7).setWidth("132px");
		allEmoji.get(5).setWidth("90px");
		allEmoji.get(4).setWidth("155px");
		allEmoji.get(3).setWidth("115px");
		allEmoji.get(1).setWidth("145px");

	}

}
