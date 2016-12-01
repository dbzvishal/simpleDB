package test;

import java.util.ArrayList;
import java.util.List;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;
import simpledb.tx.recovery.*;

public class RecoveryTest2 {

	public static void main(String[] args) {
		//Initialize a simpleDB client
		SimpleDB.init("simpleDB");
		SimpleDB.bufferMgr().flushAll(1);
        int lsn = new CheckpointRecord().writeToLog();
        SimpleDB.logMgr().flush(lsn);
		System.out.println("------------");
		
		//Create a buffer manager to hold buffer objects.
		BufferMgr basicBufferMgr = SimpleDB.bufferMgr();
		List<Block> blockList = new ArrayList<>();
		List<Buffer> bufferList = new ArrayList<>();
		RecoveryMgr rm_1 = new RecoveryMgr(1);
		RecoveryMgr rm_2 = new RecoveryMgr(2);
		
		Block blk1 = new Block("block-1.tbl", 1);
		Block blk2 = new Block("block-2.tbl", 1);
		Block blk3 = new Block("block-3.tbl", 1);
		blockList.add(blk1);
		blockList.add(blk2);
		blockList.add(blk3);
		
		Buffer buff1 = basicBufferMgr.pin(blk1);
		bufferList.add(buff1);
		bufferList.add(basicBufferMgr.pin(blk2));
		bufferList.add(basicBufferMgr.pin(blk2));
		
		//Perform log operations for transaction 1
		new SetStringRecord(1, blk1, 4, "yyy", "zzz").writeToLog();
		new SetStringRecord(1, blk2, 4, "www", "xxx").writeToLog();
		new SetStringRecord(2, blk3, 4, "uuu", "vvv").writeToLog();
		new CommitRecord(1).writeToLog();
		new SetStringRecord(2, blk1, 20, "zzz", "ttt").writeToLog();
		
		//Recover both transactions
		System.out.println("Recovery part 1");
		rm_1.recover();
		System.out.println("Recovery part 2");
		rm_2.recover();
	}
}
