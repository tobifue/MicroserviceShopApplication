package ase.priceadjustment.scraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PriceScraper {

//	public double scrape(String item) {
//		// TODO actual implementation
//		return new Random().nextDouble() * 100;
//	}

	public static final String WEBSITE = "https://www.shoepping.at";

	public Map<String, String> items;

	public String scrape(String item) {
		items = new HashMap<>();

		scanItems();
		return findPriceForName(item);
	}

	private void scanItems() {
		Document document;

		try {
			document = Jsoup.connect(WEBSITE + "/c/smartphones").get();
		} catch (IOException ignored) {
			System.out.println("Couldn't connect to homepage!");
			return;
		}

		Elements elements = document.getElementsByClass("js-param-aware");
		// Elements elements = document.getElementsByAttribute("href");

		for (Element element : elements) {
			String link = element.attributes().get("href");

			// findName(link);

			findPrice(link);

			// printItems();
		}

		// findPriceForName("P40 lite");

	}

	private void findPrice(String link) {

		Document document;

		try {
			document = Jsoup.connect(WEBSITE + link).get();
		} catch (IOException ignored) {
			System.out.println("Cannot find price for " + link);
			return;
		}

		Elements elements = document.getElementsByClass("price").eq(0);
		Elements elements2 = document.getElementsByClass("text").eq(0);

		for (Element element : elements) {
			for (Element element2 : elements2) {
				items.put(element2.text(), element.text());
			}
			// System.out.println(element.text());
		}

	}

	private void findName(String link) {

		Document document;

		try {
			document = Jsoup.connect(WEBSITE + link).get();
		} catch (IOException ignored) {
			System.out.println("Cannot find name for " + link);
			return;
		}

		Elements elements = document.getElementsByClass("text").eq(0);

		for (Element element : elements) {
			// items.put(link, element.text());
			System.out.println(element.text());

		}
	}

	private String findPriceForName(String item) {

		for (String key : items.keySet()) {
			if (key.contains(item)) {
				return items.get(key).toString();
			}
		}
		return "Couldn't find price recommendation, try another name!";

//		if (items.get(name) != null) {
//			return items.get(name).toString();
//		} else {
//			return "Couldn't find price recommendation, try another name!";
//		}

	}

	private void printItems() {
		for (Map.Entry<String, String> entry : items.entrySet()) {
			String link = entry.getKey();
			String price = entry.getValue();

			System.out.println("Name: " + link);
			System.out.println("Price: " + price);

		}

	}

}
