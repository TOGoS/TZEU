package togos.tzeu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import togos.tzeu.io.LevelReader;
import togos.tzeu.io.LevelWriter;
import togos.tzeu.io.RandomAccessFileBlob;
import togos.tzeu.io.WADReader;
import togos.tzeu.io.WADWriter;
import togos.tzeu.level.Level;

public class ProcessCommand
{
	class ArgumentError extends RuntimeException {
		public ArgumentError(String message) { super(message); }
		public ArgumentError(Throwable e) { super(e); }
	}
	
	String mapname = null;
	
	List instructions = new ArrayList();
	
	interface Instruction {
		public Object call( Object input );
	}
	class ReadLevelInstruction implements Instruction {
		protected String mapname;
		public ReadLevelInstruction( String mapname ) {
			this.mapname = mapname;
		}
		public Object call( Object input ) {
			if( !(input instanceof List) ) {
				throw new ArgumentError("Require lump list to read level, got "+input.getClass());
			}
			try {
				List lumps = (List)input;
				LevelReader lr = new LevelReader();
				return lr.readLevel(lumps, mapname, LevelReader.PARSE_ALL|LevelReader.SAVE_UNPARSED_LUMPS);
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
		}
	}
	class CompressSidedefsInstruction implements Instruction {
		public Object call( Object input ) {
			if( !(input instanceof Level) ) {
				throw new ArgumentError("No level specified");
			}
			Level l = (Level)input;
			SidedefCompressor sc = new SidedefCompressor();
			SidedefCompressor.RemapResult pcr = sc.preCompress( l.sidedefs );
			Level newLev = (Level)l.clone();
			newLev.linedefs = sc.applyCompressionToLines(l.linedefs, pcr);
			newLev.sidedefs = pcr.remappedItems;
			System.err.println("-- Sidedef compression --");
			System.err.println("Removed "+(l.sidedefs.size()-pcr.remappedItems.size())+" duplicate sidedefs.");
			System.err.println(l.sidedefs.size()+" sidedefs originally, now "+pcr.remappedItems.size());
			System.err.println(l.linedefs.size()+" linedefs");
			return newLev;
		}
	}
	class ReadWADInstruction implements Instruction {
		protected String infilename;
		public ReadWADInstruction( String infilename ) {
			this.infilename = infilename;
		}
		public Object call( Object input ) {
			if( infilename == null ) {
				throw new ArgumentError("No infile specified");
			}
			File infile = new File(infilename);
			if( !infile.exists() ) {
				throw new ArgumentError("Input file '"+infilename+"' does not exist.");
			}
			if( !infile.canRead() ) {
				throw new ArgumentError("Input file '"+infilename+"' is not readable.");
			}
			
			try {
				RandomAccessFileBlob wadBlob = new RandomAccessFileBlob(new RandomAccessFile(infile,"r"));
				WADReader wr = new WADReader();
				List l = wr.readLumps(wadBlob);
				System.err.println("-- Read "+infilename+" --");
				return l;
			} catch( FileNotFoundException e ) {
				throw new ArgumentError(e);
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
		}
	}
	class WriteWADInstruction implements Instruction {
		protected String outfilename;
		public WriteWADInstruction( String outfilename ) {
			this.outfilename = outfilename;
		}
		public Object call( Object input ) {
			List lumps;
			if( input instanceof List ) {
				lumps = (List)input;
			} else if( input instanceof Level ) {
				LevelWriter lw = new LevelWriter();
				lumps = lw.lumpify((Level)input);
			} else {
				throw new ArgumentError("Can't save non-level, non-lump-list: "+input.getClass());
			}
			try {
				// todo: mkdirs?
				FileOutputStream os = new FileOutputStream(outfilename);
				WADWriter ww = new WADWriter();
				ww.writeWad("PWAD", lumps, os);
				os.close();
				System.err.println("-- Wrote "+outfilename+" --");
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
			return input;
		}
	}
	
	public ProcessCommand( String[] args ) {
		for( int i=0; i<args.length; ++i ) {
			if( "-o".equals(args[i]) ) {
				instructions.add(new WriteWADInstruction(args[++i]));
			} else if( "-map".equals(args[i]) ) {
				instructions.add(new ReadLevelInstruction(args[++i]));
			} else if( "-compress-sidedefs".equals(args[i]) ) {
				instructions.add( new CompressSidedefsInstruction() );
			} else if( args[i].length() == 0 || args[i].charAt(0) != '-' ) {
				instructions.add( new ReadWADInstruction(args[i]) );
			} else {
				throw new RuntimeException("Unrecognised argument: "+args[i]);
			}
		}
	}
	
	public int run() {
		Object stuff = null;
		
		/*
		if( preCompressSidedefs ) {
			SidedefCompressor sc = new SidedefCompressor();
			SidedefCompressor.RemapResult pcr = sc.preCompress( l.sidedefs );
			System.err.println("-- Sidedef pre-compression --");
			System.err.println("Original     : "+l.sidedefs.size());
			System.err.println("Compressed   : "+pcr.remappedItems.size());
			System.err.println("Would remove : "+(l.sidedefs.size()-pcr.remappedItems.size()));
			if( listPreCompressSidedefs ) {
				System.err.println("-- The following lines have compressable sidedefs --");
				for( int i=0; i<l.linedefs.size(); ++i ) {
					Linedef theline = (Linedef)l.linedefs.get(i);
					if( (theline.sidedef1Index >= 0 && pcr.duplicates[theline.sidedef1Index]) ||
						(theline.sidedef2Index >= 0 && pcr.duplicates[theline.sidedef2Index]) ) {
						System.err.println(i);
					}
				}
			}
		}
		*/
		
		for( Iterator it=instructions.iterator(); it.hasNext(); ) {
			Instruction i = (Instruction)it.next();
			stuff = i.call( stuff );
		}
		
		return 0;
	}
	
	public static void main( String[] args ) {
		System.exit(new ProcessCommand(args).run());
	}
}
