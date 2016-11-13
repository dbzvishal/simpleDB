package simpledb.log;

import static simpledb.file.Page.INT_SIZE;
import static simpledb.tx.recovery.LogRecord.CHECKPOINT;

import simpledb.file.*;
import simpledb.server.SimpleDB;

import java.util.Iterator;

/**
 * A class that provides the ability to move through the
 * records of the log file in reverse order.
 * 
 * @author Edward Sciore
 */
class ForwardLogIterator implements Iterator<BasicLogRecord> {
   private Block blk = null;
   private Page pg = new Page();
   private int currentrec;
   private int lastBlkNum = -1;
   private int maxOffsetInBlock;
   
   /**
    * Creates an iterator for the records in the log file
    * It starts from the last checkpoint else the start of the file
    * {@link LogMgr#iterator()}.
    */
   ForwardLogIterator() {
      LogIterator iter = SimpleDB.logMgr().backwordIterator();
      while(iter.hasNext()){
    	  BasicLogRecord rec = iter.next();
    	  if(lastBlkNum == -1)
        	  lastBlkNum = iter.getBlock().number();
    	  int op = rec.nextInt();
          switch (op) {
             case CHECKPOINT:
            	 blk = iter.getBlock();
            	 currentrec = iter.getOffset();
            	 break;
          }
          if(blk!=null) 
        	  break;
      }
      if(blk==null){
    	  blk = iter.getBlock();
    	  currentrec = INT_SIZE;
      } else 
    	  currentrec += INT_SIZE;
      
      pg.read(blk);
      this.maxOffsetInBlock = pg.getInt(LogMgr.LAST_POS);
   }
   
   /**
    * Determines if the current log record
    * is the last record in the log file.
    * @return true if there is an earlier record
    */
   public boolean hasNext() {
	   return (currentrec < maxOffsetInBlock || blk.number() < lastBlkNum);	   
   }
   
   /**
    * Moves to the next log record.
    * If the current log record is the latest in its block,
    * then the method moves to the next block,
    * and returns the log record from there.
    * @return the next earliest log record
    */
   public BasicLogRecord next() {
      if (currentrec >= maxOffsetInBlock) 
         moveToNextBlock();
      BasicLogRecord log = new BasicLogRecord(pg, currentrec + INT_SIZE);
      currentrec = pg.getInt(currentrec);
      return log;
   }
   
   public void remove() {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Moves to the next log block,
    * and positions it after the first 4 bytes in that block.
    */
   private void moveToNextBlock() {
      blk = new Block(blk.fileName(), blk.number()+1);
      pg.read(blk);
      currentrec = pg.getInt(INT_SIZE);
	  this.maxOffsetInBlock = pg.getInt(LogMgr.LAST_POS);
   }
}
