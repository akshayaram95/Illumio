package CodingChallenge;

/**
 *
 * @author Akshaya Ramaswamy: axr170131
 * Illumio Coding Assignment:
 *    Implement a firewall which is programmed with a set of predetermined
 *    security rules. As network traffic enters and leaves the machine,
 *    the firewall rules determine whether the traffic should be allowed
 *    or not.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Firewall {

    public class Entry{

        private String direction;
        private String protocol;
        private Value ipAdd;
        private Value port;
        private IpAddress max;
        private IpAddress min;

        public Entry(String direction,String protocol,String port,String ipAddress){
            this.direction=direction;
            this.protocol=protocol;
            this.port=new Value(port);
            this.ipAdd=new Value(ipAddress);
            this.min=new IpAddress(ipAdd.getMin());
            this.max=new IpAddress(ipAdd.getMax());
        }

        @Override
        public int hashCode(){
            return direction.hashCode()*protocol.hashCode()*port.hashCode()*31;
        }

        @Override
        public boolean equals(Object another){
            if(another==null)return false;
            if(this==another)return true;
            Entry other=(Entry) another;
            if(this.direction.equals(other.direction)&& this.protocol.equals(other.protocol)) {
                if (this.port.compareTo(other.port) >= 0) {
                   if((this.min.compareTo(other.min)<=0) &&  (this.max.compareTo(other.max)>=0)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public class IpAddress implements Comparable{

        String part1,part2,part3,part4;

        public IpAddress(String ipadd){
            String[] ipParts=ipadd.split("\\.");
            this.part1=ipParts[0];
            this.part2=ipParts[1];
            this.part3=ipParts[2];
            this.part4=ipParts[3];
         }

        @Override
        public int compareTo(Object o) {
            IpAddress other=(IpAddress)o;
            if(part4.compareTo(other.part4)>=0 && part3.compareTo(other.part3)>=0){
                if(part2.compareTo(other.part2)>=0 && part1.compareTo(other.part1)>=0){
                      return 0;
                }
            }
            return -1;
        }

        public String toString(){
            return part1+"."+part2+"."+part3+"."+part4;
        }
    }

    public class Value implements Comparable{

        private String max;
        private String min;
        String[] val;
        public Value(String port){
            val=port.split("-");
            if(val.length==1){
                min=port;
                max=port;
            }
            else{
                min=val[0];
                max=val[1];
            }
        }

        public String getMin(){
            return min;
        }

        public String getMax(){
            return max;
        }

        @Override
        public int compareTo(Object o) {
            Value other=(Value)o;
            if(this.min.compareTo(other.min)<=0 && this.max.compareTo(other.max)>=0){
                return 0;
            }
            return -1;
        }
    }

    BufferedReader br;
    HashSet<Entry> set;

    /**Constructor taking a single string argument, which is a file path to a CSV file
     * */
    public Firewall(String fileName){
        try {
             br = new BufferedReader(new FileReader(fileName));
            set=new HashSet<>();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**Stores every line of the File as an Entry
     * in a HashSet
     * */
    public void eachLine() throws IOException {
        String str;
        String[] values;
        while ((str=br.readLine())!=null){
            System.out.println(str);
            values=str.split(",");
            set.add(new Entry(values[0],values[1],values[2],values[3]));
        }
    }

    /**This function returns a boolean True if there exists a rule in the file that
     * this object was initialized with that allows traffic with these particular
     * properties, and false otherwise
     * */
    public boolean accept_packet(String direction, String protocol, int port, String ipAddress){
        return set.contains(new Entry(direction,protocol,""+port,ipAddress));
         }

}
