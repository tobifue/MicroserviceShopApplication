package ase.rating.data;

import java.util.List;

import net.minidev.json.JSONArray;

public class RatingService {

	public static String getRating(RatingRepository repository, Long itemId) {
		List<Rating> allRt = repository.findAll();
		double sumrating = 0, iter = 0, finalrating;

		JSONArray j = new JSONArray();
		for (Rating r : allRt) {
			if (r.getItemId() == itemId) {
				sumrating += r.getRating();
				iter += 1;
			}
		}
		finalrating = sumrating / iter;
		System.out.println(finalrating);
		return j.toJSONString();
	}

}