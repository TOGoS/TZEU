package togos.tzeu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class ProcessCommand
{
	String infilename = null;
	String mapname = null;
	String outfilename = null;
	
	boolean compressSidedefs = false;
	boolean preCompressSidedefs = false;
	boolean listPreCompressSidedefs = false;
	
	public ProcessCommand( String[] args ) {
		for( int i=0; i<args.length; ++i ) {
			if( "-o".equals(args[i]) ) {
				outfilename = args[++i];
			} else if( "-map".equals(args[i]) ) {
				mapname = args[++i];
			} else if( "-compress-sidedefs".equals(args[i]) ) {
				compressSidedefs = true;
			} else if( "-pre-compress-sidedefs".equals(args[i]) ) {
				preCompressSidedefs = true;
			} else if( "-list-pre-compress-sidedefs".equals(args[i]) ) {
				preCompressSidedefs = true;
				listPreCompressSidedefs = true;
			} else if( args[i].length() == 0 || args[i].charAt(0) != '-' ) {
				infilename = args[i];
			} else {
				throw new RuntimeException("Unrecognised argument: "+args[i]);
			}
		}
	}
	
	public int run() {
		if( infilename == null ) {
			System.err.println("No infile specified");
			System.exit(1);
		}
		
		LevelReader lr = new LevelReader();
		
		File infile = new File(infilename);
		if( !infile.exists() ) {
			System.err.println("Input file '"+infilename+"' does not exist.");
			return 1;
		}
		if( !infile.canRead() ) {
			System.err.println("Input file '"+infilename+"' is not readable.");
			return 1;
		}
		RandomAccessFileBlob wad;
		try {
			wad = new RandomAccessFileBlob(new RandomAccessFile(infile,"r"));
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
			return 1;
		}
		
		if( mapname == null ) {
			System.err.println("No map name specified");
			return 1;
		}
		Level l;
		try {
			l = lr.readLevel(wad, mapname);
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
		
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
		if( compressSidedefs ) {
			SidedefCompressor sc = new SidedefCompressor();
			SidedefCompressor.RemapResult pcr = sc.preCompress( l.sidedefs );
			Level newLev = (Level)l.clone();
			newLev.linedefs = sc.applyCompressionToLines(l.linedefs, pcr);
			newLev.sidedefs = pcr.remappedItems;
			System.err.println("-- Sidedef compression --");
			System.err.println("Removed "+(l.sidedefs.size()-pcr.remappedItems.size())+" duplicate sidedefs.");
			l = newLev;
		}
		
		if( outfilename != null && l != null ) {
			LevelWriter lw = new LevelWriter();
			List levLumps = lw.lumpify(l);
			try {
				// todo: mkdirs?
				FileOutputStream os = new FileOutputStream(outfilename);
				WADWriter ww = new WADWriter();
				ww.writeWad("PWAD", levLumps, os);
				os.close();
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
		}
		
		return 0;
	}
	
	public static void main( String[] args ) {
		System.exit(new ProcessCommand(args).run());
	}
}
