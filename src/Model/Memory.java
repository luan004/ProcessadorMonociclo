package Model;

import Model.Lib;

public class Memory {
    private short[] instructions;
    private short[] data;

    public Memory(String binaryPath) {
        Lib loader = new Lib();
        this.instructions = loader.load_binary(binaryPath); // <- atribuir aqui o array retornado
        this.data = loader.get_data_memory();
    }

    public short readInstruction(int address) {
        return instructions[address];
    }

    public short readData(int address) {
        return data[address];
    }

    public void writeData(int address, short value) {
        data[address] = value;
    }
}
