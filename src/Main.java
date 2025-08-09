import Model.Memory;
import Model.Processor;
import Model.Registers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /**
         * Entrada do arquivo binário
         */
        Scanner sc = new Scanner(System.in);
        System.out.print("Binary path: ");
        String path = sc.nextLine();
        sc.close();

        /**
         * Inicialização dos módulos
         */
        Memory memory = new Memory(path);
        Registers registers = new Registers();


        Processor processor = new Processor(memory, registers);
        processor.run();
    }
}