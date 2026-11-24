import java.io.*;
import java.nio.file.*;

public class BufferedLineCounter {
    public static long countUnbuffered(Path file)throws IOException {
        long lines=0;boolean hasData=false;int ch;
        try(InputStream in=Files.newInputStream(file)){while((ch=in.read())!=-1){hasData=true;if(ch=='\n')lines++;}}
        if(hasData&&lastByte(file)!='\n')lines++;return lines;
    }
    public static long countBuffered(Path file)throws IOException {
        long lines=0;boolean hasData=false;int ch;
        try(BufferedInputStream in=new BufferedInputStream(Files.newInputStream(file))){while((ch=in.read())!=-1){hasData=true;if(ch=='\n')lines++;}}
        if(hasData&&lastByte(file)!='\n')lines++;return lines;
    }
    private static int lastByte(Path file)throws IOException {
        long size=Files.size(file);if(size==0)return -1;
        try(RandomAccessFile r=new RandomAccessFile(file.toFile(),"r")){r.seek(size-1);return r.read();}
    }
    public static void main(String[] args)throws Exception {
        Path file=Path.of(args.length==0?"attendance.log":args[0]);
        if(!Files.exists(file))Files.writeString(file,"Line one\nLine two\nLine three\n");
        long start=System.nanoTime(),a=countUnbuffered(file),t1=System.nanoTime()-start;
        start=System.nanoTime();long b=countBuffered(file),t2=System.nanoTime()-start;
        System.out.println("Unbuffered lines: "+a+" time(ns): "+t1);
        System.out.println("Buffered lines: "+b+" time(ns): "+t2);
    }
}