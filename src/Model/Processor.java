package Model;

public class Processor {
    private Memory memory;
    private Registers registers;
    private boolean running = true;
    private int pc = 0;

    public Processor(Memory memory, Registers registers) {
        this.memory = memory;
        this.registers = registers;
    }

    public void run() {
        System.out.println("=== Iniciando execução ===");
        int maxCycles = 10000;
        int cycles = 0;
        while (running) {
            cycles++;
            if (cycles >= maxCycles) {
                System.out.println("Limite máximo de ciclos atingido. Encerrando execução para evitar loop infinito.");
                break;
            }

            short instr = memory.readInstruction(pc);
            int format = (instr >> 15) & 0x1; // bit 15 é o formato
            int opcode;

            if (format == 0) {
                opcode = (instr >> 9) & 0x3F;  // 6 bits opcode bits 9..4
            } else {
                opcode = (instr >> 13) & 0x3;  // 2 bits opcode bits 13..12
            }

            //System.out.printf("PC=%d Instr=0x%04X Formato=%d Opcode=%d%n", pc, instr & 0xFFFF, format, opcode);

            if (format == 0) {
                // Formato R: opcode 6 bits, registradores: rd (bits 8-6), rs1 (bits 5-3), rs2 (bits 2-0)
                int rd = (instr >> 6) & 0x7;
                int rs1 = (instr >> 3) & 0x7;
                int rs2 = instr & 0x7;

                switch (opcode) {
                    case 0: // add
                        registers.set(rd, (short)(registers.get(rs1) + registers.get(rs2)));
                        break;
                    case 1: // sub
                        registers.set(rd, (short)(registers.get(rs1) - registers.get(rs2)));
                        break;
                    case 2: // mul
                        registers.set(rd, (short)(registers.get(rs1) * registers.get(rs2)));
                        break;
                    case 3: // div
                        if (registers.get(rs2) == 0) {
                            System.out.println("Erro: divisão por zero.");
                            running = false;
                        } else {
                            registers.set(rd, (short)(registers.get(rs1) / registers.get(rs2)));
                        }
                        break;
                    case 4: // cmp_equal
                        registers.set(rd, (short)(registers.get(rs1) == registers.get(rs2) ? 1 : 0));
                        break;
                    case 5: // cmp_neq
                        registers.set(rd, (short)(registers.get(rs1) != registers.get(rs2) ? 1 : 0));
                        break;
                    case 15: // load
                        registers.set(rd, memory.readData(registers.get(rs1)));
                        break;
                    case 16: // store
                        memory.writeData(registers.get(rs1), registers.get(rs2));
                        break;
                    case 63: // syscall
                        handleSyscall();
                        break;
                    default:
                        System.out.println("Opcode desconhecido no formato R: " + opcode);
                        running = false;
                        break;
                }
            } else {
                // Formato I: opcode 2 bits, rd bits 12-10, imediato bits 9-0
                int rd = (instr >> 10) & 0x7;
                int imediato = instr & 0x3FF; // 10 bits imediato

                switch (opcode) {
                    case 0: // jump
                        pc = imediato - 1; // -1 porque no final do loop incrementa pc
                        break;
                    case 1: // jump_cond (se rd == 1 pula)
                        if (registers.get(rd) == 1) {
                            pc = imediato - 1;
                        }
                        break;
                    case 3: // mov imediato
                        registers.set(rd, (short)imediato);
                        break;
                    default:
                        System.out.println("Opcode desconhecido no formato I: " + opcode);
                        running = false;
                        break;
                }
            }

            pc++;
        }

        System.out.println("=== Execução finalizada ===");
        printRegisters();
    }

    private void handleSyscall() {
        short service = registers.get(0);
        switch (service) {
            case 0: // encerra o programa
                running = false;
                break;
            case 1: // print string (endereço em r1)
                int addr = registers.get(1);
                short c;
                while ((c = memory.readData(addr)) != 0) {
                    System.out.print((char)c);
                    addr++;
                }
                System.out.println();
                break;
            case 2: // print newline
                System.out.println();
                break;
            case 3: // print integer em r1
                System.out.println(registers.get(1));
                break;
            case 6: // sleep
                // Aqui você pode fazer uma pausa na execução, ex:
                try {
                    int sleepTime = registers.get(1); // suponha que o tempo está no registrador 1
                    Thread.sleep(sleepTime * 1000L);  // tempo em segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;

            case 7: // get time
                int currentTime = (int) (System.currentTimeMillis() / 1000); // tempo em segundos desde epoch
                registers.set(1, (short) currentTime);
                break;
            default:
                System.out.println("Syscall não implementado: " + service);
                break;
        }
    }

    private void printRegisters() {
        System.out.println("=== Registradores ===");
        for (int i = 0; i < 8; i++) {
            System.out.printf("R%d: %d (0x%04X)%n", i, registers.get(i), registers.get(i) & 0xFFFF);
        }
    }
}
