package ase.txhistory.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;

public class TransactionHistory {

	public enum IdType {
		BUYER, SELLER;

		public static IdType parse(String input) throws InvalidParameterException {
			if (input.equals("buyer"))
				return BUYER;
			else if (input.equals("seller"))
				return SELLER;
			else
				throw new InvalidParameterException("Input: " + input + "is not a valid IdType!");
		}
	}

	public static String generate(IdType type, TransactionHistoryRepository repository, Long userId) {
		List<Transaction> txs = findAll(type, repository, userId);
		JSONArray j = new JSONArray();
		for (Transaction t : txs)
			j.add(t.toJsonObject());
		System.out.println(j.toJSONString());
		return j.toJSONString();
	}

	public static String generate(TransactionHistoryRepository repository, Long userId) {
		return generate(null, repository, userId);
	}

	private static List<Transaction> findAll(IdType type, TransactionHistoryRepository repository, Long userId) {
		List<Transaction> allTx = repository.findAll();
		List<Transaction> result = new ArrayList<>();
		for (Transaction tx : allTx) {
			// if type == null then select all tx's where id was involved, otherwise select
			// after specific type
			if (type == null ? tx.getBuyerId().equals(userId) || tx.getSellerId().equals(userId)
					: type == IdType.BUYER ? tx.getBuyerId().equals(userId) : tx.getSellerId().equals(userId))
				result.add(tx);
		}
		return result;
	}

	// remove eventually
	public static String generate_test(IdType type, TransactionHistoryRepository repository, Long userId) {
		List<Transaction> txs = new ArrayList<>();
		txs.add(new Transaction(1l, 1l, 10, "item a", 1));
		txs.add(new Transaction(1l, 1l, 11, "item b", 2));
		txs.add(new Transaction(1l, 1l, 12, "item c", 3));
		txs.add(new Transaction(1l, 1l, 13, "item d", 4));
		JSONArray j = new JSONArray();
		for (Transaction t : txs)
			j.add(t.toJsonObject());
		return j.toJSONString();
	}

}
