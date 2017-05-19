package test.hwms.web;

import static spark.Spark.get;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;

public class HomeController {
	public static void init() {
		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("page", "home");
			return Main.TEMPLATE_ENGINE.render(new ModelAndView(model, "home.ftl"));
		});
		get("/hello", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("page", "hello");
			model.put("os_name", System.getProperty("os.name"));
			model.put("hostname", InetAddress.getLocalHost().getHostName());
			return Main.TEMPLATE_ENGINE.render(new ModelAndView(model, "hello.ftl"));	
		});
	}
}