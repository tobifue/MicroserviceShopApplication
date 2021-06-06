package ase.priceadjustment.scraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PriceScraper {

	public static final String WEBSITE = "https://www.shoepping.at";

	public Map<String, String> items;

	public String scrape(Map<String, Object> recommend) {
		items = new HashMap<>();

		String item = (String) recommend.get("itemName");

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

		for (Element element : elements) {
			String link = element.attributes().get("href");

			findPrice(link);

		}

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

		}

	}

	private String findPriceForName(String item) {

		for (String key : items.keySet()) {
			if (key.contains(item)) {
				return String.format(items.get(key).toString());
			}
		}
		return String.format("Couldn't find price recommendation, try another name!");
	}

}
