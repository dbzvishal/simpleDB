package simpledb.log;

import static simpledb.file.Page.INT_SIZE;
import simpledb.file.*;
import java.util.Iterator;

/**
 * A class that provides the ability to move through the
 * records of the log file in reverse order.
 * 
 * @author Edward Sciore
 */
class ForwardLogIterator implements Iterator<BasicLogRecord> {
   private Block blk;
   private Page pg = new Page();
   private int currentrec;
   
   /**
    * Creates an iterator for the records in the log file
    * Give the block and the offset of the latest checkpoint or the start of the file as the input
    * {@link LogMgr#iterator()}.
    */
   ForwardLogIterator(Block blk, int offset) {
      this.blk = blk;
      pg.read(blk);
      if(offset == -1)
    	  currentrec = INT_SIZE;
      else
    	  currentrec = offset;
   }
   
   /**
    * Determines if the current log record
    * is the earliest record in the log file.
    * @return true if there is an earlier record
    */
   public boolean hasNext() {
     // return currentrec>0 || blk.number()>0;
   }
   
   /**
    * Moves to the next log record in reverse order.
    * If the current log record is the earliest in its block,
    * then the method moves to the next oldest block,
    * and returns the log record from there.
    * @return the next earliest log record
    */
   public BasicLogRecord next() {
//      if (currentrec == 0) 
//         moveToNextBlock();
//      currentrec = pg.getInt(currentrec);
//      return new BasicLogRecord(pg, currentrec+INT_SIZE);
   }
   
   public void remove() {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Moves to the next log block in reverse order,
    * and positions it after the last record in that block.
    */
   private void moveToNextBlock() {
//      blk = new Block(blk.fileName(), blk.number()-1);
//      pg.read(blk);
//      currentrec = pg.getInt(LogMgr.LAST_POS);
   }
}
