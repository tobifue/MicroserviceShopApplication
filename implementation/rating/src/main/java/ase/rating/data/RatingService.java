package ase.rating.data;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;

public class RatingService {

	public static String getRating(RatingRepository repository, String itemId) {
		List<Rating> txs = findAll(repository, itemId);
		JSONArray j = new JSONArray();
		for (Rating r : txs)
			j.add(r.toJsonObject());
		System.out.println(j.toJSONString());
		return j.toJSONString();
	}

	private static List<Rating> findAll(RatingRepository repository, String itemId) {
		List<Rating> allTx = repository.findAll();
		List<Rating> result = new ArrayList<>();
		for (Rating tx : allTx) {
			// if (tx.getItemId().equals(itemId))
			result.add(tx);
		}
		return result;
	}

	public static String generate_test(Long itemId, RatingRepository repository, Long rating) {
		List<Rating> txs = new ArrayList<>();
		txs.add(new Rating(1l, 1l, "Item1", 5l));
		txs.add(new Rating(3l, 6l, "Item2", 3l));
		txs.add(new Rating(4l, 5l, "Item3", 3l));
		JSONArray j = new JSONArray();
		for (Rating r : txs)
			j.add(r.toJsonObject());
		System.out.println(j.toJSONString());
		return j.toJSONString();

	}
}
