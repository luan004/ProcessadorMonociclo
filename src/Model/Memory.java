package Model;

public class Memory {
    
    private static final int size = 1024;
    private static short[] memory = new short[size];
    
    public static short read (short address){
        return memory[address];
    }
    
    public static void write (short address, short value){
        memory[address] = value;
    }
    
    public static int size (){
        return  size;
    }
    
    public static void printMemory(){
        for (int i = 0; i<= 100; i++){
            System.out.println("");
        }
    }
    
}
