import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BusPassStore {
    private final Path file;
    public BusPassStore(Path file) { this.file=file; }
    private static String encode(String s) { return s.replace("\\","\\\\").replace(",","\\,").replace("\n","\\n"); }
    private static String[] decode(String line) {
        List<String> fields=new ArrayList<>();StringBuilder b=new StringBuilder();boolean escaped=false;
        for(char c:line.toCharArray()) {
            if(escaped){b.append(c=='n'?'\n':c);escaped=false;}
            else if(c=='\\')escaped=true;
            else if(c==','){fields.add(b.toString());b.setLength(0);}
            else b.append(c);
        }
        if(escaped)return null;fields.add(b.toString());return fields.size()==7?fields.toArray(new String[0]):null;
    }
    public void add(String id,String reg,String name,int route,String stop,String validity,double amount)throws IOException {
        if(route<0||amount<0)throw new IllegalArgumentException("Invalid route or amount");
        try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
            w.write(String.join(",",encode(id),encode(reg),encode(name),String.valueOf(route),encode(stop),encode(validity),String.valueOf(amount)));w.newLine();
        }
    }
    public String[] find(String id)throws IOException {
        if(!Files.exists(file))return null;
        try(BufferedReader r=Files.newBufferedReader(file)){String line;while((line=r.readLine())!=null){
            String[] p=decode(line);if(p==null)continue;
            try{Integer.parseInt(p[3]);Double.parseDouble(p[6]);}catch(NumberFormatException e){continue;}
            if(p[0].equals(id))return p;
        }}return null;
    }
    public List<String[]> listByRoute(int route)throws IOException {
        List<String[]> out=new ArrayList<>();if(!Files.exists(file))return out;
        try(BufferedReader r=Files.newBufferedReader(file)){String line;while((line=r.readLine())!=null){
            String[] p=decode(line);if(p==null)continue;
            try{if(Integer.parseInt(p[3])==route)out.add(p);}catch(NumberFormatException ignored){}
        }}return out;
    }
    public static void main(String[] args)throws Exception {
        BusPassStore s=new BusPassStore(Path.of("bus-passes.csv"));
        s.add("P1001","192311001","Arun",12,"Main Gate","2027-05-31",4500);
        s.add("P1002","192311002","Divya, Devi",7,"Library","2027-05-31",3800);
        System.out.println(Arrays.toString(s.find("P1001")));
        System.out.println("Route 7 passes: "+s.listByRoute(7).size());
    }
}