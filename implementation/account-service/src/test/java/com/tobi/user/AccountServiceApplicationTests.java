package com.tobi.user;


import com.tobi.user.entity.Item;
import com.tobi.user.service.AccountService;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test class for account service
 */
@RunWith(SpringRunner.class)
class AccountServiceApplicationTests
{

	/**
	 * converts LocalDateTime to Date
	 */
	public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
		return java.sql.Timestamp.valueOf(dateToConvert);
	}

	/**
	 * calculateProfitTest mocks a transaction history
	 * with a list of items. The calculateProfit function is applied
	 * to ensure the profit is calculated by the right amount and format.
	 */

	@Test
	public void calculateProfitTest() {

		AccountService accService = new AccountService();

		LocalDateTime localDateTime = LocalDateTime.parse("2018-09-16T08:00:00");

		Date formatDateTime = convertToDateViaSqlTimestamp(localDateTime);

		Item employee1 = new Item(1L, 1L, 1L, formatDateTime, 3.4, "test", 34);
		Item employee2 = new Item(2L, 2L, 3L, formatDateTime, 3.4, "test", 34);
		List<Item> transList = new ArrayList<>();
		transList.add(employee1);
		transList.add(employee2);
		Double testProfit = accService.calculateProfit(transList);
		Double profit = 6.8*34;

		assertThat(testProfit, comparesEqualTo(profit));
	}

}