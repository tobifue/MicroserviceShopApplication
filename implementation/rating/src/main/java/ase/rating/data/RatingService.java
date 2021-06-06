package ase.rating.data;

import java.util.List;

import net.minidev.json.JSONObject;

public class RatingService {

	public static String getRating(RatingRepository repository, String itemName) {
		List<Rating> allRt = repository.findAll();
		double sumrating = 0, iter = 0, finalrating;
		for (Rating r : allRt) {
			if (r.getItemName().equalsIgnoreCase(itemName)) {
				sumrating += r.getRating();
				iter += 1;
			}
		}
		finalrating = sumrating / iter;
		System.out.println(finalrating);

		JSONObject j = new JSONObject();
		j.put("rating: ", finalrating);
		return j.toJSONString();
	}

}