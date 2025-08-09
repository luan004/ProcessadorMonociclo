package Model;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Lib {
    private short[] instruction_memory;
    private short[] data_memory;

    public short extract_bits(short value, int bstart, int blength) {
        short mask = (short) ((1 << blength) - 1);
        return (short) ((value >> bstart) & mask);
    }

    public void memory_write(short addr, short value) {
        instruction_memory[addr] = value;
        if(addr > 1 && addr < this.extract_bits(instruction_memory[1], 0, 10)) {
            data_memory[addr] = value;
        }
    }

    public short[] get_data_memory() {
        return data_memory;
    }

    public short[] load_binary(String binary_name) {
        try (FileInputStream fileInputStream = new FileInputStream(binary_name);
             DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {

            long tamanhoArquivo = fileInputStream.getChannel().size();

            int numShorts = (int) (tamanhoArquivo / 2);
            instruction_memory = new short[numShorts];
            data_memory = new short[numShorts];

            for (int i = 0; i < numShorts; i++) {
                int low = dataInputStream.readByte() & 0xFF;
                int high = dataInputStream.readByte() & 0xFF;
                int value = (low | (high << 8)) & 0xFFFF;

                this.memory_write((short) i, (short) value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instruction_memory;
    }
}
