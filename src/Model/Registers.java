package Model;

/**
 * Representa o banco de registradores do processador.
 * Cada registrador armazena valores de 16 bits (short).
 */
public class Registers {
    private static final int NUM_REGISTERS = 8; // quantidade de registradores
    private final short[] registers;

    public Registers() {
        this.registers = new short[NUM_REGISTERS];
        reset();
    }

    /**
     * Lê o valor de um registrador.
     * @param index índice do registrador (0 a NUM_REGISTERS-1)
     * @return valor armazenado no registrador
     * @throws IllegalArgumentException se o índice for inválido
     */
    public short get(int index) {
        validateIndex(index);
        return registers[index];
    }

    /**
     * Escreve um valor em um registrador.
     * @param index índice do registrador (0 a NUM_REGISTERS-1)
     * @param value valor a ser armazenado
     * @throws IllegalArgumentException se o índice for inválido
     */
    public void set(int index, short value) {
        validateIndex(index);
        registers[index] = value;
    }

    /**
     * Reseta todos os registradores para zero.
     */
    public void reset() {
        for (int i = 0; i < NUM_REGISTERS; i++) {
            registers[i] = 0;
        }
    }

    /**
     * Retorna a quantidade de registradores.
     */
    public int size() {
        return NUM_REGISTERS;
    }

    /**
     * Exibe o estado atual dos registradores (para debug).
     */
    public void dump() {
        System.out.println("=== Registradores ===");
        for (int i = 0; i < NUM_REGISTERS; i++) {
            System.out.printf("R%d: %d (0x%04X)%n", i, registers[i], registers[i]);
        }
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= NUM_REGISTERS) {
            throw new IllegalArgumentException("Índice de registrador inválido: " + index);
        }
    }
}
