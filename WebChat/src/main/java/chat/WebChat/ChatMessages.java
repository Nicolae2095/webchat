package chat.WebChat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatMessages {
private static StringBuilder allMessages= new StringBuilder("_-=Wellcome to our chat=-_");

public static void addMessage(String name, String message){
	checkLength();
	DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	Date date = new Date();
	System.out.println("date==============="+dateFormat.format(date));

	allMessages.insert(0," <b>"+name+"</b> ,<small>at "+dateFormat.format(date)+"</small> : "+message+ "<br/>");
}


public static void addSpecialMessage(String message){
	checkLength();
	//allMessages=message+"\n"+allMessages;
}

public static StringBuilder getConversation(){
	return allMessages;
}

private static  void checkLength(){
	if(allMessages.length()>15000){
		
		allMessages.delete( (allMessages.length())/3 ,allMessages.length()-1);
	}
}

public static void clearCheatMessages() {
	allMessages= new StringBuilder("_-=Wellcome to our chat=-_");
}

}
