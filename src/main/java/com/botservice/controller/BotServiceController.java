/**
 * 
 */
package com.botservice.controller;


import java.io.File;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController; 

/**
 * @author KarthickAllu
 *
 */
@RestController
public class BotServiceController {
	private static final boolean TRACE_MODE = false;
	static String botName = "super";

	
	@RequestMapping("/bots")  
    public String bots(@RequestParam String requestInput){
		System.out.println(requestInput);
        return BotServiceMessage(requestInput);  
    }  
	
	public String BotServiceMessage(String requestInput){
		String response = null;
		String request = null;
		
		try {
			String resourcesPath = getResourcesPath();
			//System.out.println(resourcesPath);
			MagicBooleans.trace_mode = TRACE_MODE;
			Bot bot = new Bot("super", resourcesPath);
			Chat chatSession = new Chat(bot);
			bot.brain.nodeStats();
			String textLine = "";

			//while(true) {
				System.out.print("Human : ");
				textLine = requestInput; //IOUtils.readInputTextLine();
				if ((textLine == null) || (textLine.length() < 1))
					textLine = MagicStrings.null_input;
				if (textLine.equals("q")) {
					System.exit(0);
				} else if (textLine.equals("wq")) {
					bot.writeQuit();
					System.exit(0);
				} else {
					request = textLine;
					if (MagicBooleans.trace_mode)
						//System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
					response = chatSession.multisentenceRespond(request);
					while (response.contains("&lt;"))
						response = response.replace("&lt;", "<");
					while (response.contains("&gt;"))
						response = response.replace("&gt;", ">");
					//System.out.println("Robot : " + response);
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	private static String getResourcesPath() {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length() - 2);
		//System.out.println(path);
		String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
		return resourcesPath;
	}

}
