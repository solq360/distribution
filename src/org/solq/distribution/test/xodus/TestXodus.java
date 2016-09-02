package org.solq.distribution.test.xodus;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.solq.distribution.test.Tool;
import org.solq.distribution.test.model.Player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import jetbrains.exodus.env.Transaction;
import jetbrains.exodus.env.TransactionalExecutable;

/***
 * 生成太多小文件，不好维护
 * */
public class TestXodus {
    ObjectMapper OBJECTMAPPER = new ObjectMapper();

    @Test
    public void test() {
	final Environment env = Environments.newInstance("myAppData");
	long start = System.currentTimeMillis();
	env.executeInTransaction(new TransactionalExecutable() {
	    @Override
	    public void execute(@NotNull final Transaction txn) {
		final Store store = env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn);
		int count = Tool.count;
		System.out.println(store.count(txn));
		while (count-- > 0) {
		    Player p = Player.of(count, count + "", count + "");
		    try {
			String json = OBJECTMAPPER.writeValueAsString(p);
			store.put(txn, StringBinding.stringToEntry(p.getId() + ""), StringBinding.stringToEntry(json));
			if (count % 5000 == 0) {
			    txn.flush();
			}
		    } catch (JsonProcessingException e) {
			e.printStackTrace();
		    }

		}
		txn.flush();
	    }
	});
	Tool.printlnTime("用时:", start);
	env.close();
    }
}
