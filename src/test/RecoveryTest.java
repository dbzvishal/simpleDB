package test;

import java.util.ArrayList;
import java.util.List;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;
import simpledb.tx.recovery.*;

public class RecoveryTest {

	public static void main(String[] args) {
		//Initialize a simpleDB client
		SimpleDB.init("simpleDB");
		//Create a buffer manager to hold buffer objects.
		BufferMgr basicBufferMgr = SimpleDB.bufferMgr();
		List<Block> blockList = new ArrayList<>();
		List<Buffer> bufferList = new ArrayList<>();
		RecoveryMgr rm_1 = new RecoveryMgr(1);
		RecoveryMgr rm_2 = new RecoveryMgr(2);
		
		Block blk1 = new Block("block-1.tbl", 1);
		//Block blk2 = new Block("block-2.tbl", 2);
		
		blockList.add(blk1);
		
		Buffer buff1 = basicBufferMgr.pin(blk1);
		
		bufferList.add(buff1);
		
		//Perform log operations for transaction 1
		new StartRecord(1).writeToLog();
		new SetIntRecord(1, blk1, 4, 100, 200).writeToLog();
		
		new CommitRecord(1).writeToLog();
		
		//Perform log operations for transaction 2
		new StartRecord(2).writeToLog();
		new SetIntRecord(2, blk1, 8, 10, 20).writeToLog();
		new SetStringRecord(2, blk1, 12, "hello", "world").writeToLog();
		
		//Commit only transaction 1
		rm_1.commit();
		
		//Recover both transactions
		rm_1.recover();
		rm_2.recover();
	}
}
